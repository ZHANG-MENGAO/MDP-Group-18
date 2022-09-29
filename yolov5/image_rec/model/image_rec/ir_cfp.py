from ctypes import *  # Import libraries
import math
import random
import os
import cv2
import numpy as np
import time
import darknet


def convertBack(x, y, w, h):
    xmin = int(round(x - (w / 2)))
    xmax = int(round(x + (w / 2)))
    ymin = int(round(y - (h / 2)))
    ymax = int(round(y + (h / 2)))
    return xmin, ymin, xmax, ymax


def get_image_id(detection):
    id_dict = {'a': 20, 'b': 21, 'c': 22, 'd': 23,
               'e': 24, 'f': 25, 'g': 26, 'h': 27,
               's': 28, 't': 29, 'u': 30,
               'v': 31, 'w': 32, 'x': 33, 'y': 34,
               'z': 35, 'one': 11, 'two': 12, 'three': 13, 'four': 14,
               'five': 15, 'six': 16, 'seven': 17, 'eight': 18,
               'nine': 19, 'arrow_up': 36, 'arrow_down': 37, 'arrow_left': 39,
               'arrow_right': 38, 'circle': 40}
    image_id = str(id_dict[detection])
    return image_id


pos_list = [[0, 0], [13, 1], [10, 5], [10, 12], [13, 12], [6, 10]][1:]


def cvDrawBoxes(detections, img):
    # Colored labels dictionary
    color_dict = {
        'a': [0, 255, 255], 'b': [238, 123, 158], 'c': [24, 245, 217], 'd': [224, 119, 227],
        'e': [154, 52, 104], 'f': [179, 50, 247], 'g': [180, 164, 5], 'h': [82, 42, 106],
        'r': [201, 25, 52], 's': [62, 17, 209], 't': [60, 68, 169], 'u': [199, 113, 167],
        'v': [19, 71, 68], 'w': [161, 83, 182], 'x': [75, 6, 145], 'y': [100, 64, 151],
        'z': [156, 116, 171], 'one': [88, 9, 123], 'two': [181, 86, 222], 'three': [116, 238, 87],
        'four': [74, 90, 143],
        'five': [249, 157, 47], 'six': [26, 101, 131], 'seven': [195, 130, 181], 'eight': [242, 52, 233],
        'nine': [131, 11, 189], 'arrow_up': [221, 229, 176], 'arrow_down': [193, 56, 44], 'arrow_left': [139, 53, 137],
        'arrow_right': [102, 208, 40], 'circle': [61, 50, 7], 'bullseye': [65, 82, 186]
    }
    if detections:
        # print(detections)
        detection = detections[0]
        # for detection in detections:
        name_tag = str(detection[0].decode())
        try:
            color = color_dict[name_tag]
            xmin, ymin, xmax, ymax = convertBack(
                float(detection[2][0]), float(detection[2][1]), float(detection[2][2]), float(detection[2][3]))
            pt1 = (xmin, ymin)
            pt2 = (xmax, ymax)
            cv2.rectangle(img, pt1, pt2, color, 3)
            cv2.putText(img,
                        detection[0].decode() +
                        " [" + str(round(detection[1] * 100, 2)) + "]",
                        (pt1[0], pt2[1] + 25), cv2.FONT_HERSHEY_SIMPLEX, 0.8,
                        color, 3)
        except:
            pass
    return img


netMain = None
metaMain = None
altNames = None

import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

while True:
    try:
        print('trying')
        s.connect(("192.168.3.1", 2222))
        print('connected!')
        break
    except:
        continue


def YOLO():
    global metaMain, netMain, altNames
    configPath = ".\cfg\custom-yolov4-tiny-detector.cfg"  # Path to cfg
    weightPath = ".\custom-yolov4-tiny-detector_best.weights"  # Path to weights
    metaPath = ".\cfg\coco.data"  # Path to meta data
    if not os.path.exists(configPath):  # Checks whether file exists otherwise return ValueError
        raise ValueError("Invalid config path `" +
                         os.path.abspath(configPath) + "`")
    if not os.path.exists(weightPath):
        raise ValueError("Invalid weight path `" +
                         os.path.abspath(weightPath) + "`")
    if not os.path.exists(metaPath):
        raise ValueError("Invalid data file path `" +
                         os.path.abspath(metaPath) + "`")
    if netMain is None:  # Checks the metaMain, NetMain and altNames. Loads it in script
        netMain = darknet.load_net_custom(configPath.encode(
            "ascii"), weightPath.encode("ascii"), 0, 1)  # batch size = 1
    if metaMain is None:
        metaMain = darknet.load_meta(metaPath.encode("ascii"))
    if altNames is None:
        try:
            with open(metaPath) as metaFH:
                metaContents = metaFH.read()
                import re
                match = re.search("names *= *(.*)$", metaContents,
                                  re.IGNORECASE | re.MULTILINE)
                if match:
                    result = match.group(1)
                else:
                    result = None
                try:
                    if os.path.exists(result):
                        with open(result) as namesFH:
                            namesList = namesFH.read().strip().split("\n")
                            altNames = [x.strip() for x in namesList]
                except TypeError:
                    pass
        except Exception:
            pass
    # netMain = cv2.dnn.readNet(weightPath, configPath)
    # netMain.setPreferableBackend(cv2.dnn.DNN_BACKEND_CUDA)
    # netMain.setPreferableTarget(cv2.dnn.DNN_TARGET_CUDA)
    cap = cv2.VideoCapture("http://192.168.3.1/html/cam_pic_new.php")
    # cap = cv2.VideoCapture(0)
    # cap = cv2.VideoCapture(r"C:\Users\Raj\Downloads\vi_0008_20220215_195024.mp4")
    frame_width = int(cap.get(3))  # Returns the width and height of capture video
    frame_height = int(cap.get(4))
    # Set out for video writer

    print("Starting the YOLO loop...")

    # Create an image we reuse for each detect

    darknet_image = darknet.make_image(frame_width, frame_height, 3)

    while True:  # Load the input frame and write output frame.
        try:
            ret, frame_read = cap.read()  # Capture frame and return true if frame present
            # For Assertion Failed Error in OpenCV
            if not ret:  # Check if frame present otherwise he break the while loop
                break

            frame_rgb = cv2.cvtColor(frame_read,
                                     cv2.COLOR_BGR2RGB)  # Convert frame into RGB from BGR and resize accordingly
            frame_resized = cv2.resize(frame_rgb,
                                       (frame_width, frame_height),
                                       interpolation=cv2.INTER_LINEAR)

            darknet.copy_image_from_bytes(darknet_image,
                                          frame_resized.tobytes())  # Copy that frame bytes to darknet_image

            detections = darknet.detect_image(netMain, metaMain, darknet_image,
                                              thresh=0.50)  # Detection occurs at this line and return detections, for customize we can change the threshold.
            image = cvDrawBoxes(detections, frame_resized)
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            # (w//2, h//2)
            # cv2.imshow('Demo', image)                                    # Display Image window
            # cv2.waitKey(1)
            print(detections)
            if len(detections) == 2:
                if detections[0][2][0] < detections[1][2][0]:
                    s.send("r".encode("UTF-8"))
                    continue
                elif detections[0][2][0] > detections[1][2][0]:
                    s.send("l".encode("UTF-8"))
                    continue
            # elif len(detections)==1:
            #     s.send("c".encode("UTF-8"))
            #     continue
        except KeyboardInterrupt:
            print('End of image recognition.')
            break
    cap.release()  # For releasing cap and out.


if __name__ == "__main__":
    YOLO()  # Calls the main function YOLO()
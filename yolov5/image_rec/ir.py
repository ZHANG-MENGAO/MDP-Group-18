from ctypes import *  # Import libraries
import os
import cv2
#import darknet
import socket
import ast
import numpy as np


# supporting function
def convertBack(x, y, w, h):
    xmin = int(round(x - (w / 2)))
    xmax = int(round(x + (w / 2)))
    ymin = int(round(y - (h / 2)))
    ymax = int(round(y + (h / 2)))
    return xmin, ymin, xmax, ymax


# get image id
def get_image_id(detection):
    id_dict = {'a': 20, 'b': 21, 'c': 22, 'd': 23,
               'e': 24, 'f': 25, 'g': 26, 'h': 27,
               's': 28, 't': 29, 'u': 30,
               'v': 31, 'w': 32, 'x': 33, 'y': 34,
               'z': 35, 'one': 11, 'two': 12, 'three': 13, 'four': 14,
               'five': 15, 'six': 16, 'seven': 17, 'eight': 18,
               'nine': 19, 'arrow_up': 36, 'arrow_down': 37, 'arrow_left': 39,
               'arrow_right': 38, 'circle': 40, "bullseye": "bullseye"}
    image_id = str(id_dict[detection])
    return image_id


# draw bounding boxes
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
                        (pt1[0], pt1[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 1,
                        color, 3)
        except:
            pass
    return img


netMain = None
metaMain = None
altNames = None


def load_darknet_model(config, weight, meta):
    if not os.path.exists(config):  # Checks whether file exists otherwise return ValueError
        raise ValueError("Invalid config path `" +
                         os.path.abspath(config) + "`")
    if not os.path.exists(weight):
        raise ValueError("Invalid weight path `" +
                         os.path.abspath(weight) + "`")
    if not os.path.exists(meta):
        raise ValueError("Invalid data file path `" +
                         os.path.abspath(meta) + "`")
    netMain = darknet.load_net_custom(config.encode(
        "ascii"), weight.encode("ascii"), 0, 1)  # batch size = 1
    metaMain = darknet.load_meta(meta.encode("ascii"))
    try:
        with open(meta) as metaFH:
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
    return netMain, metaMain, altNames


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
while True:
    try:
        print('trying')
        s.connect(("192.168.18.1", 2222))
        print('connected!')
        break
    except:
        continue


def get_detections(frame_read, frame_width, frame_height, darknet_image, netMain, metaMain):
    frame_rgb = cv2.cvtColor(frame_read, cv2.COLOR_BGR2RGB)
    frame_resized = cv2.resize(frame_rgb,
                               (frame_width, frame_height),
                               interpolation=cv2.INTER_LINEAR)

    darknet.copy_image_from_bytes(darknet_image, frame_resized.tobytes())

    detections = darknet.detect_image(netMain, metaMain, darknet_image, thresh=0.85)

    image = cvDrawBoxes(detections, frame_resized)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    return detections, image


def send_android_string(detections):
    # android_string1='ANDROID|TARGET ,'
    android_string = 'ANDROID,'

    target_id = get_image_id(str(detections[0][0])[2:-1])
    # print(target_id)
    if target_id != 'bullseye':
        # android_string1+="<"+obstacle_number+">"+','+"<"+target_id+">"
        android_string += str(target_id) + ','
        print(android_string)
        s.send(android_string.encode("UTF-8"))
    else:
        # print('s')
        s.send("bullseye".encode("UTF-8"))


def ImageRec():
    #global metaMain, netMain, altNames
    #configPath = ".\cfg\custom-yolov4-tiny-detector.cfg"
    #weightPath = ".\custom-yolov4-tiny-detector_best.weights"
    #metaPath = ".\cfg\coco.data"

    # load darknet model
    #netMain, metaMain, altNames = load_darknet_model(configPath, weightPath, metaPath)
    record = {}
    # Get livevideo from RPi Camera
    # cap = cv2.VideoCapture("http://192.168.18.1/html/cam_pic_new.php")
    cap = cv2.VideoCapture("http://192.168.18.1:8000")
    print(cap)
    # Get livevideo from PC Webcam
    # cap = cv2.VideoCapture(0)
    # Get video from path
    # cap = cv2.VideoCapture(r"C:\Users\Raj\Downloads\vi_0008_20220215_195024.mp4")
    frame_width = int(cap.get(3))
    frame_height = int(cap.get(4))
    print("waiting for android go")
    recvMsg = s.recv(1024).decode("UTF-8")
    print('msg: ', msg)
    # recvMsg = 'g'
    if str(recvMsg) == 'g':
        print("Starting the YOLO loop...")
        keep_going = True
        while keep_going:
            try:
                # darknet_image = darknet.make_image(frame_width, frame_height, 3)
                ret, frame_read = cap.read()
                if not ret:
                    break
                #detections, image = get_detections(frame_read, frame_width, frame_height, darknet_image, netMain,
                #                                   metaMain)

                print(frame_read)

                cv2.imshow('Demo', image)
                cv2.waitKey(1)
                try:
                    if detections:
                        send_android_string(detections)
                        try:
                            if record[detections[0][0]] < round(detections[0][1] * 100, 2):
                                record[detections[0][0]] = round(detections[0][1] * 100, 2)
                                cv2.imwrite("./Demo/" + str(get_image_id(str(detections[0][0])[2:-1])) + ".jpg", image)
                        except:
                            if get_image_id(str(detections[0][0])[2:-1]) != 'bullseye':
                                record[detections[0][0]] = round(detections[0][1] * 100, 2)
                except KeyboardInterrupt:
                    print('End of image recognition.')
                    break
                continue
            except:
                cap.release()
                print(record)
                continue


if __name__ == "__main__":
    ImageRec()
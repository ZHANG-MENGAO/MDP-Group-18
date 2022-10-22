from ctypes import *  # Import libraries
import math
import random
import os
import cv2
import numpy as np
import time
# import darknet
from PIL import Image
import sys
import glob
import ast
import socket

# adding Folder_2 to the system path
sys.path.insert(0, 'C:/Users/guanl/yolov5/image_rec/model')
# sys.path.insert(0, '/yolov5/image_rec')

from detect import main, parse_opt
# run()


def convertBack(x, y, w, h):
    xmin = int(round(x - (w / 2)))
    xmax = int(round(x + (w / 2)))
    ymin = int(round(y - (h / 2)))
    ymax = int(round(y + (h / 2)))
    return xmin, ymin, xmax, ymax


def get_image_id(detection):
    id_dict = {'A': 20, 'B': 21, 'C': 22, 'D': 23,
               'E': 24, 'F': 25, 'G': 26, 'H': 27,
               'S': 28, 'T': 29, 'U': 30,
               'V': 31, 'W': 32, 'X': 33, 'Y': 34,
               'Z': 35, '1': 11, '2': 12, '3': 13, '4': 14,
               '5': 15, '6': 16, '7': 17, '8': 18,
               '9': 19, 'Up': 36, 'Down': 37, 'Left': 39,
               'Right': 38, 'Stop': 40, "Bullseye": "bullseye"}
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


def organise_images(image_storage):
    row1 = []
    # row2=[]
    counter = 1
    flag = 0
    for image in image_storage:
        row1.append(image)
    print(len(image_storage))

    rows = [row1]
    flag = 0
    horizontal = []
    for row in rows:
        horiz = np.hstack(row)
        horizontal.append(horiz)
    vertical_attachment = np.vstack(horizontal)

    return vertical_attachment

import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

while True:
    try:
        print('trying')
        s.connect(("192.168.18.1", 2222))
        print('connected!')
        break
    except:
        continue


def YOLO():
    # global metaMain, netMain, altNames
    # configPath = ".\cfg\custom-yolov4-tiny-detector.cfg"  # Path to cfg
    # weightPath = ".\custom-yolov4-tiny-detector_best.weights"  # Path to weights
    # metaPath = ".\cfg\coco.data"  # Path to meta data
    # if not os.path.exists(configPath):  # Checks whether file exists otherwise return ValueError
    #     raise ValueError("Invalid config path `" +
    #                      os.path.abspath(configPath) + "`")
    # if not os.path.exists(weightPath):
    #     raise ValueError("Invalid weight path `" +
    #                      os.path.abspath(weightPath) + "`")
    # if not os.path.exists(metaPath):
    #     raise ValueError("Invalid data file path `" +
    #                      os.path.abspath(metaPath) + "`")
    # if netMain is None:  # Checks the metaMain, NetMain and altNames. Loads it in script
    #     netMain = darknet.load_net_custom(configPath.encode(
    #         "ascii"), weightPath.encode("ascii"), 0, 1)  # batch size = 1
    # if metaMain is None:
    #     metaMain = darknet.load_meta(metaPath.encode("ascii"))
    # if altNames is None:
    #     try:
    #         with open(metaPath) as metaFH:
    #             metaContents = metaFH.read()
    #             import re
    #             match = re.search("names *= *(.*)$", metaContents,
    #                               re.IGNORECASE | re.MULTILINE)
    #             if match:
    #                 result = match.group(1)
    #             else:
    #                 result = None
    #             try:
    #                 if os.path.exists(result):
    #                     with open(result) as namesFH:
    #                         namesList = namesFH.read().strip().split("\n")
    #                         altNames = [x.strip() for x in namesList]
    #             except TypeError:
    #                 pass
    #     except Exception:
    #         pass
    # netMain = cv2.dnn.readNet(weightPath, configPath)
    # netMain.setPreferableBackend(cv2.dnn.DNN_BACKEND_CUDA)
    # netMain.setPreferableTarget(cv2.dnn.DNN_TARGET_CUDA)
    url = 'http://192.168.18.1:8000/stream.mjpg'
    cap = cv2.VideoCapture(url)
    # cap = cv2.VideoCapture(0)
    # cap = cv2.VideoCapture(r"C:\Users\Raj\Downloads\vi_0008_20220215_195024.mp4")
    # frame_width = int(cap.get(3))  # Returns the width and height of capture video
    # frame_height = int(cap.get(4))
    # Set out for video writer

    print("Starting the YOLO loop...")

    # Create an image we reuse for each detect
    image_num = 1

    while image_num < 3:  # Load the input frame and write output frame.
        recvMsg = s.recv(1024).decode("UTF-8")
        # print('recvMsg: ', recvMsg)

        # recvMsg = 'g'
        if str(recvMsg) == 'g':
            print("Starting the YOLO loop...")
            # s.send("p".encode("UTF-8"))
            # time.sleep(2)
            # image_list = []
            # for filename in glob.glob('imageFolder/*.gif'):  # assuming gif
            #     im = Image.open(filename)
            # url = 'http://192.168.18.1:8000/stream.mjpg'
            cap = cv2.VideoCapture(url)

            ret, frame_read = cap.read()

            cv2.imwrite('C:/Users/guanl/yolov5/image_rec/model/images/test_image{0}.jpg'.format(image_num), frame_read)
            cv2.imwrite('C:/Users/guanl/yolov5/image_rec/imageFolder/test_image{0}.jpg'.format(image_num), frame_read)


            #time.sleep(2)
            #run()
            print('identifying')
            label = main(parse_opt())
            print('the label is: ', label)
            try:
                os.remove('C:/Users/guanl/yolov5/image_rec/model/images/test_image{0}.jpg'.format(image_num))
            except:
                pass

            image_num += 1

            if label == 'Left':
                s.send("l".encode("UTF-8"))
                continue
            elif label == 'Right':
                s.send("r".encode("UTF-8"))

                continue
            else:
                continue
             #s.send("c".encode("UTF-8"))


        # except KeyboardInterrupt:
        #     print('End of image recognition.')
        #     break
    #cap.release()  # For releasing cap and out.


if __name__ == "__main__":
    YOLO()  # Calls the main function YOLO()

    path = r"C:/Users/guanl/yolov5/image_rec/model/runs/detect"

    lst = []
    for i in range(2):
        lst.append('test_image{0}.jpg'.format(i + 1))

    image_storage = []

    for idx, img in enumerate(lst):
        image = cv2.imread(path + '/exp{}'.format(idx + 2) + '/' + img)
        image = cv2.resize(image, (416, 416))
        image_storage.append(image)

    collage = organise_images(image_storage)
    cv2.imwrite("./Demo/detection_collage.jpg", collage)

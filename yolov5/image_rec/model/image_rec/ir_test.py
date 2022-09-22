from ctypes import *  # Import libraries
from detect.py import run
import os
import cv2
#import darknet
import socket
import ast
import numpy as np
from PIL import Image



# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# while True:
#     try:
#         print('trying')
#         s.connect(("192.168.18.1", 2222))
#         print('connected!')
#         break
#     except:
#         continue
#
# record = {}
    # Get livevideo from RPi Camera
    # cap = cv2.VideoCapture("http://192.168.18.1/html/cam_pic_new.php")

import pafy
import cv2

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


# url = "http://192.168.18.1:8000"
url = 'http://192.168.18.1:8000/stream.mjpg'
# video = pafy.new(url)
# best = video.getbest(preftype="mp4")

cap = cv2.VideoCapture(url)

# cap = cv2.VideoCapture("http://192.168.18.1:8000")
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
print('cap: ', cap.read())
    # Get livevideo from PC Webcam
    # cap = cv2.VideoCapture(0)
    # Get video from path
    # cap = cv2.VideoCapture(r"C:\Users\Raj\Downloads\vi_0008_20220215_195024.mp4")
frame_width = int(cap.get(3))
frame_height = int(cap.get(4))
    #print("waiting for android go")
#recvMsg = s.recv(1024).decode("UTF-8")
#print('msg: ', recvMsg)
# recvMsg = 'g'
#    if str(recvMsg) == 'g':
#        print("Starting the YOLO loop...")
keep_going = True
while keep_going:
    #try:
        # darknet_image = darknet.make_image(frame_width, frame_height, 3)
    ret, frame_read = cap.read()
    print('ret: ', ret)
    print('frame_read: ', frame_read)
    image = Image.fromarray(frame_read, 'RGB')
    #img.show()
    # if not ret:
    #     break
    #             detections, image = get_detections(frame_read, frame_width, frame_height, darknet_image, netMain,

    cv2.imwrite('color_img.jpg', frame_read)
    cv2.imshow("image", frame_read)
    cv2.waitKey()

    run



#     try:
#         #     if detections:
#         #         send_android_string(detections)
#         #         try:
#         #             if record[detections[0][0]] < round(detections[0][1] * 100, 2):
#         #                 record[detections[0][0]] = round(detections[0][1] * 100, 2)
#         #                 cv2.imwrite("./Demo/" + str(get_image_id(str(detections[0][0])[2:-1])) + ".jpg", image)
#         #         except:
#         #             if get_image_id(str(detections[0][0])[2:-1]) != 'bullseye':
#         #                 record[detections[0][0]] = round(detections[0][1] * 100, 2)
#         # except KeyboardInterrupt:
#         #     print('End of image recognition.')
#         #     break
#         # continue
#     except:
#         cap.release()
#         print(record)
#         continue
#
# #
# #
# recvMsg = s.recv(1024).decode("UTF-8")
# print('msg: ', recvMsg)
#   g  # recvMsg = 'g'
#     if str(recvMsg) == 'g':
#         print("Starting the YOLO loop...")
#         keep_going = True
#         while keep_goin:
#             try:
#                 # darknet_image = darknet.make_image(frame_width, frame_height, 3)
#                 ret, frame_read = cap.read()
#                 if not ret:
#                     break
#                 #detections, image = get_detections(frame_read, frame_width, frame_height, darknet_image, netMain,
#                 #                                   metaMain)
#
#                 print(frame_read)
#
#                 cv2.imshow('Demo', image)
#                 cv2.waitKey(1)
#                 try:
#                     if detections:
#                         send_android_string(detections)
#                         try:
#                             if record[detections[0][0]] < round(detections[0][1] * 100, 2):
#                                 record[detections[0][0]] = round(detections[0][1] * 100, 2)
#                                 cv2.imwrite("./Demo/" + str(get_image_id(str(detections[0][0])[2:-1])) + ".jpg", image)
#                         except:
#                             if get_image_id(str(detections[0][0])[2:-1]) != 'bullseye':
#                                 record[detections[0][0]] = round(detections[0][1] * 100, 2)
#                 except KeyboardInterrupt:
#                     print('End of image recognition.')
#                     break
#                 continue
#             except:
#                 cap.release()
#                 print(record)
#                 continue
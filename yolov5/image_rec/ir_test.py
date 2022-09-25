from ctypes import *  # Import libraries
# from image_rec.yolov5.detect import run
#from yolov5.detect import run
# from detect.py import run
import os
import cv2
#import darknet
import socket
import ast
import numpy as np
from PIL import Image
import sys

# adding Folder_2 to the system path
sys.path.insert(0, 'C:/Users/guanl/yolov5/image_rec/model')
# sys.path.insert(0, '/yolov5/image_rec')

from detect import main, parse_opt
# run()

def send_android_string(detections):
    # android_string1='ANDROID|TARGET ,'
    android_string = 'ANDROID,'

    target_id = get_image_id(detections)
    # print(target_id)
    if target_id != 'bullseye':
        # android_string1+="<"+obstacle_number+">"+','+"<"+target_id+">"
        android_string += str(target_id) + ','
        print(android_string)
        s.send(android_string.encode("UTF-8"))
    else:
        # print('s')
        s.send("bullseye".encode("UTF-8"))

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

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
while True:
    try:
        print('trying')
        s.connect(("192.168.18.1", 2222))
        print('connected!')
        break
    except:
        continue

# record = {}
    # Get livevideo from RPi Camera
    # cap = cv2.VideoCapture("http://192.168.18.1/html/cam_pic_new.php")


# url = "http://192.168.18.1:8000"
url = 'http://192.168.18.1:8000/stream.mjpg'
# video = pafy.new(url)
# best = video.getbest(preftype="mp4")

cap = cv2.VideoCapture(url)

# cap = cv2.VideoCapture("http://192.168.18.1:8000")
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
#print('cap: ', cap.read())
    # Get livevideo from PC Webcam
    # cap = cv2.VideoCapture(0)
    # Get video from path
    # cap = cv2.VideoCapture(r"C:\Users\Raj\Downloads\vi_0008_20220215_195024.mp4")
frame_width = int(cap.get(3))
frame_height = int(cap.get(4))
print("waiting for android go")
recvMsg = s.recv(1024).decode("UTF-8")
#print('recvMsg: ', recvMsg)
image_num = 1
recvMsg = 'g'
if str(recvMsg) == 'g':
    print("Starting the YOLO loop...")
    keep_going = True
    while keep_going:
        #try:
        ret, frame_read = cap.read()
        #print('ret: ', ret)
        #print('frame_read: ', frame_read)
        image = Image.fromarray(frame_read, 'RGB')

        cv2.imwrite('C:/Users/guanl/yolov5/image_rec/model/images/test_image{0}.jpg'.format(image_num), frame_read)
        # cv2.imwrite('C:/Users/guanl/yolov5/image_rec/model/images/test_image{0}.jpg'.format(image_num), frame_read)
    # cv2.imwrite('test_image', frame_read)
        cv2.imshow("image", frame_read)
        cv2.waitKey()

    # run()
        print('identifying')
        label = main(parse_opt())
        print('this is the returned label ', label)

        try:
            os.remove('C:/Users/guanl/yolov5/image_rec/model/images/test_image{0}.jpg'.format(image_num))
        except:
            pass

        send_android_string(label)

        #if label == 'Left':
        s.send("l".encode("UTF-8"))


        #except:
        #cap.release()
            # print(record)
        #    continue
#print('msg: ', recvMsg)
# recvMsg = 'g'
#     if str(recvMsg) == 'g':
#         print("Starting the YOLO loop...")
#         keep_going = True


        # darknet_image = darknet.make_image(frame_width, frame_height, 3)

    #img.show()
    # if not ret:
    #     break
    #             detections, image = get_detections(frame_read, frame_width, frame_height, darknet_image, netMain,
    # save to '/path/to/destination/image.png'



    # try:
    #     os.remove('C:/Users/guanl/yolov5/image_rec/model/images/test_image{0}.jpg'.format(image_num))
    # except:
    #     pass






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
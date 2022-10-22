import cv2
import numpy as np
import os
import ast
import socket


# path="./Demo/"
# dirs = os.listdir(path)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

while True:
    try:
        print('trying')
        s.connect(("192.168.18.1", 2763))
        print('Algo testing success')
        break
    except:
        continue
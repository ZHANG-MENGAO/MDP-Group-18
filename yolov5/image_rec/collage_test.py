# importing the modules

import cv2
import numpy as np
import os
import ast
import socket

def organise_images(image_storage):
    row1=[]
    #row2=[]
    counter=1
    flag = 0
    for image in image_storage:

        row1.append(image)
    print(len(image_storage))

    rows = [row1]
    flag = 0
    horizontal=[]
    for row in rows:
        horiz=np.hstack(row)
        horizontal.append(horiz)
    vertical_attachment=np.vstack(horizontal)

    return vertical_attachment
path=r"C:/Users/guanl/yolov5/image_rec/model/runs/detect"


lst = []
for i in range(2):
    lst.append('test_image{0}.jpg'.format(i+1))

image_storage = []

for idx, img in enumerate(lst):
    image = cv2.imread(path + '/exp{}'.format(idx+2) + '/' +  img)
    image = cv2.resize(image, (416, 416))
    image_storage.append(image)

collage = organise_images(image_storage)
cv2.imwrite("./Demo/detection_collage.jpg", collage)


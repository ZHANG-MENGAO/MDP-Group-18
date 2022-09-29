# importing the modules

import cv2
import numpy as np
import os
import ast
import socket


path="./Demo/"
dirs = os.listdir(path)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
while True:
    try:
        print('trying')
        s.connect(("192.168.18.1", 4444))
        print('Image ID retriever connected!')
        break
    except:
        continue

recvMsg = s.recv(1024).decode("UTF-8")
lst=recvMsg.split(',')
lst.sort()
lst=[x+'.jpg' for x in lst]
lst.append('temp.JPG')

dirs=list(set(dirs).intersection(set(lst)))

# 5 --> 6 (dont remove blank) 6
# 6 --> 6 (remove blank) 7
# 7 --> 8 (dont remove blank) 8
# 8 --> 8 (remove blank) 9

def make_image_lst(path,dirs):
    image_storage=[]
    length=len(dirs)
    if (length%2)==1:
        for file in dirs:
            if 'temp' not in file:
                image=cv2.imread(path+'/'+file)
                image=cv2.resize(image,(416,416))
                image_storage.append(image)
    else:
       for file in dirs:
            image=cv2.imread(path+'/'+file)
            image=cv2.resize(image,(416,416))
            image_storage.append(image)
    return image_storage


def organise_images(image_storage):
    row1=[]
    row2=[]
    counter=1
    for image in image_storage:
        if counter==1:
            row1.append(image)
            counter=2
        elif counter==2:
            row2.append(image)
            counter=1
    print('here')
    print(len(image_storage))
    if len(image_storage)==8:
        rows=[row1[:2],row1[2:],row2[:2],row2[2:]]
    elif len(image_storage)==6:
        rows=[row1,row2]
    horizontal=[]
    for row in rows:
        horiz=np.hstack(row)
        horizontal.append(horiz)
    vertical_attachment=np.vstack(horizontal)

    return vertical_attachment

def create_collage(path,dirs):
    imgs=make_image_lst(path,dirs)
    collage=organise_images(imgs)
    cv2.imwrite("./Demo/detection_collage.jpg",collage)



if __name__ == "__main__":

    create_collage(path,dirs)
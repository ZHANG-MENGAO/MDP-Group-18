# import the opencv library
import cv2
# define a video capture object
# vid = cv2.VideoCapture('http://192.168.18.1:8000')
vid = cv2.VideoCapture('')
# vid.open('https://www.youtube.com/watch?v=CLeZyIID9Bo&ab_channel=Settle')
cv2.namedWindow('live cam', cv2.WINDOW_NORMAL)
# print(cv2.getBuildInformation())
#while (True):

    # Capture the video frame
                # by frame
ret, frame = vid.read()

print('ret: ', ret)
print('frame: ', frame)

    # Display the resulting frame
    # cv2.imshow('frame', frame)

    # the 'q' button is set as the
    # quitting button you may use any
    # desired button of your choice
# if cv2.waitKey(1) & 0xFF == ord('q'):


# After the loop release the cap object
vid.release()
# Destroy all the windows
cv2.destroyAllWindows()




# from ctypes import *  # Import libraries
# import os
# import cv2
# #import darknet
# import socket
# import ast
# import numpy as np
#
# cap = cv2.VideoCapture("http://192.168.18.1:8000")
#
# ret, frame_read = cap.read()
# print('ret: ', ret)
# print('frame_read: ', frame_read)
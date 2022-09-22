import cv2
import imagezmq
image_hub = imagezmq.ImageHub()
while True:  # show streamed images until Ctrl-C

    print('waiting to receive')
    rpi_name, image = image_hub.recv_image()
    print('received')
    cv2.imshow(rpi_name, image)  # 1 window for each RPi
    cv2.waitKey(1)
    image_hub.send_reply(b'OK')
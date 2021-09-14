import socket, threading;
import pymysql
import cvbarcode as cv
import FirebasePushAlarm as fb
import pyzbar.pyzbar as pyzbar
import cv2
import time

text = ""
msg = ""

def getBarcode():

    global text
    cap = cv2.VideoCapture('http://203.249.75.64:81/stream')

    i = 0
    while (cap.isOpened()):
        ret, img = cap.read()

        if not ret:
            continue

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        decoded = pyzbar.decode(gray)

        for d in decoded:
            x, y, w, h = d.rect

            barcode_data = d.data.decode("utf-8")
            barcode_type = d.type

            cv2.rectangle(img, (x, y), (x + w, y + h), (0, 0, 255), 2)

            text = '%s (%s)' % (barcode_data, barcode_type)
            cv2.putText(img, text, (x, y), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 255), 2, cv2.LINE_AA)
            return 0

        key = cv2.waitKey(1)
        if key == ord('q'):
            break
        elif key == ord('s'):
            i += 1
            cv2.imwrite('zbar.jpg' % i, img)

    cap.release()
    cv2.destroyAllWindows()

def barcode(bar):
    local = 'dbtest.cuslvraxrcdc.ap-northeast-2.rds.amazonaws.com'
    db = pymysql.connect(
        host=local,
        user='user',
        db='ttockshow',
        password='qwert123',
        charset='utf8'
    )
    sendD = []
    curs = db.cursor()
    curs2 = db.cursor()
    print("bar : ",bar)
    while bar == 'Start':
        print("상품을 인식 중입니다...")
        bar = cv.getBarcode()

    if cvThread.is_alive():
        fb.sendMessage("상품이 인식 완료되었습니다!","똑쇼")
        print("상품을 인식 완료하였습니다!")
    bar = bar[0:13]
    sql = """select user_id, date, contents, star_rank, cite from review where barcord_id = %s order by date desc"""
    sql2 = """select barcord_id, name, star_avg from product where barcord_id = %s """
    curs.execute(sql, [bar])
    curs2.execute(sql2,[bar])
    select = list(curs.fetchall())
    selectp = list(curs2.fetchmany(10))
    db.commit()
    for i in range(len(selectp[0])):
        sendD.append(str(selectp[0][i]))
    for i in range(len(select)):
        for j in range(len(select[i])):
            sendD.append(str(select[i][j]))
    ret = '#'.join(sendD)
    return ret

def rcv(client_socket, addr):
    global msg
    data = client_socket.recv(4)
    length = int.from_bytes(data, "little")
    data = client_socket.recv(length)
    msg = data.decode()
    print(msg)
    if len(msg) != 0:
        msg = barcode(msg)

def binder(client_socket, addr):
    global text
    global msg
    print('Connected by', addr)
    try:
        rcvThread.start()
        cvThread.start()
        while True: 
            #rcvThread.start()
            while cvThread.is_alive() and rcvThread.is_alive():
                print("바코드를 인식 중입니다.",end='',flush=True)
                time.sleep(0.4)
                print(".",end='',flush=True)
                time.sleep(0.4)
                print(".",end='',flush=True)
                time.sleep(0.4)
                print("\r",end='',flush=True)
                print(" "*26,end='',flush=True)
                print("\r",end='',flush=True)
                continue
            if not cvThread.is_alive() and rcvThread.is_alive():
                msg = barcode(text)
                print("상품을 인식 완료하였습니다!")
            data = msg.encode()

            length = len(data)
            client_socket.sendall(length.to_bytes(4, byteorder='little'))
            client_socket.sendall(data)
    except:
        print("except : " , addr)
    finally:
        print("socket close")
        client_socket.close()

if __name__ == '__main__':
    print("START")
    cvThread = threading.Thread(target=getBarcode) # args = http
    cvThread.daemon = True

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind(('', 9999))
    server_socket.listen()
    try:
        while True:
            client_socket, addr = server_socket.accept()
            th = threading.Thread(target=binder, args = (client_socket,addr))
            rcvThread = threading.Thread(target=rcv, args = (client_socket,addr))
            th.start()
    except:
        print("except")
    finally:
        print("finally")

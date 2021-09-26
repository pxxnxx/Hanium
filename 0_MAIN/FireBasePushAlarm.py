from pyfcm import FCMNotification

APIKEY = ""
TOKEN = ""
push_service = FCMNotification(APIKEY)


def sendMessage(body, title):
    data_message = {
        "body": body,
        "title": title
    }

    result = push_service.single_device_data_message(registration_id=TOKEN, data_message=data_message)

    print(result)


if __name__ == '__main__':
    sendMessage("인식완료", "똑쇼")

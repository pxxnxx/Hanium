from pyfcm import FCMNotification

APIKEY = "AAAA0brfvYU:APA91bG31YupO8cjeVl669-duqw_kjRE1XSUdLl0dS9PhPL9YDN1D164W6Hurr9JCUbWgK5Wa9WZ4luNt9UMcccP0XVlNMgvfyaiudeAQcHscCaIu7AZAuB5wZna1uUfr8mM66whRKqx"
TOKEN = "duVCxvvKRHetPGmc-qO-ti:APA91bGX4POqrviJtSyK8yrtsKeam7tMWvsbd8f_sQjXIHxJYTD8xwF8jjnRDeXRvJoICVO6w72TVW2ZZDJ-rYsyLvu-agWx3kPddsJ-8ND0LArg16h7QvOf9s83ur_oqiCsvX2SlCbx"
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

from konlpy.tag import Okt
import sys
from collections import Counter
from wordcloud import WordCloud
import pymysql as db
import matplotlib.pyplot as plt

local = 'dbtest.cuslvraxrcdc.ap-northeast-2.rds.amazonaws.com'
con = db.connect(
    host=local,
    user='user',
    db='ttockshow',
    password='qwert123',
    charset='utf8'
)

cur_tm=con.cursor()

def insert_db(link,barcode):
    #UPDATE 테이블명 SET 컬럼1 = 수정값1 [, 컬럼2 = 수정값2 ...] [WHERE 조건];
    sql_miningLink="""UPDATE product SET mining=%s WHERE barcord_id=%s"""
    link=link.replace("https://drive.google.com/file/d/","https://drive.google.com/uc?id=")
    link=link.replace("/view?usp=sharing","")
    cur_tm.execute(sql_miningLink,(link,barcode))


def tts_db(ttsStr,barcode):
    sql_tts="""UPDATE product SET tts=%s WHERE barcord_id = %s """
    cur_tm.execute(sql_tts,(ttsStr,barcode))


def fetchReview(codeNum):
    sel_rev = """select contents from review where barcord_id = %s"""
    cur_tm.execute(sel_rev, [codeNum])
    rev = list(cur_tm.fetchall())
    return rev

def get_noun(news):
    okt = Okt()
    noun = okt.nouns(news)
    print("1")
    for i,v in enumerate(noun):
        if len(v) < 10:
            noun.pop(i)

    print("2")
    count = Counter(noun)

    print("3")
    noun_list = count.most_common(100)

    #for word in noun_list:
     #   if word[1] in ['Noun', 'Verb', 'Adjective']:  # 명사, 동사, 형용사
      #      print(word[0])

    print(noun_list)
    return noun_list

def visualize(noun_list):
    wc = WordCloud(font_path='./namsan.ttf', background_color="white",width=1000,height=1000,max_words=100,max_font_size=300)
    #wc.generate_from_frequencies(dict(noun_list[0]))
    #wc.to_file('keyword.png')

def excuteMining(maxLen,barcode):
    review_list = [x[0] for x in fetchReview(barcode)]
    print("fetch done.")
    # print(review_list)
    strReview = " ".join(review_list)
    print("string done.")
    # print(strReview)
    if len(strReview) > maxLen:
        strReview = strReview[0:maxLen]
    #print(strReview)
    res = get_noun(strReview)
    visualize(res)
    # newVisualize(get_noun(strReview))
    print("caculating is done:)")
    #res.sort(key=lambda x : x[1],reverse=True)
    print(res)




if __name__=="__main__":
    # sql="""select barcord_id from review group by barcord_id"""
    """
    cur_tm.execute(sql)
    result=cur_tm.fetchall()
    #cnt=0
    num=int(input("NUM : "))
    cnt=num
    while(cnt<len(result)):
        pro=result[cnt]
        print(pro[0])
        excuteMining(100000,pro[0])  # #리뷰내용최대길이(전체리뷰합친길이), 바코드번호
        cnt+=1
    con.commit()
    cur_tm.close()
    """
    #excuteMining(1000000,"8801007160337")
    excuteMining(1000000, "5410126116953")
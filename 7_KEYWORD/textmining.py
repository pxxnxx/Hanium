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

cur=con.cursor()

def fetchReview(codeNum):
    police = """select contents from review where barcord_id = %s"""
    cur.execute(police, [codeNum])
    dontknow = list(cur.fetchall())
    return dontknow

def get_noun(news):
    okt = Okt()
    noun = okt.phrases(news)
    print("1")
    for i,v in enumerate(noun):
        if len(v) < 10:
            noun.pop(i)

    print("2")
    count = Counter(noun)

    print("3")
    noun_list = count.most_common(100)

    return noun_list

def visualize(noun_list):
    wc = WordCloud(font_path='./namsan.ttf', background_color="white",width=1000,height=1000,max_words=100,max_font_size=300)
    wc.generate_from_frequencies(dict(noun_list))
    wc.to_file('keyword.png')

if __name__=="__main__":
    review_list = [x[0] for x in fetchReview(str(8801007160337))]
    print("fetch done.")
    #print(review_list)
    strReview = " ".join(review_list)
    print("string done.")
    #print(strReview)
    inputlist = strReview[-100000:-1]
    print(inputlist)
    visualize(get_noun(inputlist))
    #newVisualize(get_noun(strReview))
    print("visualization done. :)")
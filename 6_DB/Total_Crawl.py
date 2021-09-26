import TM_SSG_Crawl_DB_Save as ssg
import TM_LotteOn_Crawl_DB_Save as lotte
import pymysql as db

local = 'dbtest.cuslvraxrcdc.ap-northeast-2.rds.amazonaws.com'
con = db.connect(
    host=local,
    user='user',
    db='ttockshow',
    password='qwert123',
    charset='utf8'
)

cur=con.cursor()

#전체 프로덕트 돌림
sql_find = "SELECT barcord_id, name ,ssg , lotte FROM product ORDER BY star_avg "

#리뷰테이블에 들어있는 바코드만 돌림
# sql_find = """select review.barcord_id ,product.name ,product.ssg , product.lotte ,count(review.barcord_id) as 'count'
# from product inner join review
# on product.barcord_id = review.barcord_id
# group by review.barcord_id """

cur.execute(sql_find)
result = cur.fetchall()
print(result)

CalAvg="""select round(avg(star_rank),2),barcord_id from review
    where barcord_id =%s"""
ApAvg="""UPDATE product SET star_avg=%s WHERE barcord_id =%s"""

def average(pro):
    cur.execute(CalAvg, pro[0])
    result_avg = cur.fetchone()
    print(result_avg)
    cur.execute(ApAvg, result_avg)
    con.commit()

DelPro="""DELETE FROM product WHERE barcord_id = %s """

# 0번부터 시작
num=int(input("몇 번?"))
cnt = num
while cnt < len(result):
    pro = result[cnt]
    print(pro[1])
    if pro[1]=="8801007160337" or pro[1]=="8801007377926" : continue
    SSG = ssg.crawl(pro, cur)
    con.commit()
    LOTTE = lotte.crawl(pro, cur)
    con.commit()
    cnt+=1
    if "없습니다" in SSG and LOTTE:
        cur.execute(DelPro,pro[0])
        con.commit()
        continue
    average(pro)
    print(cnt)

# AvgAll
# CalAvgAll="""select round(avg(star_rank),2), barcord_id  from review
#     group by barcord_id"""
# cur.execute(CalAvgAll)
# result=cur.fetchall()
# print(result)
# ApAvgAll="""UPDATE product SET star_avg=%s WHERE barcord_id =%s"""
# cur.executemany(ApAvg,result)
# con.commit()

cur.close()
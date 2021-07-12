import time
import urllib.request 
import math
import pandas as pd
from bs4 import BeautifulSoup
from pandas import DataFrame
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import os


#크롬드라이버 연결
chrome_driver=os.path.join('chromedriver')
chrome_options = webdriver.ChromeOptions()
driver = webdriver.Chrome(chrome_driver, options=chrome_options)
data_list = []

plusUrl = urllib.parse.quote_plus(input('검색어를 입력하시오 : '))
url = f'http://emart.ssg.com/search.ssg?target=all&query={plusUrl}&src_area=recom'
driver.get(url)

driver.find_element_by_css_selector('.thmb').click()
time.sleep(2)

review_total = driver.find_element_by_css_selector('.num').text 
review_total = review_total.replace(",","")
print("리뷰 개수:",review_total)

#페이지별 리뷰 개수
review_per_page = 10 
total_page = int(review_total) / review_per_page 
total_page = math.ceil(total_page) 
print("리뷰 페이지 수:", total_page) 

# 상품명 확인 
product = driver.find_element_by_css_selector('.cdtl_info_tit').text 
print("상품명:",product) 
review_grade = driver.find_element_by_css_selector('.cdtl_grade_total').text
print("평점:", review_grade)


def get_page_data(): 
    users = driver.find_elements_by_css_selector('.user.in') # 사용자명 수집 
    ratings = driver.find_elements_by_css_selector('.sp_cdtl.cdtl_cmt_per') # 평점 수집 
    review = driver.find_elements_by_css_selector('.desc_txt') #리뷰 수집
    # 사용자명수와 평점수가 같을 경우만 수집 
    if len(users) == len(ratings): 
        for index in range(len(users)): 
            data = {} 
            data['username'] = users[index].text 
            data['rating'] = int(ratings[index].get_attribute('style')) / 20
            data['review'] = review[index].text
            print(data) 
            data_list.append(data) 

print("수집 시작") # 첫 페이지 수집하고 시작 

get_page_data() # 버튼을 눌러서 페이지를 이동해 가면서 계속 수집. # 예외처리를 해줘야 함. 하지 않으면 중지됨. 

for page in range(1, total_page): 
    try: 
        print(str(page) + " page 수집 끝") 
        if(button_index % 10 == 1):
            continue
        button_index = page # 데이터 수집이 끝난 뒤 다음 페이지 버튼을 클릭 
        print("한태희 개새끼1")
        driver.find_element_by_xpath(f"//*[@id='comment_navi_area']/a[{button_index}]").click() 
        print("한태희 개새끼2")
        time.sleep(5) #1 0page 수집이 끝나서 11로 넘어가기 위해서는 > 버튼을 눌러야 함. 
        if(page % 10 == 0): 
            driver.find_element_by_css_selector('.btn_next').click() 
            time.sleep(5) # 해당 페이지 데이터 수집
         
        get_page_data()
    except: 
        print("수집 에러") 
print(str(page) + " page 수집 끝") 
print("수집 종료") 

df = pd.DataFrame(data_list) 
print(df) # 엑셀로 저장 
df.to_excel("ssg-crawling-example.xlsx")

//*[@id="comment_navi_area"]/a[1]
//*[@id="comment_navi_area"]/a[2]
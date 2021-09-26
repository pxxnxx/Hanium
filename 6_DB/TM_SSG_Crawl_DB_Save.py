import time
import urllib.request
import urllib.parse
import math
from selenium import webdriver
# 크롬드라이버 연결

options = webdriver.ChromeOptions()
options.add_experimental_option("excludeSwitches", ["enable_logging"])
driver = webdriver.Chrome(options=options)
data_list = []


def crawl(pro,cur):

    product = pro[1]
    plusUrl = urllib.parse.quote_plus(product)
    url = f'http://www.ssg.com/search.ssg?target=all&query={plusUrl}'
    driver.get(url)

    count = pro[2] # 여기에 Count 넣어야해
    
    a = driver.find_element_by_css_selector('.csrch_top.v2').text

    if "상품이 없습니다." in a:
        print("해당 상품 없음")
        return a
        # driver.quit()
        # sys.exit()
    driver.find_element_by_css_selector('.thmb').click()
    time.sleep(1)

    # 상품명 확인 
    product = driver.find_element_by_css_selector('.cdtl_info_tit').text 
    print("상품명:",product)
    
    try: #리뷰 없을 때
        review_none = driver.find_element_by_css_selector('.cdtl_review_txt').text 
        print(review_none)
        return review_none
        
    except Exception: #리뷰 있을 때
        price = driver.find_element_by_css_selector('.ssg_price').text 
        price = price.replace(",","")
        print("가격:",price)
    
    try: #리뷰 게시가 안되어있을때
        table = driver.find_element_by_class_name('cdtl_cmt_tbl')
        nodata = table.find_element_by_tag_name('p').text
        print(nodata)
        return nodata
    except Exception:
        # 최신순 클릭
        driver.find_element_by_xpath('//*[@id="cmt_select_sort"]/div/a').click()
        driver.find_element_by_xpath('//*[@id="cmt_select_sort"]/div/div/ul/li[2]').click()
    
    review_total = driver.find_element_by_css_selector('.num').text
    review_total = int(review_total.replace(",",""))
    if review_total > 1000 :
        print("리뷰가 1000개 이상이기 때문에 건너뜀")
        return str(review_total)
    review_grade = driver.find_element_by_css_selector('.cdtl_grade_total').text
    print("평점:", review_grade)
    print("리뷰 개수:",review_total)
    print("기존 리뷰 개수", count)
    print("필요한 리뷰 개수 :", review_total-count)

    #페이지별 리뷰 개수
    review_per_page = 10
    total_page = (review_total-count) / review_per_page 
    total_page = math.ceil(total_page) 
    print("긁어올때 필요한 페이지 수:", total_page) 

    def get_page_data():
        users = driver.find_elements_by_css_selector('.user')  # 사용자명 수집
        ratings = driver.find_elements_by_css_selector('.sp_cdtl.cdtl_cmt_per')  # 평점 수집
        reviews = driver.find_elements_by_css_selector('.cdtl_cmt_tx.v2')  # 리뷰 수집
        for i in range(len(reviews)):
            if len(data_list) == review_total - count:
                break
            if users[1].text == '':
                user = users[i+2].text
            else:
                user = users[i+1].text
            rating = ratings[i].text
            rating = rating.replace("구매 고객 평점 별 5개 중 ", "")
            rating = rating.replace("개", "")
            rating = int(rating)
            review = reviews[i].text
            review = review.replace("사진\n", "")
            review = review.replace("비디오\n", "")
            num = (2 * i + 1) % 20
            date = driver.find_element_by_xpath(f'//*[@id="cdtl_cmt_tbody"]/tr[{num}]/td[5]/div')
            date = date.text.replace("-", "")
            data_tu = (pro[0], user, date, rating, review, "ssg")
            data_list.append(data_tu)
            #print(data_tu)
            print("\r {0}".format(data_tu), end="")

    print("수집 시작") # 첫 페이지 수집하고 시작 
    get_page_data()
    for page in range(0, total_page):
        try:
            if page == 0:
                print("1 page 수집 끝")
                driver.find_element_by_xpath('//*[@id="comment_navi_area"]/a[1]').click()
                time.sleep(1)
                get_page_data()
            elif page > 0 and page < 10:
                print(str(page+1) + " page 수집 끝")
                button_index = page + 2  # 데이터 수집이 끝난 뒤 다음 페이지 버튼을 클릭
                driver.find_element_by_xpath(f'//*[@id="comment_navi_area"]/a[{button_index}]').click()
                time.sleep(1)
                get_page_data()
            else:
                print(str(page + 1) + " page 수집 끝")
                button_index = page % 10 + 3  # 데이터 수집이 끝난 뒤 다음 페이지 버튼을 클릭
                driver.find_element_by_xpath(f'//*[@id="comment_navi_area"]/a[{button_index}]').click()
                time.sleep(1)
                get_page_data()
        except Exception as e:
            print(e)
    print("수집 종료")

    sql = "INSERT IGNORE INTO review (barcord_id,user_id,date, star_rank,contents,cite) VALUES (%s,%s,%s,%s,%s,%s)"
    cur.executemany(sql, data_list)
    query_ssg = """UPDATE product SET ssg=%s WHERE barcord_id=%s """
    cur.execute(query_ssg, (review_total, pro[0]))
    return a


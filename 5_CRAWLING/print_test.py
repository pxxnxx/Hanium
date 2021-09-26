import time
import urllib.request
import urllib.parse
import math
from selenium import webdriver
import pandas as pd
import sys

#크롬드라이버 연결
options = webdriver.ChromeOptions()
options.add_experimental_option("excludeSwitches", ["enable_logging"])
driver = webdriver.Chrome(options=options)
data_list = []
 
product = "CJ제일제당 전립소 쏘팔메토"
url = "https://store.steampowered.com/"
driver.get(url)
driver.find_element_by_css_selector('.thmb').click()

table = driver.find_element_by_class_name('cdtl_cmt_tbl')
nodata = table.find_element_by_tag_name('p').text
print(nodata)

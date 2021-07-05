from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.chrome.option import Options
import time

option = Options()
option.add_argument('headless')
option.add_argument('--window-size=1920,1080')
browser=webdriver.Chrome(options=option)

#path=r'\Users\han\tmpfolder'
browser.get('http://finance.naver.com')
time.sleep(5)
screenshot=browser.save_screenshot('screenshot.png')
browser.quit

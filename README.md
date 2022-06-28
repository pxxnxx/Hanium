
# 똑쇼 프로젝트

* 프로젝트명 : 스마트 안경을 이용한 똑쇼 (똑똑한 쇼핑)  
* 팀명 : 임의의팀  
* 팀 멘토 : 한주연 멘토  
* 팀 멘티 : 한태희(팀장), 김민석, 김지석, 전수민  

# 똑쇼 프로젝트 핵심코드 모음
*  Aduino_Streaming.ino
*  cvbarcode.py
*  driveuplode.py
*  textmining.py
*  TM_LotteOn_Crawl_DB_Save.py
*  TM_SSG_Crawl_DB_Save.py
*  tmp.png
*  Total_Crawl.py


# 코드 간단설명

### Aduino_Streaming.ino
아두이노 보드인 ESP32를 전반적으로 컨트롤하는 ino 파일입니다. ESP-32에 현재 탑재되어 있으며 변경된 프로그램을 업로드하여 작동할 수 있습니다.  
보드가 부팅되면 AutoConneted AP가 활성화 되며, 타 기기를 활용하여 ESP-32를 특정 AP에 접속시킬 수 있습니다.
### cvbarcode.py
아두이노에서 스트리밍되는 영상을 실시간으로 확인하여 화면 내에 존재하는 1D Barcode를 인식한다.  
스트리밍 되는 영상을 관리자 이외의 다른 사용자가 접근하지 않도록 서버에서 처리한다.
### driveuplode.py
바코드 번호를 통해 DB에서 상품 리뷰를 꺼내와 textmining을 진행하고 firebase를 통해 앱으로 전달한다.
### textmining.py
DB에 존재하는 모든 상품들의 리뷰를 텍스트마이닝을 통해 도식화하여 저장한다.  
서버에서 별도로 실행되도록 설계되었다.  
### TM_LotteOn_Crawl_DB_Save.py
LotteOn의 웹구조에 맞춰서 크롤링하기 위한 함수
### TM_SSG_Crawl_DB_Save.py
SSG의 웹구조에 맞춰서 크롤링하기 위한 함수
### tmp.png
텍스트마이닝 완료된 이미지가 업로드 되기 전에 임시로 저장되는 파일. 
### Total_Crawl.py
LotteOn과 SSG의 쇼핑몰에서 리뷰를 크롤링한다.  
서버에서 별도로 실행되도록 설계되었다.

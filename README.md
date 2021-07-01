<h1>URL 입력받아 HTML 페이지의 문자열을 처리하는 프로그램</h1>


<h2>Spring Boot 프로젝트</h2>


<h3>1. 비즈니스 프로세스</h3>
  ① 화면에서 URL을 입력 받는다.(localhost:8080)
  <br>
  ② 해당 URL을 호출하여 HTML 페이지 정보를 읽습니다.
  <br>
  ③ Type 값이 HTML 태그가 제외인 경우 문자열에서 HTML 태그를 제거 합니다.
  <br>
  ④ 문자열 중 영어와 숫자만을 찾아 나눠서 오름차순 정렬 합니다.
  <br>
  ⑤ 영어와 숫자를 한번씩 섞어 새로운 문자열을 만든다.
  <br>
  ⑥ 출력 묶음 단위로 문자열의 길이를 나눠 몫에 해당하는 문자열과 나머지에 해당하는 문자열을 화면에 표시 합니다.

<h3>2. 환경</h3>
  ① JAVA8
  <br>
  ② Spring Boot 2.5.2
  <br>
  ③ Maven Project

<h3>3.실행 방법</h3>
  ① 프로젝트를 임포트 합니다.
  <br>
  ② urlDivisionApplication을 실행 합니다.
  <br>
  ③ localhost:8080 접속 합니다.

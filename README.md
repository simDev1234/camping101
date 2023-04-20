# camping101

## **요약**

- 누구와 함께, 또는 혼자 여행을 떠나고 싶은 사람들을 위한 캠핑 예약 사이트를 만들었습니다.
- 캠핑장 사장님은
    - 사장님 사이트에서 캠핑장을 등록하고, 예약 현황을 관리할 수 있습니다.
- 여행을 떠나려는 고객은
    - 캠핑 101 사이트에서 캠핑장 정보를 조회하고 예약을 할 수 있습니다.
    - 또한 방문했던 캠핑장에 캠프 로그를 남기고, 다른 사람들로부터 댓글을 받을 수 있습니다.
    - 인상 깊게 보았던 캠프 로그는 북마크로 등록하여 언제든지 다시 꺼내 볼 수 있습니다.
- 관리자는
    - 관리 사이트에서 회원과 캠프로그, 캠프로그에 달리는 추천태그를 관리할 수 있습니다.

## **역할**

- Github Conventional commit, PR 리뷰, Markdown 언어로 TroubleShooting 기록
- Spring Security와 JWT Access Token/Refresh Token, Google OAuth로 사용자 로그인 인증 구현
- 비밀번호 잊을 시 제한 시간(5분)이 있는 Temporal Password를 메일로 발급해 로그인하는 로직 구현
- RDBMS로 MariaDB(실행 환경), H2(개발환경) 사용, NoSQL로 Redis 사용
- Redis에 Data Crud시 RedisClient(RedisTemplate 포함) 또는 RedisRepository 사용
- S3FileUploader, CustomMailSender, RandomCode, FilterResponseHandler 등의 유틸 클래스 작성
- AWS EC2(ubuntu), S3, RDS 사용 및 Swagger, Logback 사용

## **아키텍처**
![camping101 (1)](https://user-images.githubusercontent.com/107039546/233258746-0626705e-e72a-410b-8893-d47cd32d476b.jpg)

## ERD
![camping101-erd drawio](https://user-images.githubusercontent.com/107039546/233258713-3006d0b0-2605-41e5-88ef-426b8c0e3841.png)

## 화면

- 일반 사용자 사이트
- 사장님 사이트
- 관리 사이트
![2023-04-19_11 17 52](https://user-images.githubusercontent.com/107039546/233258782-4b54ba4e-a23b-41ad-a33f-88bdf82365bf.png)
![2023-04-19_11 18 57](https://user-images.githubusercontent.com/107039546/233258832-cb0bb60e-8aaa-4c2f-bdcc-97c707f4e90c.png)
![2023-04-19_11 18 14](https://user-images.githubusercontent.com/107039546/233258826-91025999-cea3-4e51-a2a2-0ca3afdcae85.png)
![2023-04-19_11 23 36](https://user-images.githubusercontent.com/107039546/233258816-6327cd31-cc36-4b27-9ac6-cc225bbc3ab5.png)




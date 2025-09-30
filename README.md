# NBE7-9-1-Team04
> 데브코스 백엔드 7기 9회차 4팀 A3O2 1차 프로젝트
[자세한 정보를 원한다면 위키로](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki)

## 📋 목차
- [프로젝트 소개](#프로젝트-소개)
- [주요 기능](#주요-기능)
- [팀원 및 역할](#팀원-및-역할)
- [협업 방식](#협업-방식)
- [시작하기](#시작하기)
- [시연 영상](#시연-영상)
- [기술 스택](#기술-스택)

<hr />

## 프로젝트 소개
> 우리는 작은 로컬 카페 'Grids & Circles' 입니다. <br />
> 고객들은 온라인 웹사이트를 통해 커피 원두 패키지를 주문합니다.  <br />
> 우리는 매일 전날 오후 2시부터 당일 오후 2시까지의 주문을 모아서 처리합니다. <br />

<br />

## 주요 기능
- **회원 관리**: 회원가입, 로그인, 주소 관리
- **메뉴 관리**: 메뉴 조회, 등록, 수정, 삭제
- **장바구니**: 장바구니 추가, 수정, 삭제, 조회
- **주문 처리**: 주문 생성, 조회, 상태 관리
- **결제**: 결제 처리 및 내역 관리
- **관리자**: 관리자 전용 관리 기능

<br />

## ERD
<img width="1530" height="1042" alt="A3O2 (9)" src="https://github.com/user-attachments/assets/1031ed4a-1835-4125-ad9c-8d619a93c915" />

<br />

## 팀원 및 역할

### 기능별 담당 영역

| 이름 | 역할 | 담당 영역 | GitHub | Wiki 문서 |
|------|------|-----------|--------|--------|
| **강휘윤** | Backend | 회원, 주소 도메인 | [Github](https://github.com/Creamcheesepie) | [회원 도메인 위키 문서](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki/%ED%9A%8C%EC%9B%90-%EB%8F%84%EB%A9%94%EC%9D%B8) |
| **박종수** | Backend & Frontend | 주문 도메인, FE 공통 영역 | [Github](https://github.com/gandaraipower) | [주소 도메인 위키 문서](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki/%EC%A3%BC%EC%86%8C-%EB%8F%84%EB%A9%94%EC%9D%B8) |
| **오혜승** | Backend & PM | 결제 도메인, 일정 관리 및 기획 | [Github](https://github.com/Hyeseung-OH) | [주문 도메인 위키 문서](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki/%EC%A3%BC%EB%AC%B8-%EB%8F%84%EB%A9%94%EC%9D%B8) |
| **윤예지** | Backend | 장바구니 도메인, BE 공통 영역 | [Github](https://github.com/dpwl0974) | [결제 도메인 위키 문서](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki/%EA%B2%B0%EC%A0%9C-%EB%8F%84%EB%A9%94%EC%9D%B8) |
| **최병준** | Backend | 메뉴 도메인, 관리자 도메인 | [Github](https://github.com/larama-C) | [메뉴 도메인 위키 문서](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki/%EB%A9%94%EB%89%B4-%EB%8F%84%EB%A9%94%EC%9D%B8) |

<br />

## 협업 방식
[협업 방식(커밋 컨벤션, 코딩 컨벤션 등)과 관련된 내용은 위키 페이지에서 확인 가능합니다](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/wiki)
<hr />

## 시작하기
이 프로젝트를 실행하기 위해서는 아래와 같은 사항들이 필요합니다.
- **Java**: Java 21 (LTS)
- **Node.js**: 20.x LTS (권장)
- **Database**: MySQL 8.0 이상 (운영 환경)

 ### 설치 및 실행

#### 1. 저장소 클론
```bash
git clone https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04.git
cd NBE7-9-1-Team04
```

#### 2. 데이터베이스 설정
MySQL에 데이터베이스를 생성합니다:
```sql
CREATE DATABASE [데이터베이스명] CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

#### 3. 백엔드 환경 변수 설정
프로젝트 루트 디렉토리에 `.env` 파일을 생성합니다:
```properties
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/[데이터베이스명]
DB_USERNAME=[사용자명]
DB_PASSWORD=[비밀번호]
```

#### 4. Lombok 설정 (IntelliJ IDEA)
1. **플러그인 설치**
   - `File` → `Settings` (Windows/Linux) 또는 `Preferences` (Mac)
   - `Plugins` → `Marketplace` 탭 선택
   - "Lombok" 검색 후 설치
   - IDE 재시작

2. **Annotation Processing 활성화**
   - `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
   - "Enable annotation processing" 체크
   - `Apply` → `OK`

#### 필요한 의존성은 이미 gradle.build에 들어 있으므로 필요하다면 빌드를 해 주시기 바랍니다.

<hr />

## 시연 영상
[![Video Label](http://img.youtube.com/vi/NGMVwdaf0Bc/0.jpg)](https://youtu.be/NGMVwdaf0Bc)
<br/>

<hr />

## 기술 스택

### Backend
<div>
<img src="https://img.shields.io/badge/Java%2021-007396?style=for-the-badge&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Boot%203.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white">
</div>

### Frontend
<div>
<img src="https://img.shields.io/badge/Next.js%2015.5.3-000000?style=for-the-badge&logo=next.js&logoColor=white">
<img src="https://img.shields.io/badge/React%2019.1.0-61DAFB?style=for-the-badge&logo=react&logoColor=black">
<img src="https://img.shields.io/badge/TypeScript%205.9.2-3178C6?style=for-the-badge&logo=typescript&logoColor=white">
<img src="https://img.shields.io/badge/Tailwind%20CSS%204.1.13-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white">
<img src="https://img.shields.io/badge/ESLint%209.36.0-4B32C3?style=for-the-badge&logo=eslint&logoColor=white">
</div>

### Database
<div>
<img src="https://img.shields.io/badge/MySQL%208.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/H2%20Database-0000BB?style=for-the-badge&logo=h2&logoColor=white">
</div>

### Development Tools
<div>
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
<img src="https://img.shields.io/badge/Cursor-000000?style=for-the-badge&logo=cursor&logoColor=white">
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/Zoom-2D8CFF?style=for-the-badge&logo=zoom&logoColor=white">
</div>

<hr />

### 문의사항
프로젝트에 대한 문의사항이나 버그 리포트는 [Issues](https://github.com/prgrms-be-devcourse/NBE7-9-1-Team04/issues)에 등록해 주세요.

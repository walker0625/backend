# Canofy MD: AI 기반 고혈압 및 피부질환 진단 보조 플랫폼

## 1. 프로젝트 소개 (Introduction)
**Canofy MD**는 환자의 생체 데이터와 의료 이미지를 분석하여 의료진의 진단을 보조하는 디지털 헬스케어 플랫폼의 백엔드 시스템입니다.
Spring Boot 기반의 멀티 모듈 아키텍처로 설계되었으며, 이기종 AI 모델(혈압 예측, 피부암 진단)과의 안정적인 데이터 파이프라인을 구축하여 실시간에 가까운 분석 결과를 제공합니다.

### 🎯 핵심 목표
- **AI 모델 통합 서빙:** 서로 다른 입출력 명세를 가진 AI 모델(Tabular Data, Vision)을 표준화된 인터페이스로 통합
- **데이터 무결성 보장:** 의료 데이터(혈압, 이미지)의 전처리 및 진단 이력의 정합성 관리
- **안정적인 확장성:** 도메인(Core)과 API 서비스(Api) 계층 분리를 통한 유지보수성 확보

---

## 2. 기술 스택 (Tech Stack)

| Category | Technology |
| --- | --- |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.0.2 |
| **Database** | MySQL, Redis |
| **ORM / Query** | Spring Data JPA, QueryDSL |
| **HTTP Client** | Spring WebClient (External AI Server Comm) |
| **Security** | Spring Security, JWT |
| **Build Tool** | Gradle (Multi-module) |

---

## 3. 시스템 아키텍처 (System Architecture)

본 프로젝트는 **멀티 모듈(Multi-Module)** 구조로 구성되어 있으며, 외부 AI 추론 서버와 REST 통신을 수행합니다.

### 📦 모듈 구조
* **module-core**: 도메인 엔티티(Entity), 레포지토리(Repository), 공통 유틸리티(Util), 설정(Config) 등 비즈니스 로직의 핵심 자원을 관리합니다.
* **module-api**: 클라이언트 요청을 처리하는 컨트롤러(Controller), AI 서버 연동 서비스(Service), 보안 필터(Security Filter) 등을 포함합니다.

### 📡 AI 연동 아키텍처
API 서버는 클라이언트와 AI 모델 사이의 **Gateway** 역할을 수행합니다.

## 4. 핵심 기능 (Key Features)

### 🩸 1. 혈압 예측 서비스 (Blood Pressure AI)
환자의 과거 혈압 기록을 시계열 데이터로 변환하여 4주 후의 혈압 추이를 예측합니다.

* **데이터 전처리 (`PredictionService.java`):**
    * `BpHistoryService`를 통해 환자의 유효한 측정 이력을 조회
    * AI 모델 입력 형태에 맞춰 수축기(SBP), 이완기(DBP), 맥박(Pulse) 데이터를 리스트 형태로 정규화
* **모델 연동:** `WebClient`를 사용하여 JSON 포맷으로 예측 서버에 요청 전송
* **결과 저장:** 주차별(1~4주) 예측 데이터를 매핑하여 DB에 이력 저장

### 📸 2. 피부암 진단 서비스 (Skin Cancer AI)
환부 이미지를 업로드하여 피부암 발병 확률을 분석합니다.

* **이미지 처리 (`DiagnoseService.java`):**
    * 업로드된 이미지를 썸네일과 원본으로 분리하여 파일 시스템에 저장
    * `MultipartBodyBuilder`를 활용하여 이미지를 바이너리 스트림으로 변환 후 AI 서버 전송
* **진단 분석:**
    * AI가 반환한 확률 리스트를 `BigDecimal`을 사용하여 정밀 계산 (소수점 3자리)
    * 암/비암 확률 도출 및 진단 결과 생성
* **이력 관리:** 동일 병변에 대한 진단 그룹(`GroupSeq`) 기능을 통해 병변 변화 추적 가능

### 🔐 3. 보안 및 인증 (Security & Auth)
* **JWT 인증:** Access/Refresh Token 기반의 인증 시스템 구축
* **암호화:** `CustomPasswordEncoder`를 통한 개인정보 암호화 처리
* **권한 관리:** 환자(Patient), 의료진(Staff) 등 사용자 타입별 API 접근 제어

---

## 5. 프로젝트 구조 (Project Structure)

```bash
backend
├── module-api               # API 서버 모듈
│   ├── src/main/java/kr/lifesemantics/canofymd/moduleapi
│   │   ├── domain           # 도메인별 비즈니스 로직 (bp, sc, patient 등)
│   │   ├── global           # 전역 설정 (config, util, exception)
│   │   ├── webClient        # 외부 AI 서버 통신 모듈
│   │   └── ...
│   └── ...
├── module-core              # 공통 코어 모듈
│   ├── src/main/java/kr/lifesemantics/canofymd/modulecore
│   │   ├── domain           # Entity, Repository 정의
│   │   ├── enums            # 공통 Enum 클래스
│   │   └── ...
│   └── ...
└── build.gradle             # 루트 빌드 설정

# Burger Builder: Console Burger Shop Simulator 🍔
## 햄버거 가게 주문 및 조리 시뮬레이터 (Java 콘솔 미니 프로젝트)

## 1) 프로젝트 개요
이 프로젝트는 **Java 콘솔(터미널) 기반**으로 동작하는 햄버거 주문 및 조리 시뮬레이터입니다.  
사용자는 미리 준비된 **기본 버거 메뉴를 주문**할 수 있고, 원하는 재료를 조합해 **커스텀 버거를 직접 구성**할 수도 있습니다.

> 본 프로젝트는 GUI/웹/모바일 앱이 아니라, `burger.Main`을 실행해 **콘솔에서 메뉴 번호를 입력하며 사용하는 프로그램**입니다.

---

## 2) 주요 기능
아래 기능은 현재 코드 기준으로 구현된 내용입니다.

| 구분 | 기능 | 설명 |
|---|---|---|
| 메뉴 | 전체 메뉴 조회 | 기본 메뉴 목록과 가격을 확인하고, 가격 오름차순 정렬 결과도 확인 |
| 주문 | 기본 버거 주문 | 메뉴 번호와 수량을 입력해 주문 생성 |
| 주문 | 커스텀 버거 주문 | 재료를 단계적으로 선택해 1개 커스텀 버거 주문 |
| 주문 | 주문 내역 조회 | 누적 주문 목록(주문번호, 고객명, 주문내용, 총액, 시간) 출력 |
| 재고 | 재고 확인 | 재료별 현재 재고 수량 출력 |
| 가격 | 가격 계산 | `PricingPolicy` 기반으로 주문 항목 금액/총액 계산 |
| 통계 | 매출 통계 | 전체 주문 기준 총 주문 수, 총 매출 계산 |
| 통계 | 인기 메뉴 통계 | 메뉴별 판매 수량 집계 후 인기 순 출력 |
| 조리 | Queue 기반 멀티스레드 처리 | `BlockingQueue`에 주문 적재, 별도 조리 스레드가 순차 처리 |
| 종료 | 프로그램 종료 | 종료 시 조리 스레드 안전 종료 |

---

## 3) 커스텀 버거 기능
커스텀 버거는 Builder 방식으로 재료를 선택합니다.

### 선택 가능한 항목
- **패티 종류**: 소고기 / 치킨
- **패티 개수**
- **치즈 개수**
- **양상추 추가 여부**
- **토마토 추가 여부**
- **피클 추가 여부**
- **양파 종류**: 없음 / 생양파 / 구운양파
- **베이컨 추가 여부**
- **소스 선택**: 케첩 / 마요 / 머스타드 / 스파이시 소스 (공백으로 복수 선택 가능)

### 가격 방식
- 커스텀 버거는 **기본 가격(2000원)** + **선택 재료 가격 합계**로 계산됩니다.
- 따라서 사용자가 어떤 재료를 얼마나 선택했는지에 따라 최종 가격이 달라집니다.

---

## 4) 사용한 주요 Java 개념

### 4-1. 객체지향 설계
- `model`, `service`, `factory`, `policy`, `util` 패키지로 책임을 분리했습니다.
- 도메인 객체(`Burger`, `CustomBurger`, `Ingredient`, `Order`, `OrderItem`)를 중심으로 데이터를 관리합니다.
- `MenuItem` 인터페이스를 공통 타입으로 사용해 기본 버거와 커스텀 버거를 같은 방식으로 다룹니다.
- `CustomBurger`는 `Burger`를 상속해 공통 속성/동작을 재사용합니다.

### 4-2. 디자인 패턴
- **Builder Pattern**: `CustomBurger.Builder`로 커스텀 재료를 단계적으로 조합
- **Factory Pattern**: `MenuFactory`가 기본 메뉴 객체 생성을 담당
- **Strategy Pattern**: `PricingPolicy` 인터페이스 + `BasicPricingPolicy` 구현으로 가격 정책 분리

### 4-3. 제네릭 / 컬렉션
- **List**: 메뉴 목록, 주문 내역, 주문 항목, 재료 목록
- **Map**: 재고(`stockMap`) 관리, 인기 메뉴 집계(`menuCountMap`)
- **Queue**: 조리 완료/시작 알림 저장(`notificationQueue`)
- **BlockingQueue**: 조리 대기 주문 큐(`cookingQueue`)

### 4-4. 람다 / Stream
- 메뉴를 가격순으로 정렬 출력
- 전체 주문의 총매출 합계 계산
- 메뉴별 판매량 집계 및 인기 순 정렬
- 재료 목록을 묶어서 설명 문자열 생성
- 커스텀 버거의 재료 금액 합계 계산

### 4-5. 멀티스레드 기초
- `CookingService`에서 별도 조리 스레드를 실행합니다.
- 주문은 `BlockingQueue`에 저장되고, 조리 스레드가 순서대로 꺼내 처리합니다.
- `Thread.sleep(3000)`으로 조리 시간을 단순 표현합니다.
- `notificationQueue`에 조리 시작/완료 메시지를 적재합니다.
- `ConsolePrinter`의 `synchronized` 출력으로 콘솔 메시지 충돌을 줄였습니다.

---

## 5) 클래스 구성 설명

프로젝트 구조(실제 코드 기준):

```text
src
└─ burger
   ├─ Main.java
   ├─ Store.java
   ├─ model
   │  ├─ MenuItem.java
   │  ├─ Burger.java
   │  ├─ CustomBurger.java
   │  ├─ Ingredient.java
   │  ├─ Ingredients.java
   │  ├─ Order.java
   │  └─ OrderItem.java
   ├─ factory
   │  └─ MenuFactory.java
   ├─ policy
   │  ├─ PricingPolicy.java
   │  └─ BasicPricingPolicy.java
   ├─ service
   │  ├─ OrderManager.java
   │  ├─ InventoryManager.java
   │  ├─ CookingService.java
   │  └─ StatisticsService.java
   └─ util
      └─ ConsolePrinter.java
```

| 파일 | 역할 |
|---|---|
| `burger.Main` | 프로그램 진입점. `Store`를 생성하고 실행 |
| `burger.Store` | 콘솔 메뉴 흐름 제어(입력 처리, 주문/재고/통계/종료 연결) |
| `model.MenuItem` | 메뉴 공통 인터페이스(이름/가격/재료/설명) |
| `model.Burger` | 기본 버거 모델. 메뉴 공통 정보 보관 |
| `model.CustomBurger` | 커스텀 버거 모델. 내부 `Builder`로 구성 |
| `model.Ingredient` | 단일 재료 정보(이름, 가격) |
| `model.Ingredients` | 자주 쓰는 재료 상수 모음 |
| `model.Order` | 주문 단위 정보(주문번호, 고객명, 항목, 총액, 시간) |
| `model.OrderItem` | 주문 내 개별 항목(메뉴, 수량, 소계 계산) |
| `factory.MenuFactory` | 기본 버거 메뉴 생성/제공 |
| `policy.PricingPolicy` | 가격 정책 인터페이스 |
| `policy.BasicPricingPolicy` | 기본 가격 계산(단가 × 수량) |
| `service.OrderManager` | 주문 생성/저장/출력 관리 |
| `service.InventoryManager` | 재고 확인, 재료 소모, 재고 출력 |
| `service.CookingService` | 조리 큐 및 조리 스레드 관리, 알림 생성 |
| `service.StatisticsService` | 총매출/인기 메뉴 통계 출력 |
| `util.ConsolePrinter` | 동기화된 콘솔 출력 유틸리티 |

---

## 6) 실행 방법

### IntelliJ 기준 (권장)
1. IntelliJ에서 프로젝트 폴더 열기
2. Project SDK/JDK 설정 확인 (JDK 17 이상 권장)
3. 실행 클래스에서 `burger.Main` 선택 후 Run
4. 콘솔에 표시되는 메뉴 번호를 입력해 사용

### 터미널 실행 (선택)
> Windows PowerShell에서는 `**` 글롭이 동작하지 않을 수 있으므로, 기본은 IntelliJ 실행을 권장합니다.

```bash
javac -d out src/burger/**/*.java src/burger/*.java
java -cp out burger.Main
```

---

## 7) 실행 화면 예시

### 메인 메뉴 예시
```text
================================
1. 전체 메뉴 보기
2. 기본 버거 주문
3. 커스텀 버거 주문
4. 주문 내역 보기
5. 재고 확인
6. 매출 / 인기 메뉴 통계
0. 종료
================================
```

### 기본 버거 주문 예시
```text
[기본 버거 주문]
1. 클래식버거 - 4500원
2. 치즈버거 - 5000원
...
주문할 메뉴 번호: 2
수량: 2
[주문 확인]
메뉴: 치즈버거
수량: 2
총액: 10000원
주문하시겠습니까? (Y/N): Y
주문이 접수되었습니다.
주문번호: 1001
```

### 커스텀 버거 주문 예시
```text
[커스텀 버거 만들기]
1단계. 패티 종류를 선택하세요.
...
9단계. 소스를 선택하세요. 하나 이상 선택해야 합니다.
선택 예시: 1 4 → 1 3
[커스텀 버거 구성 결과]
재료: 소고기 패티 x 2, 치즈, 양상추, 머스타드
예상 가격: 6100원
이 구성으로 주문하시겠습니까? (Y/N): Y
```

### 조리 알림 예시
```text
[조리 알림]
- 주문 #1001 치즈버거 x 2 조리를 시작했습니다.
- 주문 #1001 치즈버거 x 2 조리가 완료되었습니다.
```

---

## 8) 본인이 구현한 부분
- 콘솔 메뉴 UI 구성 및 사용자 입력 처리
- 기본 메뉴 조회/주문 기능
- 커스텀 버거 Builder 구조 구현
- 주문 내역 생성/저장/출력 관리
- 재고 확인 및 주문 시 재료 차감 처리
- 가격 계산 정책 분리(`PricingPolicy`)
- 매출 통계 및 인기 메뉴 집계
- 조리 Queue + 멀티스레드 기반 처리
- 콘솔 출력 동기화 처리(`ConsolePrinter`)

💫위 내용들에 대한 아이디어 초안과 각종 java 개념을 어디에 사용할 지 구상하고 기능 구현을 직접 하는데 초점을 두었습니다.

---

## 9) AI 활용 여부 및 범위
- AI는 프로젝트 아이디어 구체화, 클래스 구조 설계, 디자인 패턴 적용 방향 검토, README 작성 과정에서 참고했습니다.
- 최종 코드 구성, 실행 테스트, 오류 수정, 제출 내용 검토는 직접 수행했습니다.

---

## 10) 마무리
이 프로젝트는 수업에서 다룬 **객체지향 설계**, **디자인 패턴(Builder/Factory/Strategy)**, **제네릭/컬렉션(List/Map/Queue/BlockingQueue)**, **람다/Stream**, **멀티스레드 기초**를 콘솔 프로그램 형태로 적용해 본 과제입니다.  
복잡한 프레임워크 없이도 역할 분리와 자료구조 선택, 스레드 처리 흐름을 연습하는 데 중점을 두었습니다.

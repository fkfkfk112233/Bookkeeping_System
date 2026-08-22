# Bookkeeping System - Backend

Bookkeeping System 的 Spring Boot 後端，負責 REST
API、商業邏輯、資料驗證、資料存取與 SQLite 資料庫操作。

## Tech Stack

-   Java 21
-   Spring Boot 4.1
-   Spring Web MVC
-   Spring Data JPA
-   Hibernate
-   Jakarta Validation
-   Maven
-   SQLite
-   SQLite JDBC

## Requirements

-   JDK 21
-   Maven Wrapper（專案已提供）

確認 Java：

``` bash
java -version
```

## Run Backend

進入 backend：

``` bash
cd backend
```

### Windows

``` bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

``` bash
./mvnw spring-boot:run
```

Backend 預設執行於：

``` text
http://localhost:8080
```

## Build

Windows：

``` bash
mvnw.cmd clean package
```

Linux / macOS：

``` bash
./mvnw clean package
```

## Database

目前使用 SQLite：

``` text
bookkeeping.db
```

資料庫設定位於：

``` text
src/main/resources/application.properties
```

目前主要設定：

``` properties
spring.datasource.url=jdbc:sqlite:bookkeeping.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

`ddl-auto=update` 會依照目前 Entity 結構更新資料表。

## Project Structure

``` text
backend/
│
├── src/
│   ├── main/
│   │   ├── java/backend/
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   └── TransactionController.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── category/
│   │   │   │   ├── dashboard/
│   │   │   │   └── transaction/
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── Category.java
│   │   │   │   ├── Transaction.java
│   │   │   │   └── User.java
│   │   │   │
│   │   │   ├── enums/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── bookkeeping.db
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Architecture

``` text
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
Entity / SQLite
```

### Controller

負責接收 HTTP Request、呼叫 Service，以及建立 HTTP Response。

目前主要 Controller：

-   `TransactionController`
-   `CategoryController`
-   `DashboardController`

### Service

負責主要商業邏輯，例如：

-   建立 / 修改 / 刪除交易
-   建立 / 修改 / 刪除分類
-   交易與分類驗證
-   Dashboard 統計

### Repository

使用 Spring Data JPA 存取 SQLite 資料庫。

### Entity

目前主要 Entity：

-   `User`
-   `Category`
-   `Transaction`

### DTO

使用 DTO 將 API request / response 與 Entity 分離。

## REST API

### Transactions

Base URL：

``` text
/api/transactions
```

  -------------------------------------------------------------------------------------------------------------
  Method                  Endpoint                                                      Description
  ----------------------- ------------------------------------------------------------- -----------------------
  GET                     `/api/transactions?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`   查詢日期區間交易

  GET                     `/api/transactions/{id}`                                      查詢單筆交易

  POST                    `/api/transactions`                                           新增交易

  PUT                     `/api/transactions/{id}`                                      修改交易

  DELETE                  `/api/transactions/{id}`                                      刪除交易
  -------------------------------------------------------------------------------------------------------------

### Categories

Base URL：

``` text
/api/categories
```

  Method   Endpoint                 Description
  -------- ------------------------ --------------
  GET      `/api/categories`        查詢全部分類
  GET      `/api/categories/{id}`   查詢單一分類
  POST     `/api/categories`        新增分類
  PUT      `/api/categories/{id}`   修改分類
  DELETE   `/api/categories/{id}`   刪除分類

### Dashboard

Base URL：

``` text
/api/dashboard
```

  Method   Endpoint                         Description
  -------- -------------------------------- ----------------------
  GET      `/api/dashboard/summary`         收入、支出、結餘摘要
  GET      `/api/dashboard/income`          收入分類統計
  GET      `/api/dashboard/expense`         支出分類統計
  GET      `/api/dashboard/income/trend`    收入趨勢
  GET      `/api/dashboard/expense/trend`   支出趨勢
  GET      `/api/dashboard/frequency`       交易頻率統計

Dashboard API 使用：

``` text
startDate
endDate
```

例如：

``` text
/api/dashboard/summary?startDate=2026-08-01&endDate=2026-08-31
```

## Validation & Error Handling

API Request 使用 Jakarta Validation。

全域例外處理位於：

``` text
exception/GlobalExceptionHandler.java
```

目前負責統一處理常見的資源不存在、驗證與 API 錯誤。

## CORS

前後端分離開發時，由 `CorsConfig` 處理跨來源請求設定。

預設：

``` text
Frontend: http://localhost:5173
Backend:  http://localhost:8080
```

## Development Notes

目前版本以單一使用者測試情境為主，資料模型已包含
`User`，但部分交易與分類流程仍以固定使用者資料進行開發。

未來加入登入系統後，建議完成：

1.  Authentication / Authorization
2.  User 與 Transaction / Category 的完整關聯
3.  Repository 查詢加入 User scope
4.  API Authorization
5.  多使用者資料隔離

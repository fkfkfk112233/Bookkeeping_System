# Bookkeeping System

個人記帳管理系統，採用前後端分離架構，提供收入、支出、分類管理與
Dashboard 統計功能。

## Features

-   Dashboard 收支總覽
-   收入 / 支出交易管理
-   新增、編輯、刪除交易
-   今日 / 本週 / 本月交易篩選
-   收入分類管理
-   支出分類管理
-   新增、編輯、刪除分類
-   收入 / 支出分類統計
-   收入 / 支出趨勢圖
-   交易頻率統計
-   RESTful API
-   SQLite 資料庫
-   Responsive UI
-   前後端分離架構

## Tech Stack

### Frontend

-   React 19
-   Vite 8
-   React Router 7
-   Axios
-   Bootstrap 5
-   Chart.js
-   react-chartjs-2

### Backend

-   Java 21
-   Spring Boot 4.1
-   Spring Web MVC
-   Spring Data JPA
-   Hibernate
-   Jakarta Validation
-   Maven

### Database

-   SQLite
-   SQLite JDBC Driver

## Project Structure

``` text
Bookkeeping_System/
│
├── README.md
│
├── frontend/
│   ├── README.md
│   ├── package.json
│   └── src/
│       ├── components/
│       ├── pages/
│       └── services/
│
├── backend/
│   ├── README.md
│   ├── pom.xml
│   ├── bookkeeping.db
│   └── src/
│       ├── main/java/backend/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── enums/
│       │   ├── exception/
│       │   ├── repository/
│       │   └── service/
│       └── main/resources/
│
└── .vscode/
```

## Architecture

``` text
┌─────────────────────┐
│   React Frontend    │
│   localhost:5173    │
└──────────┬──────────┘
           │ Axios / REST API
           ▼
┌─────────────────────┐
│ Spring Boot Backend │
│   localhost:8080    │
└──────────┬──────────┘
           │ Spring Data JPA
           ▼
┌─────────────────────┐
│       SQLite        │
│   bookkeeping.db    │
└─────────────────────┘
```

Backend 內部採用 Controller → Service → Repository → Entity
的分層方式，並以 DTO 作為 API request / response 與 Entity
之間的資料邊界。

## Getting Started

### Requirements

請先準備：

-   JDK 21
-   Node.js
-   npm

確認環境：

``` bash
java -version
node -v
npm -v
```

### 1. Start Backend

開啟終端機：

``` bash
cd backend
```

Windows：

``` bash
mvnw.cmd spring-boot:run
```

Linux / macOS：

``` bash
./mvnw spring-boot:run
```

Backend 預設執行於：

``` text
http://localhost:8080
```

### 2. Start Frontend

開啟另一個終端機：

``` bash
cd frontend
npm install
npm run dev
```

Frontend 預設執行於：

``` text
http://localhost:5173
```

前端需要在 Backend 已啟動的情況下使用完整功能。

## Frontend Pages

  Route             Page           Description
  ----------------- -------------- ------------------------------------
  `/`               Dashboard      收支摘要、分類統計、趨勢與交易頻率
  `/transactions`   Transactions   管理收入與支出交易
  `/categories`     Categories     管理收入與支出分類

## API Overview

### Transactions

``` text
GET    /api/transactions?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
GET    /api/transactions/{id}
POST   /api/transactions
PUT    /api/transactions/{id}
DELETE /api/transactions/{id}
```

### Categories

``` text
GET    /api/categories
GET    /api/categories/{id}
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}
```

### Dashboard

``` text
GET /api/dashboard/summary
GET /api/dashboard/income
GET /api/dashboard/expense
GET /api/dashboard/income/trend
GET /api/dashboard/expense/trend
GET /api/dashboard/frequency
```

Dashboard 與交易查詢 API 使用 `startDate`、`endDate`：

``` text
?startDate=2026-08-01&endDate=2026-08-31
```

## UI / CSS

前端主要使用 Bootstrap 5，並搭配專案共用的 `index.css` 維持頁面一致性。

目前頁面共用：

-   Bootstrap Card
-   Bootstrap Button
-   Bootstrap Table
-   Bootstrap responsive utilities
-   `page-container`
-   `page-header`
-   共用 responsive spacing

Dashboard、Transactions、Categories
的卡片維持一致的視覺風格，並針對小螢幕進行 responsive 排版。

## Development Notes

目前版本主要以單一使用者測試情境為開發目標。

目前資料模型已包含
`User`，但部分交易與分類流程仍以固定使用者資料進行測試；若後續加入完整登入系統，需要進一步完成：

1.  Authentication / Authorization
2.  User 與 Transaction / Category 的完整關聯
3.  Repository 查詢加入 User scope
4.  API 層級的使用者權限驗證
5.  多使用者資料隔離

## Documentation

-   [Frontend README](./frontend/README.md)
-   [Backend README](./backend/README.md)

## License

目前為學習與專案開發用途。

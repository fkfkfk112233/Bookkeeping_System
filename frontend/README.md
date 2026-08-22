# Bookkeeping System - Frontend

Bookkeeping System 的 React
前端，負責頁面呈現、使用者互動、交易與分類表單、Dashboard 圖表，以及與
Spring Boot REST API 的溝通。

## Tech Stack

-   React 19
-   Vite 8
-   React Router 7
-   Axios
-   Bootstrap 5
-   Chart.js
-   react-chartjs-2

## Requirements

-   Node.js
-   npm

確認環境：

``` bash
node -v
npm -v
```

## Installation

進入 frontend：

``` bash
cd frontend
```

安裝套件：

``` bash
npm install
```

## Development

啟動開發伺服器：

``` bash
npm run dev
```

通常會使用：

``` text
http://localhost:5173
```

使用完整功能前，請先啟動 Backend `http://localhost:8080`。

## Build / Preview

建立 production build：

``` bash
npm run build
```

預覽 production build：

``` bash
npm run preview
```

## Lint

執行 ESLint：

``` bash
npm run lint
```

## Pages

  Route             Page           Function
  ----------------- -------------- --------------------------------------
  `/`               Dashboard      收支摘要、分類統計、趨勢圖、交易頻率
  `/transactions`   Transactions   收入 / 支出交易 CRUD
  `/categories`     Categories     收入 / 支出分類 CRUD

### Dashboard

Dashboard 提供：

-   日期區間統計
-   收入
-   支出
-   結餘
-   收入分類統計
-   支出分類統計
-   收入趨勢
-   支出趨勢
-   交易頻率

### Transactions

提供：

-   今日 / 本週 / 本月篩選
-   收入交易列表
-   支出交易列表
-   新增收入
-   新增支出
-   編輯交易
-   刪除交易

新增與編輯交易共用 `TransactionModal`。

### Categories

提供：

-   收入分類管理
-   支出分類管理
-   新增分類
-   編輯分類
-   刪除分類

## Project Structure

``` text
frontend/
│
├── src/
│   ├── components/
│   │   ├── ChartCard.jsx
│   │   ├── DateTimeCard.jsx
│   │   ├── IncomeExpensePieChart.jsx
│   │   ├── Layout.jsx
│   │   ├── Navbar.jsx
│   │   ├── SummaryCard.jsx
│   │   ├── TransactionModal.jsx
│   │   ├── TransactionTable.jsx
│   │   └── TrendLineChart.jsx
│   │
│   ├── pages/
│   │   ├── Dashboard.jsx
│   │   ├── Transactions.jsx
│   │   └── Categories.jsx
│   │
│   ├── services/
│   │   ├── api.js
│   │   ├── categoryApi.js
│   │   ├── dashboardApi.js
│   │   └── transactionApi.js
│   │
│   ├── App.jsx
│   ├── App.css
│   ├── index.css
│   └── main.jsx
│
├── index.html
├── package.json
├── package-lock.json
└── vite.config.js
```

## API Communication

API 呼叫集中在 `src/services/`：

``` text
services/
├── api.js
├── categoryApi.js
├── dashboardApi.js
└── transactionApi.js
```

頁面與元件透過 service 呼叫 Backend API，避免在 UI 元件中直接重複建立
API request。

``` text
React Page / Component
          │
          ▼
       Service
          │
          ▼
      Axios / API
          │
          ▼
Spring Boot Backend
```

## Styling

專案主要使用 Bootstrap 5 進行 UI 與 responsive layout，並使用
`src/index.css` 放置全域與共用頁面樣式。

目前共用樣式包含：

-   `page-container`
-   `page-header`
-   Bootstrap `card`
-   Bootstrap `btn`
-   Bootstrap `table`
-   Bootstrap responsive utilities
-   mobile spacing adjustments

Dashboard、Transactions、Categories 的卡片樣式維持一致，交易頁面的收入 /
支出卡片也使用相同的 Bootstrap Card 結構。

## Development Notes

目前前端以單一使用者記帳情境為主。

未來加入登入系統後，API service 可進一步整合：

-   Authentication token
-   Authorization
-   User-specific API request
-   Error handling / session expiration

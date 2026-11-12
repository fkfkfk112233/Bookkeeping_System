# Bookkeeping System

個人記帳系統，採用前後端分離架構，提供一般使用者的收支管理功能，以及管理者的使用者管理與系統統計功能。

## 系統架構

| 模組 | 技術 | 說明 |
|---|---|---|
| Frontend | React 19、Vite 8、React Router、Axios、Bootstrap 5、Chart.js | 使用者介面、頁面路由、圖表與 API 串接 |
| Backend | Java 21、Spring Boot 4.1.0、Spring Security、Spring Data JPA、Hibernate | REST API、登入驗證、權限控管與資料存取 |
| Database | SQLite | 儲存使用者、分類與交易資料 |

專案目錄：

```text
Bookkeeping_System/
├── frontend/       # React 前端
├── backend/        # Spring Boot 後端
└── README.md       # 系統說明與啟動方式
```

## 系統功能

### 一般使用者

- Session 登入與登出
- 收支 Dashboard 與統計圖表
- 收入、支出交易新增、查詢、修改及刪除
- 個人分類管理
- 個人資料修改與帳號停用

### 管理者

- 管理者登入
- 管理者 Dashboard
- 管理者、一般使用者、今日登入及停權人數統計
- 使用者新增、查詢、修改及刪除

## 環境需求

請先安裝以下工具：

- Java JDK 21
- Node.js 與 npm（建議使用目前仍受支援的 LTS 版本）
- Git（選用）

Backend 使用 Maven Wrapper，通常不需要另外安裝 Maven。

## 本機啟動

請在專案根目錄開啟兩個終端機，分別啟動 Backend 與 Frontend。

### 1. 啟動 Backend

Windows PowerShell：

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Backend 預設位址：`http://localhost:8080`

### 2. 啟動 Frontend

另開一個 PowerShell：

```powershell
cd frontend
npm ci
npm run dev
```

Frontend 預設位址：`http://localhost:5173`

在瀏覽器開啟 `http://localhost:5173` 即可使用系統。

## 預設帳號與開發測試資料

未指定其他 Spring Profile 時，Backend 預設使用 `dev` profile。第一次啟動時會依序執行初始化與測試資料產生：

1. `DataInitializer`：建立預設管理員及系統分類。
2. `DataSeeder`：建立一般測試帳號，並產生測試交易。

預設帳號：

| 身分 | Username | Password | Role |
|---|---|---|---|
| 管理者 | `admin` | `admin123` | `ADMIN` |
| 測試使用者 | `test` | `test123` | `USER` |

`DataSeeder` 會產生 2026/01/01～2026/11/11（含首尾）的 220 筆測試交易：每月 20 筆，其中 4 筆收入、16 筆支出，涵蓋五種支付方式。所有 Seeder 交易都屬於 `test` 帳號。

每次以 `dev` profile 啟動時，Seeder 會先刪除 `test` 帳號中描述以 `[TEST-SEED]` 開頭的舊測試交易，再重新產生；其他手動建立的交易不會被刪除。開發環境每次啟動也會確保 `test` 帳號密碼為 `test123`、角色為 `USER` 且帳號啟用。

SQLite 資料庫預設只保留資料表結構，不預先放入使用者、分類或交易資料；上述資料會在 Backend 啟動時建立。資料庫檔案位置為 `backend/bookkeeping.db`（以 Backend 工作目錄及 JDBC 設定為準）。

> 注意：`admin/admin123` 與 `test/test123` 是開發測試帳號，不應直接作為正式環境帳密使用。

## 環境設定

Frontend 預設呼叫 `http://localhost:8080/api`。若 Backend 位址不同，請在 `frontend/` 建立 `.env.local`：

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

修改環境變數後，請重新啟動 Vite 開發伺服器。

Backend 預設使用 `dev` profile。若要指定其他 Profile，例如 `prod`，可在 PowerShell 設定：

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
.\mvnw.cmd spring-boot:run
```

指定 `prod` 後不會啟用 `dev` profile 的測試資料產生器。正式環境仍須自行設定適當的資料庫、帳號、網域、CORS 與安全設定。

## 驗證指令

Frontend：

```powershell
cd frontend
npm ci
npm run lint
npm run build
```

Backend：

```powershell
cd backend
.\mvnw.cmd clean test
```

## 子專案文件

- [Frontend 開發與啟動說明](frontend/README.md)
- [Backend 開發與啟動說明](backend/README.md)

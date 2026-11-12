# Bookkeeping System

個人記帳系統，採前後端分離架構。

## 專案組成

- `frontend/`：React 19 + Vite + Bootstrap + Chart.js
- `backend/`：Java 21 + Spring Boot + Spring Security + Spring Data JPA
- Database：SQLite

## 本機啟動

### 1. Backend

Windows PowerShell：

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Backend 預設：`http://localhost:8080`。

### 2. Frontend

另開 PowerShell：

```powershell
cd frontend
npm ci
npm run dev
```

Frontend 預設：`http://localhost:5173`。

## 功能

### 一般使用者
- Session 登入與登出
- 個人收支 Dashboard
- 收入 / 支出交易管理
- 分類管理
- 個人資料修改與帳號停用

### 管理者
- 管理者 Dashboard
- 管理者、一般使用者、今日登入及停權人數統計
- 使用者新增、修改、查詢及刪除

## 驗證指令

Backend：

```powershell
cd backend
.\mvnw.cmd clean test
```

Frontend：

```powershell
cd frontend
npm ci
npm run lint
npm run build
```

各模組細節請參閱 `frontend/README.md` 與 `backend/README.md`。

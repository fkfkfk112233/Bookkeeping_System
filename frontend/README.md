# Frontend

Bookkeeping System 的前端專案，使用 React 建立單頁式應用程式，透過 Axios 呼叫 Spring Boot Backend API。

## 技術

- React 19
- Vite 8
- React Router 7
- Axios
- Bootstrap 5
- Chart.js 4
- react-chartjs-2

## 功能頁面

### 一般使用者

| 路由 | 頁面 | 說明 |
|---|---|---|
| `/login` | 使用者登入 | 一般使用者登入 |
| `/dashboard` | Dashboard | 收支摘要與統計圖表 |
| `/transactions` | 交易管理 | 管理收入與支出交易 |
| `/categories` | 分類管理 | 管理個人收支分類 |
| `/profile` | 個人資料 | 查看及修改個人資料、停用帳號 |

### 管理者

| 路由 | 頁面 | 說明 |
|---|---|---|
| `/login/admin` | 管理者登入 | 管理者登入 |
| `/admin` | 管理者 Dashboard | 顯示系統使用者統計 |
| `/admin/users` | 使用者管理 | 新增、查詢、修改及刪除使用者 |

根路由 `/` 會重新導向至 `/login`。

## 環境需求

- Node.js（建議使用目前仍受支援的 LTS 版本）
- npm
- 可正常啟動的 Backend

## 安裝與啟動

在 `frontend/` 目錄執行：

```powershell
npm ci
npm run dev
```

Vite 預設會在以下網址啟動：

```text
http://localhost:5173
```

若要建立正式版前端：

```powershell
npm run build
```

建置結果會輸出至 `dist/`。若要在本機預覽建置結果：

```powershell
npm run preview
```

## Backend API 設定

API 共用 Axios instance 位於：

```text
src/services/api.js
```

預設 API Base URL：

```text
http://localhost:8080/api
```

若 Backend 使用其他位址，請在 `frontend/` 建立 `.env.local`，設定：

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

`.env.example` 提供環境變數範例。Vite 只會將 `VITE_` 開頭的環境變數提供給前端程式。修改環境變數後，必須重新啟動開發伺服器。

Axios 設定包含 `withCredentials: true`，用於跨來源請求時傳送 Session Cookie。因此 Backend 必須允許 Frontend 的來源，且 CORS 必須允許 Credentials。目前 Backend 預設允許的開發來源為 `http://localhost:5173`。

## 專案結構

```text
frontend/
├── public/                 # 靜態資源（若有）
├── src/
│   ├── components/         # 共用 UI 元件
│   ├── pages/              # 頁面元件
│   │   └── admin/          # 管理者頁面
│   ├── services/           # Axios 與各功能 API 呼叫
│   ├── utils/              # 共用工具，例如登入狀態處理
│   ├── App.jsx             # 路由設定
│   ├── App.css             # App 樣式
│   ├── index.css           # 全域樣式
│   └── main.jsx            # React 入口
├── .env.example            # 環境變數範例
├── package.json            # 套件與 npm scripts
└── vite.config.js          # Vite 設定
```

## 常用指令

| 指令 | 用途 |
|---|---|
| `npm ci` | 依照 package-lock.json 安裝套件 |
| `npm run dev` | 啟動 Vite 開發伺服器 |
| `npm run lint` | 執行 ESLint 檢查 |
| `npm run build` | 建置正式版前端 |
| `npm run preview` | 預覽正式版建置結果 |

## 注意事項

- Frontend 不直接連線 SQLite，所有資料操作都透過 Backend API。
- 登入狀態使用 Backend Session Cookie 維持；API 請求需保留 `withCredentials: true`。
- 若遇到 API 連線失敗，請先確認 Backend 是否啟動、`VITE_API_BASE_URL` 是否正確，以及 Backend CORS 是否允許目前前端來源。
- `dist/` 是建置產物，不是主要開發原始碼。

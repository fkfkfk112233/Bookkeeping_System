# Backend

Bookkeeping System 的後端服務，使用 Spring Boot 提供 REST API，負責登入驗證、權限控管、交易與分類管理、統計資料及資料庫存取。

## 技術

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Hibernate ORM / Hibernate Community Dialects（SQLite）
- SQLite JDBC
- Bean Validation
- Maven Wrapper

## 環境需求

- Java JDK 21
- Windows PowerShell（以下指令以 Windows 為例）

專案包含 Maven Wrapper，Windows 可使用 `mvnw.cmd`，不需要另外安裝 Maven。

## 啟動方式

在 `backend/` 目錄執行：

```powershell
.\mvnw.cmd spring-boot:run
```

Backend 預設啟動位址：

```text
http://localhost:8080
```

預設 API 前綴：

```text
http://localhost:8080/api
```

## Spring Profile 與初始化資料

`src/main/resources/application.properties` 設定：

```properties
spring.profiles.default=dev
```

因此在未指定 `SPRING_PROFILES_ACTIVE` 時，預設會使用 `dev` profile。啟動時依照 `@Order` 順序執行：

1. `DataInitializer`（`@Order(1)`）：建立預設管理者及系統分類。
2. `DataSeeder`（`@Profile("dev")`、`@Order(2)`）：建立測試使用者及測試交易。

### 預設管理者

| 欄位 | 值 |
|---|---|
| Username | `admin` |
| Password | `admin123` |
| Role | `ADMIN` |

`DataInitializer` 只會在找不到 `admin` 帳號時建立預設管理者；若該帳號已存在，不會在每次啟動時重設其密碼。

### 預設測試使用者

| 欄位 | 值 |
|---|---|
| Username | `test` |
| Password | `test123` |
| Role | `USER` |

`DataSeeder` 每次在 `dev` profile 啟動時，都會確保 `test` 帳號存在、密碼為 `test123`、角色為 `USER` 且帳號啟用。因此若在開發環境手動修改該帳號密碼，下次啟動時會被重設。

### 測試交易資料

Seeder 會使用 Java、Entity 與 Repository 產生固定亂數種子的測試資料：

- 日期範圍：2026/01/01～2026/11/11（含首尾）
- 總筆數：220 筆
- 每月：20 筆
- 每月收入：4 筆
- 每月支出：16 筆
- 所有測試交易均關聯至 `test` 使用者
- 交易描述以 `[TEST-SEED]` 開頭
- 涵蓋五種支付方式

每次啟動時，Seeder 只會刪除 `test` 使用者中描述以 `[TEST-SEED]` 開頭的交易，再重新產生測試交易。該使用者自行新增、且描述不以此標記開頭的交易會保留。

### 關閉開發測試資料

若要使用其他 Profile，例如 `prod`，可在 PowerShell 設定：

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
.\mvnw.cmd spring-boot:run
```

明確指定 `prod` 後不會啟用 `dev` profile，也不會執行 `DataSeeder`。請注意，`DataInitializer` 目前沒有 Profile 限制，仍會在啟動時檢查並建立預設管理者與系統分類。

## Database

使用 SQLite，預設連線設定：

```properties
spring.datasource.url=jdbc:sqlite:bookkeeping.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

資料庫檔案預設為 Backend 工作目錄下的 `bookkeeping.db`。Hibernate 會依 Entity 建立或更新資料表；初始化程式再建立管理者、系統分類及開發測試資料。

主要資料表：

- `users`：使用者帳號、密碼、角色與啟用狀態
- `categories`：收入與支出分類
- `transactions`：交易紀錄

## API 功能範圍

| Controller | Base Path | 功能 |
|---|---|---|
| `AuthController` | `/api/auth` | 登入、登出 |
| `DashboardController` | `/api/dashboard` | 一般使用者收支摘要與統計 |
| `TransactionController` | `/api/transactions` | 交易 CRUD |
| `CategoryController` | `/api/categories` | 分類 CRUD |
| `UserProfileController` | `/api/users` | 個人資料與帳號停用；另含管理者使用者資料操作 |
| `UserController` | `/api/admin/users` | 管理者使用者管理 |
| `AdminDashboardController` | `/api/admin/dashboard` | 管理者統計摘要 |

主要 API 路徑（詳細 Request/Response 請以各 Controller 與 DTO 為準）：

```text
POST   /api/auth/login
POST   /api/auth/logout
GET    /api/dashboard/summary
GET    /api/transactions
POST   /api/transactions
GET    /api/categories
GET    /api/users/me
PUT    /api/users/me
GET    /api/admin/dashboard/summary
GET    /api/admin/users
```

## Security 與 Session

- 使用 Spring Security 驗證使用者。
- 密碼使用 BCrypt 編碼。
- 登入狀態採 Session Cookie 維持。
- `/api/auth/**` 允許未登入存取。
- `/api/admin/**` 限制 `ADMIN` 角色。
- 個人資料 `/api/users/me` 需登入。
- 交易、分類及一般 Dashboard API 需具備 `USER` 或 `ADMIN` 角色。
- 其他使用者管理 API 限制管理者使用。

## CORS

目前 `CorsConfig` 預設允許的前端來源為：

```text
http://localhost:5173
```

並允許帶 Credentials 的跨來源請求。若前端改用其他網域或 Port，需同步調整 CORS 設定。

## 專案結構

```text
backend/
├── src/main/java/backend/
│   ├── config/          # Security、CORS、初始化與 Seeder
│   ├── controller/      # REST API Controller
│   ├── dto/             # API Request / Response DTO
│   ├── entity/          # JPA Entity
│   ├── enums/           # 交易類型、支付方式等列舉
│   ├── exception/       # 全域例外處理
│   ├── repository/      # Spring Data JPA Repository
│   ├── security/        # Spring Security 使用者服務
│   └── service/         # 業務邏輯
├── src/main/resources/
│   └── application.properties
├── src/test/            # 測試程式
├── bookkeeping.db       # SQLite 資料庫（執行後建立或更新）
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## 常用指令

```powershell
# 啟動
.\mvnw.cmd spring-boot:run

# 執行測試
.\mvnw.cmd test

# 清理並執行測試
.\mvnw.cmd clean test

# 打包
.\mvnw.cmd clean package
```

## 注意事項

- 開發環境預設會重新產生 `test` 帳號的 Seeder 假交易；請勿將這些資料誤認為真實使用者資料。
- `test` 帳號密碼會在每次 `dev` 啟動時重設為 `test123`。
- `admin/admin123` 與 `test/test123` 僅供開發測試，不應用於正式環境。
- 正式部署前，應另行檢查正式資料庫、環境變數、CORS、Session Cookie、CSRF 與其他安全設定。

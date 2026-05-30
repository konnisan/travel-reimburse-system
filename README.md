# Travel Reimburse System

差旅报销系统，采用前后端分离结构：

- `backend/`：Spring Boot + MyBatis-Plus 后端服务
- `frontend/`：Vue 3 + Vite 前端应用
- `fk.sql`：数据库初始化脚本

## 环境要求

- JDK 17
- Maven 3.8+
- Node.js 20+
- MySQL 5.7+ 或 8.x

## 数据库准备

默认数据库名为 `fk`。先创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS fk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

导入初始化脚本：

```sh
mysql -u root -p fk < fk.sql
```

如果使用的是已经存在的旧数据库，并且 `fk_reim_main` 表还没有 `bill_date` 字段，需要执行一次迁移脚本：

```sh
mysql -u root -p fk < doc/migration-add-bill-date.sql
```

也可以手动执行：

```sql
ALTER TABLE fk_reim_main
  ADD COLUMN bill_date varchar(20) NULL COMMENT 'Bill date' AFTER creation_time;

UPDATE fk_reim_main
SET bill_date = LEFT(creation_time, 10)
WHERE bill_date IS NULL
  AND creation_time IS NOT NULL
  AND creation_time <> '';
```

## 启动后端

进入后端目录：

```sh
cd backend
```

默认连接：

- 数据库地址：`jdbc:mysql://localhost:3306/fk`
- 数据库用户：`root`
- 数据库密码：`123456`
- 后端端口：`8080`

直接启动：

```sh
mvn spring-boot:run
```

如需覆盖数据库连接或 JWT 配置，可以通过环境变量传入：

```sh
set DB_URL=jdbc:mysql://localhost:3306/fk?useUnicode=true^&characterEncoding=utf-8^&serverTimezone=Asia/Shanghai
set DB_USERNAME=root
set DB_PASSWORD=123456
set JWT_SECRET=change-me
set JWT_EXPIRES_IN_SECONDS=7200
mvn spring-boot:run
```

PowerShell 示例：

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "123456"
$env:JWT_SECRET = "change-me"
mvn spring-boot:run
```

## 启动前端

进入前端目录并安装依赖：

```sh
cd frontend
npm install
```

启动开发服务器：

```sh
npm run dev
```

Vite 默认会启动在 `http://localhost:5173`。前端开发代理已在 `frontend/vite.config.ts` 中配置，会把 `/api`、`/auth`、`/b2c` 转发到 `http://localhost:8080`。

## 常用命令

后端测试：

```sh
cd backend
mvn test
```

前端构建：

```sh
cd frontend
npm run build
```

前端单元测试：

```sh
cd frontend
npm run test:unit
```

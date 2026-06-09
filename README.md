# Travel Reimburse System

差旅报销系统，采用前后端分离结构：

- `backend/`：Spring Boot + MyBatis-Plus 后端服务
- `frontend/`：Vue 3 + Vite 前端应用
- `fk.sql`：完整数据库初始化脚本
- `doc/`：旧数据库迁移脚本

## 环境要求

- JDK 17
- Maven 3.8+
- Node.js 20+
- MySQL 5.7+ 或 8.x
- Redis 6+

## 数据库准备

默认数据库名为 `fk`。先创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS fk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

新环境直接导入完整初始化脚本即可，`fk.sql` 已包含 `bill_date`、乐观锁字段 `version`，并已将金额字段规范为 `DECIMAL`、日期字段规范为 `DATE/DATETIME`：

```sh
mysql -u root -p fk < fk.sql
```

也可以在数据库管理工具里手动执行：

```sql
ALTER TABLE fk_reim_main
  ADD COLUMN bill_date varchar(20) NULL COMMENT 'Bill date' AFTER creation_time;

UPDATE fk_reim_main
SET bill_date = LEFT(creation_time, 10)
WHERE bill_date IS NULL
  AND creation_time IS NOT NULL
  AND creation_time <> '';

ALTER TABLE fk_reim_main
  ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
```

字段类型规范化脚本会将金额字段调整为 `DECIMAL(18,2)`，将单据日期、行程日期、补助日历日期调整为日期类型，并将标题、事由、备注等字段长度调整到页面规则要求的范围。

## Redis 准备

后端默认连接 Redis：

- 地址：`localhost`
- 端口：`6379`
- DB：`1`
- 密码：`123456`

如果本机 Redis 配置不同，可以通过环境变量覆盖：

```powershell
$env:REDIS_HOST = "localhost"
$env:REDIS_PORT = "6379"
$env:REDIS_DATABASE = "1"
$env:REDIS_PASSWORD = "123456"
```

## 账号与流程

内置演示账号：

- `employee / employee123`：新增、保存草稿、提交报销单
- `finance / finance123`：查看、审核、作废报销单
- `admin / admin123`：完整管理权限

单据状态：

- `0 草稿`：员工可编辑、保存、提交
- `1 待审核`：员工提交后进入该状态，财务可审核
- `2 已作废`：作废后的历史单据不可继续编辑
- `3 已完成`：财务审核通过后的最终状态

管理员编辑 `待审核` 或 `已完成` 单据时，系统不会直接覆盖原单，而是将原单作废，并生成一张新的草稿修订单，避免破坏已提交或已审核历史。

## 启动后端

进入后端目录：

```sh
cd backend
```

默认数据库连接：

- 地址：`jdbc:mysql://localhost:3306/fk?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai`
- 用户名：`root`
- 密码：`123456`
- 端口：`8080`

启动：

```sh
mvn spring-boot:run
```

如需覆盖数据库或 JWT 配置：

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "123456"
$env:JWT_SECRET = "change-me"
$env:JWT_EXPIRES_IN_SECONDS = "7200"
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

Vite 默认运行在 `http://localhost:5173`。前端开发代理已配置，会把 `/api`、`/auth`、`/b2c` 转发到 `http://localhost:8080`。

## 常用命令

后端测试：

```sh
cd backend
mvn test
```

前端类型检查：

```sh
cd frontend
npm run type-check
```

前端构建：

```sh
cd frontend
npm run build
```

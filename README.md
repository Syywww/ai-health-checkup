<p align="center">
	<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">智康体检预约系统</h1>
	<h4 align="center">基于 RuoYi-Vue 3.9.2 前后端分离的体检预约 + AI 助手系统</h4>
</p>

## 项目简介

一个基于 **RuoYi-Vue 3.9.2**（Spring Boot 3 + Vue 3 前后端分离）的体检预约管理系统。在若依快速开发框架之上实现了完整的体检预约业务闭环：

- **预约设置日历化**：按月放号、每日可预约人数、Excel 批量导入导出；
- **预约下单联动**：与放号数据实时联动，满号拦截、未放号拦截，原子扣减防超卖；
- **基础资料管理**：会员、检查项、检查组、体检套餐、体检记录；
- **AI 助手**：内置 AI 对话（会话 + 消息记录），支持模型配置。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Java 17 · Spring Boot 3.2 · Spring Security · JWT |
| 持久层 | MyBatis · MySQL 8 · Redis（缓存/验证码） |
| 前端 | Vue 3 · Element Plus · Vite · Pinia |
| 工具 | Maven · Lombok · Excel（EasyExcel 风格导入导出） |

## 功能模块

| 模块 | 说明 |
| --- | --- |
| 预约管理 | 检查项 / 检查组 / 套餐管理（含关联配置） |
| 预约设置 | 日历化放号：按月查看、按天设置人数、模板下载、Excel 上传批量放号 |
| 会员预约 | 会员档案管理、预约下单（与放号实时联动、防超卖）、体检记录 |
| 系统管理 | 若依自带：用户 / 角色 / 菜单 / 字典 / 日志 / 定时任务 |
| AI 助手 | AI 对话会话管理、消息记录 |

### 预约设置（日历化）

`appointment_setting` 一张表三个字段完成日历化：

```
setting_date (唯一) + max_count + reserved_count
```

- `GET  /reservation/setting/getOrderSettingByMonth?month=YYYY-MM` 按月查询
- `POST /reservation/setting/add` 按天放号（幂等：已存在则更新）
- `PUT  /reservation/setting/editNumberByOrderDate` 修改某天可预约数
- `GET  /reservation/setting/downloadTemplate` 下载 Excel 模板
- `POST /reservation/setting/upload` 上传 Excel 批量放号

### 预约下单（防超卖）

预约时对放号日期做三道校验，并通过**原子 SQL 扣减**防止超卖：

```sql
UPDATE appointment_setting
SET reserved_count = reserved_count + 1
WHERE id = #{id} AND reserved_count < max_count
```

条件不满足时影响 0 行 → 拒绝并抛"该日期预约已满"，事务回滚，预约记录不会落库。

## 目录结构

```
ai-health-checkup/
├── health/                  # 后端（Maven 多模块，RuoYi-Vue 3.9.2）
│   ├── health-admin/        #   Web 入口模块
│   ├── health-ai/           #   AI 助手（会话/消息）
│   ├── health-member/       #   会员/体检记录
│   ├── health-reservation/  #   预约设置/预约下单（日历化 + 防超卖）
│   ├── health-common/       #   公共模块
│   ├── health-framework/    #   框架核心（安全/配置）
│   ├── health-system/       #   系统管理
│   ├── health-generator/    #   代码生成
│   ├── health-quartz/       #   定时任务
│   └── sql/                 #   建库/建表/菜单/字典脚本
└── health-Vue3-master/      # 前端（Vue 3 + Element Plus）
    ├── src/
    │   ├── api/reservation/ #   预约相关接口封装
    │   ├── views/reservation/ # 预约设置日历页等
    │   └── ...
    └── vite.config.js
```

## 快速开始

### 1. 初始化数据库

```bash
# 依次执行（库名 health）
mysql -u root -p < health/sql/ry_20260417.sql                    # 若依系统表
mysql -u root -p health < health/sql/health_business_tables.sql  # 业务表
mysql -u root -p health < health/sql/health_business_menu_dict.sql # 菜单 + 字典
```

> 旧库已存在时，执行 `health/sql/health_upgrade_appointment_setting.sql` 为预约设置表补 `reserved_count` 列即可。

### 2. 启动后端

```bash
cd health/health-admin
mvn spring-boot:run
# 或打包运行
mvn install -pl health-admin -am -DskipTests
java -jar target/health-admin.jar
```

默认地址 `http://localhost:8080`，默认账号 `admin / admin123`。

### 3. 启动前端

```bash
cd health-Vue3-master
npm install
npm run dev
```

访问 `http://localhost:80`（Vite 代理后端）。

## 数据库脚本说明（`health/sql/`）

| 文件 | 用途 |
| --- | --- |
| `ry_20260417.sql` | 若依官方系统表（含建库语句） |
| `health_business_tables.sql` | 12 张业务表：会员/体检记录/预约/预约设置/检查组/检查项/套餐/关联表/AI 会话与消息 |
| `health_business_menu_dict.sql` | 业务菜单（48 条）+ 角色授权 + 字典（health_type / health_sex） |
| `health_upgrade_appointment_setting.sql` | 存量库增量脚本：预约设置表加 `reserved_count` 列 |

## 许可证

基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 二次开发，遵循其开源协议。
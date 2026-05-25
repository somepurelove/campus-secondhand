# 校园二手交易平台

## 项目简介

本项目是吉首大学软件工程课程设计作品，一个面向校园内部的二手物品交易平台，为校园内的学生和教职工提供安全、便捷、高效的二手物品交易渠道，促进校园资源的循环利用。

## 技术栈

- **后端框架**：Spring Boot 2.7.15
- **ORM框架**：MyBatis-Plus 3.5.3.1
- **数据库**：MySQL 8.0
- **前端技术**：原生 HTML/CSS/JavaScript
- **单元测试**：JUnit 5 + Mockito

## 项目结构

```
campus-secondhand/
├── src/
│   ├── main/
│   │   ├── java/com/campus/secondhand/
│   │   │   ├── config/          # 配置类（Security、CORS、异常处理）
│   │   │   ├── controller/      # 控制器层（REST API）
│   │   │   ├── entity/          # 实体类
│   │   │   ├── event/           # 事件类（观察者模式）
│   │   │   ├── listener/        # 监听器
│   │   │   ├── mapper/          # 数据访问层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   │   └── strategy/    # 策略模式实现
│   │   │   └── SecondhandPlatformApplication.java
│   │   └── resources/
│   │       └── application.yml  # 配置文件
│   └── test/                    # 单元测试
├── frontend/                    # 前端代码
│   ├── index.html
│   ├── styles.css
│   ├── script.js
│   ├── api.js
│   └── images/                  # 商品图片
└── pom.xml                      # Maven配置
```

## 核心功能模块

### 1. 用户管理模块
- 用户注册、登录
- 个人信息管理
- 地址管理

### 2. 商品管理模块
- 商品发布、编辑、删除
- 商品审核（审核员）
- 商品搜索、分类浏览
- 图片上传

### 3. 订单管理模块
- 创建订单
- 订单状态管理（待支付、已支付、已发货、已完成、已取消）
- 库存控制

### 4. 评价系统模块
- 交易评价（评分+评论）
- 信用评分计算
- 评价通知

### 5. 纠纷处理模块
- 纠纷提交
- 纠纷仲裁
- 消息通知

## 设计模式应用

| 设计模式 | 应用位置 | 说明 |
|---------|---------|------|
| 策略模式 | CreditScoreStrategy | 信用评分计算支持多种算法 |
| 观察者模式 | ReviewEvent/Listener | 评价后自动更新信用分 |
| 状态模式 | OrderService | 订单状态转换控制 |
| 工厂模式 | Spring IoC | Bean自动创建 |
| 单例模式 | @Service注解 | 服务层单例管理 |

## SOLID原则遵循

- **单一职责原则(SRP)**：每个Service只负责一个模块
- **开闭原则(OCP)**：通过接口扩展，不修改原有代码
- **里氏替换原则(LSP)**：Service实现可替换
- **接口隔离原则(ISP)**：接口职责单一
- **依赖倒置原则(DIP)**：依赖接口而非实现

## 快速开始

### 环境要求
- JDK 1.8+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置
1. 创建数据库：`campus_secondhand`
2. 导入SQL脚本：`campus_secondhand.sql`

### 运行项目
```bash
# 编译运行
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/secondhand-platform-1.0-SNAPSHOT.jar
```

### 前端访问
直接打开 `frontend/index.html` 或使用Live Server

## API接口

后端服务运行在 `http://localhost:8080/api`

主要接口：
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/product/search` - 搜索商品
- `POST /api/product/publish` - 发布商品
- `POST /api/order/create` - 创建订单
- `GET /api/review/user/{userId}` - 查询评价

## 团队成员

| 姓名 | 学号 | 角色 | 职责 |
|-----|------|------|------|
| 陈海涛 | 2022405527 | 需求分析师 | 需求分析、信用评价系统 |
| 曾昭利 | 2023401898 | 系统设计师 | 架构设计、商品管理系统 |
| 刘嘉璐 | 2023402309 | 后端开发 | 后端开发、订单管理系统 |

## 许可证

本项目为课程设计作品，仅供学习交流使用。

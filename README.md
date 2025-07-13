# 个人成绩管理系统 (Personal Grade Management System)

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Java](https://img.shields.io/badge/Java-8+-green.svg)
![Platform](https://img.shields.io/badge/platform-Windows%20%7C%20Linux%20%7C%20macOS-lightgrey)

一个基于Java Swing开发的个人成绩管理系统，支持课程信息管理、多种GPA计算方法和成绩分析。

## 🌟 功能特点

### 📚 课程管理
- 添加、编辑、删除课程信息
- 课程搜索和筛选功能
- 课程分类（必修、选修、通识）
- 专业课标记
- 学期管理
- 自动保存数据

### 🎯 GPA计算
- 标准五分制GPA (5.0制)
- 标准四分制GPA (4.0制)
- 北大算法GPA
- 大工算法GPA
- 加权平均分

### 📊 成绩分析
- 专业课均分统计
- 必修课均分统计
- 选修课均分统计
- 通识课均分统计
- 可视化图表展示

### 💾 数据管理
- Excel文件存储
- 自动保存功能
- 数据导入导出
- 数据验证和错误提示

## 🔧 系统要求

- JDK 8或更高版本
- 内存: 最小256MB
- 磁盘空间: 50MB以上
- 支持的操作系统: Windows, Linux, macOS

## 📥 安装说明

### Windows用户
1. 下载发布版本的`个人成绩管理系统.exe`
2. 双击运行exe文件
3. 确保系统已安装JRE 8或更高版本

### macOS用户
1. 确保已安装Java 8或更高版本
   ```bash
   # 检查Java版本
   java -version
   ```
2. 下载发布版本的JAR文件
3. 在终端中运行：
   ```bash
   java -jar gpa-calculator-1.1.0-jar-with-dependencies.jar
   ```
   
### Linux用户
1. 确保已安装Java 8或更高版本
   ```bash
   # 检查Java版本
   java -version
   ```
2. 下载发布版本的JAR文件
3. 在终端中运行：
   ```bash
   java -jar gpa-calculator-1.1.0-jar-with-dependencies.jar
   ```

### 从源码构建（适用于所有平台）
1. 克隆仓库
   ```bash
   git clone https://github.com/your-username/ScoreServer.git
   ```

2. 进入项目目录
   ```bash
   cd ScoreServer
   ```

3. 使用Maven构建项目
   ```bash
   mvn clean package
   ```

4. 运行生成的JAR文件
   ```bash
   java -jar target/gpa-calculator-1.1.0-jar-with-dependencies.jar
   ```

## 🎮 使用说明

### 添加课程
1. 点击"添加课程"按钮
2. 填写课程信息：
   - 课程名称（必填）
   - 学分（必填，>0）
   - 成绩（0-100）
   - 是否计入GPA
   - 学期
   - 课程类型
   - 是否是专业课

### 编辑课程
1. 右键点击要编辑的课程
2. 选择"修改课程信息"
3. 修改相关信息
4. 点击确定保存

### 成绩筛选
- 使用搜索框快速查找课程
- 使用学期下拉框筛选特定学期
- 使用课程类型筛选特定类型

### 数据管理
- 程序自动保存数据到`score.xlsx`
- 数据文件位于程序运行目录
- 建议定期备份数据文件

## 🏗️ 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── gpa/
│   │           ├── model/
│   │           │   └── Course.java         // 课程实体类
│   │           ├── service/
│   │           │   └── GPACalculator.java  // GPA计算服务
│   │           └── ui/
│   │               └── MainFrame.java      // 主界面
│   └── resources/
│       └── icons/                          // 图标资源
└── test/                                   // 测试目录
```

## 🛠️ 技术栈

- Java 8
- Swing (GUI)
- Apache POI (Excel操作)
- JFreeChart (图表显示)
- Maven (项目管理)
- Launch4j (EXE打包)

## 📝 更新日志

### v1.1.0 (2024-03)
- ✨ 添加专业课标记功能
- 🎨 添加课程分类统计
- 🔧 优化界面布局
- ✨ 添加成绩筛选功能
- 📝 完善项目文档

### v1.0.0 (2024-02)
- 🎉 首次发布
- ✨ 基础课程管理功能
- 📊 GPA计算功能
- 💾 Excel数据存储

## 🚀 待优化项目

1. 数据存储优化
   - [ ] 添加数据库支持
   - [ ] 添加数据备份功能
   - [ ] 添加数据导入导出功能

2. 界面优化
   - [ ] 添加深色模式
   - [ ] 支持界面主题切换
   - [ ] 优化移动设备适配

3. 功能扩展
   - [ ] 添加成绩趋势分析
   - [ ] 支持多用户管理
   - [ ] 添加学习建议功能

## 👥 贡献指南

欢迎提交Issue和Pull Request。在提交PR之前，请确保：

1. 代码符合项目规范
2. 添加必要的测试
3. 更新相关文档
4. 遵循现有的代码风格

## 📄 开源协议

本项目采用MIT协议。详见 [LICENSE](LICENSE) 文件。

## 📞 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交 [Issue](https://github.com/your-username/ScoreServer/issues)
- 邮箱：your-email@example.com

## 🙏 致谢

感谢所有为这个项目提供建议和帮助的人。 
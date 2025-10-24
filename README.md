## <font>项目简介</font>
<font>该项目是专为实验室内部设计的论文管理系统，旨在解决学术文献管理混乱、归纳效率低、分享不便等问题。系统支持论文上传、智能信息提取、分类管理、多格式引用生成等核心功能，结合大语言模型辅助归纳论文创新点与不足，助力科研团队高效管理文献资源。</font>

## <font>核心功能</font>
### <font>📦</font><font> 论文全生命周期管理</font>
+ **<font>上传与解析</font>**<font>：支持 PDF/CAJ/DOCX 格式上传，自动提取标题、作者、期刊等元数据，减少手动录入</font>
+ **<font>详情与编辑</font>**<font>：可视化展示论文核心信息（研究方向、创新点等），支持手动修改与补充</font>
+ **<font>下载与分享</font>**<font>：单文件/批量下载，支持自定义分享权限（仅查看 / 允许下载）与有效期</font>
+ **<font>删除与恢复</font>**<font>：删除论文自动移入回收站（保留 30 天），支持一键恢复或彻底删除</font>

### <font>🧠</font><font> 智能辅助归纳</font>
+ <font>集成大语言模型 API，自动归纳论文研究方向、创新点及可能的不足</font>
+ <font>保留归纳历史版本，支持用户对比优化</font>

### <font>📂</font><font> 分类与搜索</font>
+ <font>多级文件夹分类，支持论文批量移动与多文件夹归属</font>
+ <font>多维度搜索（标题 / 作者 / 关键词 / 研究方向），快速定位目标文献</font>

### <font>📝</font><font> 引用与笔记</font>
+ <font>内置国标 GB/T 7714、APA、IEEE 等常见引用格式，一键生成并复制</font>
+ <font>支持自定义引用模板，满足个性化需求</font>
+ <font>富文本阅读笔记，关联论文永久保存</font>

### <font>👥</font><font> 基础用户管理</font>
+ <font>支持用户创建、密码修改、登录状态管理</font>
+ <font>局域网内轻量使用，无需复杂权限配置</font>

## <font>技术栈</font>
### <font>前端</font>
+ <font>框架：Vue3 + TypeScript</font>
+ <font>构建工具：Vite3</font>
+ <font>UI 组件库：Element Plus</font>
+ <font>状态管理：Pinia</font>
+ <font>路由：Vue Router</font>

### <font>后端</font>
+ <font>语言：Java 17</font>
+ <font>框架：Spring Boot 3</font>
+ <font>认证：Sa-Token</font>
+ <font>ORM：MyBatis-Plus</font>
+ <font>数据库：MySQL 8.0</font>
+ <font>缓存：Redis 6.2</font>
+ <font>连接池：Druid</font>

## <font>快速开始</font>
### <font>环境准备</font>
+ <font>前端：Node.js 22、npm</font>
+ <font>后端：JDK 17、MySQL 8.0、Redis 6.2</font>

### <font>项目克隆</font>
```bash
git clone https://github.com/wzyzxl/thesis_management_system.git
cd thesis_management_system
```

### <font>后端启动</font>
1. <font>导入</font>`<font>sql</font>`<font>目录下的数据库脚本</font>
2. <font>修改</font>`<font>application.yml</font>`<font>配置数据库、Redis 连接信息</font>
3. <font>运行</font>`<font>ThesisManagementSystemApplication.java</font>`<font>启动类</font>

### <font>前端启动</font>
```bash
cd frontend
npm install
npm run dev
```

### <font>访问系统</font>
+ <font>前端地址：</font>[<font style="color:rgb(9, 105, 218);">http://localhost:5173</font>](http://localhost:5173/)
+ <font>初始账号：admin 初始密码：123456</font>

## <font>目录结构</font>
```plain
LabPaperManager/
├── backend/                # 后端项目
│   ├── src/main/java/com/laboratory/paper/
│   │   ├── controller/     # 接口控制层
│   │   ├── service/        # 业务逻辑层
│   │   ├── mapper/         # 数据访问层
│   │   ├── entity/         # 实体类
│   │   ├── domain/         # 实体类
│   │   ├── vo/         		# 实体类
│   │   ├── config/         # 配置类
│   │   └── utils/          # 工具类
│   └── src/main/resources/
│       ├── application.yml # 全局配置
├── frontend/               # 前端项目
│   ├── src/
│   │   ├── components/     # 公共组件
│   │   ├── views/          # 页面组件
│   │   ├── router/         # 路由配置
│   │   ├── store/          # 状态管理
│   │   ├── utils/          # 工具函数
│   │   └── assets/         # 静态资源
│   └── vite.config.ts      # 构建配置
└── images                  # 项目级别图片
└── docs                    # 相关文档
└── sql                     # 数据库脚本
└── LICENSE                 # 许可证
└── README.md               # 项目说明文档
```

## <font>使用说明</font>
### <font>1. 论文上传</font>
1. <font>点击首页「上传论文」按钮，进入上传页面</font>
2. <font>拖拽或选择 PDF/CAJ/DOCX 文件，系统自动提取基础信息</font>
3. <font>补充关键词、归属文件夹等信息，点击「确认上传」</font>
4. <font>上传完成后可在论文列表查看</font>

### <font>2. 论文管理</font>
+ **<font>编辑信息</font>**<font>：进入论文详情页，点击「编辑」按钮修改基础信息及核心归纳内容</font>
+ **<font>分类管理</font>**<font>：在论文列表勾选论文，点击「批量移动」选择目标文件夹</font>
+ **<font>删除操作</font>**<font>：点击「删除」按钮，论文移入回收站，可在回收站恢复或彻底删除</font>

### <font>3. 论文分享</font>
1. <font>在论文详情页点击「分享」按钮</font>
2. <font>选择分享内容（完整论文 / 仅核心内容）、访问权限（仅查看 / 允许下载）、有效期</font>
3. <font>生成分享链接，支持复制链接或下载二维码</font>
4. <font>分享记录可在「分享管理」页面查看与取消</font>

### <font>4. 引用生成</font>
1. <font>进入论文详情页「核心内容」标签页</font>
2. <font>选择目标引用格式（如国标 GB/T 7714、APA）</font>
3. <font>系统自动生成引用文本，点击「复制」即可用于论文写作</font>

## <font>界面展示</font>
### <font>登录页</font>
![](/images/demo/login.png)

### <font>首页（论文列表）</font>
![](/images/demo/home.png)

### <font>论文详情页</font>
![](/images/demo/detail.png)

### <font>上传页面</font>
![](/images/demo/upload.png)

### <font>回收站页面</font>
## ![](/images/demo/recycle_bin.png)

## 文档说明
1. [论文管理系统需求文档](/docs/论文管理系统需求文档.md)
2. [论文管理系统设计文档](/docs/论文管理系统设计文档.md)
3. [论文管理系统API文档](/docs/论文管理系统API文档.md)

## 高保真原型
[墨刀原型](https://modao.cc/proto/MNGmQdODt4etpl0sMJ4Qml/sharing?view_mode=read_only&screen=rbpV0F5APt8nmfUvD)

## <font>许可证</font>
<font>本项目基于</font>[MIT License](/LICENSE)开源，仅供实验室内部学习与使用。</font>
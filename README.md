# SpringbootCreator

一款由Java Springboot框架编写的通用CMS后台管理系统, 该系统能够让您快速搭建一个Java Web的Api应用, 各类需要登录后台的账号管理基础系统, 以及CMS文章资讯类网站等网络应用.

# 框架功能与技术结构如下:

# 后端技术:
* Java17~21, Springboot, MySQL8.0, Mybatis-plus, Freemarker, Im4java(Imagemagick图库功能)

# 前端技术:
* Bootstrap5, JQuery3, Summernote editor

# 核心功能:
* 用户管理, 权限管理, 文章管理, 分类管理, 图片管理, 系统日志等功能

# 功能图片展示:

<img width="1005" height="525" alt="example1" src="https://github.com/user-attachments/assets/11901a4c-350c-4365-a749-7dde6c7411fb" />
<img width="1005" height="466" alt="example3" src="https://github.com/user-attachments/assets/f1d784e7-faf5-4f53-821f-5bb3bae58309" />
<img width="1005" height="462" alt="example4" src="https://github.com/user-attachments/assets/2f81abae-485b-496d-8e37-903b1ce201b2" />
<img width="1005" height="462" alt="example5" src="https://github.com/user-attachments/assets/75f78209-a8ab-4098-9481-195c258d11ef" />
<img width="1005" height="440" alt="example2" src="https://github.com/user-attachments/assets/5970776e-9d2d-4e51-af76-5c7792f8365f" />

# 安装与使用方法:
1. 在MySQL创建名为springbootcreator的数据库, 并导入根目录下的springbootcreator.sql,
2. 使用Idea打开该项目, 找到并打开src/main/resources/application.properties, 修改spring.datasource.username, spring.datasource.password为您的本地MySQL账户密码.
3. 确保根目录/static/uploads具有写入权限.(该文件夹为应用外置文章图片的保存目录, Win10无需操作, 因为默认具有写入权限)
4. 等待Idea的gradle完成包依赖初始化后, 在Idea点击运行SpringbootApplication
5. 使用浏览器访问http://localhost:8080, 顶部导航栏左边有CMS示例文章(可在后台文章管理中删除), 导航栏右边是系统后台登录入口.
6. 管理员账号admin, 密码123456, 权限受限测试账号test, 密码123456

非必要选项
7. 本项目支持Imagemagick图库类的功能, 使用Imagemagick生成缩略图质量要比java标准库的ImageIO要好非常多,但需要您自己安装Imagemagick, 使用该功能需要在CommonUtils类里面找到静态变量USE_IMAGEMAGICK设置为true, 再配置好IMAGEMAGICK_PATH的真实物理路径.
注意: win10因为路径问题仅支持Imagemagick 6.x, 请勿安装7.x， 否则会提示路径错误.

# 郑重提醒:
* 该项目代码仅供学习和测试使用, 如需在生产环境使用, 请自行负责所产生的问题与后果.

# 后端
- 本应用使用springboot快速构建
- 部署流程如下：
    1. 本地使用mvn clean install 命令打包。target目录下会生成zip压缩包，压缩包中包括打包后的jar以及部署脚本。
    2. 上传*.zip压缩包至阿里云服务器。
    3. 服务器解压zip文件，使用bin下serve.sh脚本  sh server.sh start命令即可启动应用。
# 前端
- 安装nodejs(只需安装一次)
- 安装cnpm(只需安装一次)
    - `npm install -g cnpm --registry=https://registry.npm.taobao.org`
- 安装依赖(只需安装一次)
    - 执行`install.sh`
- 开发(监听源码自动构建)
    - 执行`dev.sh`
    - 基于mock数据开发，访问路径：`http://localhost:8081/index-debug.html`
    - 基于后端数据开发，访问路径：`http://localhost:8080/index-debug.html`
- 提交代码前构建
    - 执行`build.sh`，构建生成的前端代码路径：`/src/main/resources/static`

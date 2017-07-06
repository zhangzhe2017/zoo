# 后端
- 本应用使用springboot快速构建
- 部署流程
    - `ssh 47.92.67.9`
    - `cd /root/zoo && git pull && mvn clean install && cd target && mv Connection-1.0-SNAPSHOT-bin.zip /root && cd && rm -rf Connection-1.0-SNAPSHOT && unzip Connection-1.0-SNAPSHOT-bin.zip && cd Connection-1.0-SNAPSHOT/bin && sh server.sh restart`
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

本应用使用springboot快速构建

部署流程如下：

1. 本地使用mvn clean install 命令打包。target目录下会生成zip压缩包，压缩包中包括打包后的jar以及部署脚本。
2. 上传*.zip压缩包至阿里云服务器。
3. 服务器解压zip文件，使用bin下serve.sh脚本  sh server.sh start命令即可启动应用。
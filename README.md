# liunetx
刚学完netty 参考https://github.com/wucao/natx 写个内网穿透工具

## 打包
mvn clean package

## server端启动
### 在带有公网 IP 的服务器上执行 Java 命令:
```java -jar liunetx-server.jar -port 6000 -password password```
## client启动
### 在内网机器上执行 Java 命令:
```java -jar liunetx-client.jar -s_addr 121.37.168.xx -s_port 6000 -password password -p_addr 127.0.0.1 -p_port 3389 -r_port 3389```

参数说明:
- `s_addr` 服务端的网络地址，即服务端运行的服务器外网 IP 或 hostname
- `s_port` 服务端的端口
- `password` 服务端的 password
- `p_addr` 被代理的应用网络地址
- `p_port` 被代理的应用端口号
- `r_port` 服务端对外访问该应用的端口
## http反向代理和负载均衡
```nginx
http {
    upstream echo {
        server 10.15.64.252:8080;
        server 10.15.64.252:8081;
    }

    server {
        server_name proxy.local;
        listen 8080 reuseport;

        location / {
            proxy_pass http://echo;
        }
    }
```

## http流量镜像
```nginx
http {
    upstream echo {
        server 10.15.64.252:8080;
        server 10.15.64.252:8081;
    }

    upstream mirrorecho {
        server 10.15.64.252:8082;
    }

    server {
        server_name proxy.local;
        listen 8080 reuseport;

        location / {
            mirror /mirror;
            proxy_pass http://echo;
        }

        location = /mirror {
            internal;
            proxy_pass http://mirrorecho$request_uri;
        }
    }
```

注意：

1. 镜像服务报错或不可用，不影响主服务的响应时间。
2. 镜像服务响应慢，会影响主服务的响应时间。

参考资料：https://alex.dzyoba.com/blog/nginx-mirror/

## tcp反向代理和负载均衡
```nginx
stream {
    log_format tcp '$remote_addr [$time_local] '
                 '$protocol $status $bytes_sent $bytes_received '
                 '$session_time';

    upstream echotcp {
        server localhost:8091;
        server localhost:8092;
    }
    
    server {
        listen 8090;
        proxy_pass echotcp;
        proxy_connect_timeout 5s;
        access_log logs/tcp_access.log tcp;
    }
}
```

测试结果：

1. 客户端连接时会负载均衡到不同服务端。
2. 客户端连上之后所有的消息会发到同一个服务端。
3. 如果被连接的服务端离线，客户端会收到断线通知，不会自动连到其他服务端。

测试问题：

nginx部署在本地虚拟机中，echo客户端和服务端都部署在本地，echo客户端可以连上nginx代理，但是nginx代理服务连接本地echo服务端。这种方式部署http服务代理是可以的。解决方法是把echo服务端一起部署到本地虚拟机中。

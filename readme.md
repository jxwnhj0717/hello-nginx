# Nginx

## Nginx概述

### Nginx版本

1. 开源版Nginx。
2. 商业版Nginx Plus。
3. 分支版本Tengine。淘宝网技术团队发起。
4. 扩展版本OpenResty。支持Lua。

### Nginx架构

![架构](docs/nginx_architecture.png)

说明：

1. 多个Worker进程。
2. 模块化扩展。



模块化：

1. 核心模块，管理Nginx服务的运行。比如进程管理、cpu亲缘性、内存管理、配置管理、日志管理。
2. 事件模块，负责网络连接处理。
3. HTTP模块，web服务器、反向代理、负载均衡、流量镜像。
4. Mail模块，邮件服务代理。
5. Stream模块，TCP/UDP反向代理、负载均衡、流量镜像。
6. 第三方模块。



## 配置指令

### 配置指令域

1. main，全局域。

2. http，http服务指令域。

3. stream，tcp/udp服务指令域。

4. upstream，代理服务器组。

5. server，定义服务的ip、port等信息。

6. location，对用户的请求访问路由处理。



## 反向代理和负载均衡

[反向代理和负载均衡](docs/reversed_proxy.md)



## 集群管理

1. 配置编辑：Jenkins。
2. 配置归档：Gitlab。
3. 配置发布：Ansible。
4. 日志收集和分析：ELK。
5. 监控：Prometheus+Grafana。



## 其他资料

* [Nginx介绍](https://moonbingbing.gitbooks.io/openresty-best-practices/content/ngx/nginx.html)

* [OpenResty动态更新](https://developer.aliyun.com/article/745757)


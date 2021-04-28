package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EchoServer implements SmartLifecycle {

    @Value("${echo.port}")
    private int port;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ChannelFuture futrue;

    @Override
    public void start() {
        try {
            run();
            log.info("启动echo服务[{}]", port);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void stop() {
        if(workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if(bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if(futrue != null) {
            futrue.cancel(true);
            futrue = null;
        }
    }

    @Override
    public boolean isRunning() {
        return futrue != null;
    }

    private void run() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LineBasedFrameDecoder(1000))
                                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new EchoServerHandler())
                                .addLast(new StringEncoder(CharsetUtil.UTF_8));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        futrue = b.bind(port).sync();
    }
}

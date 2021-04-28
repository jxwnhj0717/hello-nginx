package com.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;

@Component
public class EchoClient {

    private EventLoopGroup group = new NioEventLoopGroup();
    @Getter
    private Channel channel = null;

    public Channel connect(String host, int port) throws InterruptedException {
        if(channel != null) {
            channel.close().sync();
            channel = null;
        }
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        p.addLast(new LineBasedFrameDecoder(1000))
                            .addLast(new StringDecoder(CharsetUtil.UTF_8))
                            .addLast(new EchoClientHandler())
                            .addLast(new StringEncoder(CharsetUtil.UTF_8));
                    }
                });

        // Start the client.
        ChannelFuture future = b.connect(host, port).sync();
        channel = future.channel();
        channel.closeFuture().addListener(f -> {
            channel = null;
        });
        return channel;
    }

    public void disconnect() throws Exception {
        if(channel != null) {
            channel.close().sync();
            channel = null;
        }
    }

    @PreDestroy
    private void destory() {
        group.shutdownGracefully();
    }
}

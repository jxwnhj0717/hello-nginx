package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@SpringBootApplication
public class EchoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchoClientApplication.class, args);
    }

    @Autowired
    private EchoClient client;

    @ShellMethod("connect")
    public void connect(int port) throws InterruptedException {
        client.connect("localhost", port);
    }

    @ShellMethod("connectRemote")
    public void connectRemote(String host, int port) throws InterruptedException {
        client.connect(host, port);
    }

    @ShellMethod("echo")
    public void echo(String msg) {
        client.getChannel().writeAndFlush(msg + "\n");
    }
}

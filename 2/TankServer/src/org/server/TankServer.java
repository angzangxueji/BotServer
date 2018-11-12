package org.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.handler.ChannelInitializerImp;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class TankServer {

    private static final String SERVERPATH = "/home/fanjiaming/项目/2/TankServer/src/org/resources/tanklegendNettyServer.properties";
    private static Map<String, String> configs= new ConcurrentHashMap();
    private String server_host;
    private int server_port;
//    static {
//        configs = new ConcurrentHashMap();
//    }
    public void bind(String host,int port){
        this.server_port=port;
        this.server_host=host;
    }
    public void init() {
        BufferedReader serverIn = null;
        String conf = "";

        try {
            serverIn=new BufferedReader(new FileReader(SERVERPATH));
            while ((conf = serverIn.readLine()) != null) {
                String[] pair = conf.split("=");
                if (pair.length != 2) {
                    throw new Exception("ServerTest config error");
                } else {
                    configs.put(pair[0], pair[1]);
                }
            }
//            System.out.println(configs.get("server_host"));
            bind(configs.get("server_host"),Integer.parseInt(configs.get("server_port")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void  run(){
        EventLoopGroup bossgroup = new NioEventLoopGroup();

        EventLoopGroup workgroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        try {
        server.group(bossgroup, workgroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializerImp());

            ChannelFuture future = null;
            future = server.bind(server_host,server_port).sync();

            System.out.println("等待链接");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

}

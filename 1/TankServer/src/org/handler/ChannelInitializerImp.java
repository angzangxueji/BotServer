package org.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ChannelInitializerImp extends ChannelInitializer<SocketChannel> {
    public ChannelInitializerImp() {

    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast(new HttpRequestDecoder());//inbound
        sc.pipeline().addLast(new HttpObjectAggregator(65536));//inbound
        sc.pipeline().addLast(new HttpResponseEncoder());//outbound

        sc.pipeline().addLast(new WelcomePageHandler());
//        sc.pipeline().addLast(new RegisterPageHandler());
//        sc.pipeline().addLast(new TestHandler());
        sc.pipeline().addLast(new DoHandler());
        sc.pipeline().addLast(new HallPageHandler());
//        sc.pipeline().addLast(new RegisterHandler());
//        sc.pipeline().addLast(new HallHandler());           //处理大厅业务
//        sc.pipeline().addLast(new HouseHandler());
    }
}

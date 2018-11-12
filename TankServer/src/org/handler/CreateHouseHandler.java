package org.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.EventExecutorGroup;
import org.temp.Tempfield;

public class CreateHouseHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr= (FullHttpRequest) msg;
        Tempfield temp=new Tempfield();
        if (fhr.uri().equals("/create")){
            String uid=temp.getUid();

        }
    }
}

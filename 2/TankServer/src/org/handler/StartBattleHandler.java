package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.concurrent.EventExecutorGroup;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class StartBattleHandler extends ChannelInboundHandlerAdapter {
    private int j;
    public StartBattleHandler(int j) {
        this.j=j;
    }
    ChannelInitializerImp ch=new ChannelInitializerImp();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr= (FullHttpRequest) msg;
        if (fhr.uri().equals("/battle_start")){
            System.out.println("-------------->"+fhr.uri());
            j+=1;
            ch.setJ(j);
            System.out.println("jjjjjjjjjjjjjjjjjjjjj"+j);
            String json= "{\"status\":\"1\"}";
            HttpResponse httpresponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("utf-8")));
            httpresponse.headers().set(CONTENT_TYPE, "application/x-javascript; charset=utf-8");
            httpresponse.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) httpresponse).content().readableBytes());
            ctx.writeAndFlush(httpresponse);
        }

        else if (fhr.uri().equals("/start_2")){
            System.out.println("-------------->"+fhr.uri());
            String text= "{\"start\":\"0\"}";
            if (ch.getJ()>=2){
                text="{\"start\":\"1\"}";
            }
            HttpResponse httpresponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(text.getBytes("utf-8")));
            httpresponse.headers().set(CONTENT_TYPE, "application/x-javascript; charset=utf-8");
            httpresponse.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) httpresponse).content().readableBytes());
            ctx.writeAndFlush(httpresponse);
        }

        else {
            ctx.fireChannelRead(msg);
        }

    }
}

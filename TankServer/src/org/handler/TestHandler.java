package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class TestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr= (FullHttpRequest) msg;
        System.out.println(fhr.uri());
        if (fhr.uri().equals("/logmsg")){
                System.out.println(fhr.uri());
                Map parmMap = new HashMap();
                List<InterfaceHttpData> parmList=null;
                System.out.println(fhr.uri());
                HttpPostRequestDecoder decoder=null;
                decoder = new HttpPostRequestDecoder(fhr);
                InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
                parmList = httpdecoder.getBodyHttpDatas();
//            for (InterfaceHttpData parm : parmList) {
//                Attribute data = (Attribute) parm;
//                System.out.println(data);
////                parmMap.put(data.getName(), data.getValue());
//            }
                String loginmsg=null;
                String message=null;
                HashMap map=new HashMap();
                String[] user=null;
                for (InterfaceHttpData l:parmList){
                    loginmsg=String.valueOf(l);
                    message=loginmsg.substring(7,loginmsg.length());
                    System.out.println(message);
                    user=message.split("=");
                    map.put(user[0],user[1]);
                }
            FullHttpResponse response=null;
            String json="1";
                if (map.get("account")=="123"){
                    try {
                        response = new DefaultFullHttpResponse(HTTP_1_1,
                                OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
                        response.headers().set(CONTENT_TYPE, "text/plain");
                        response.headers().setInt(CONTENT_LENGTH,
                                response.content().readableBytes());
//                        System.out.println("获取请求：\n"+msg+"\n");
                        ctx.writeAndFlush(response);
                        System.out.println(json);
//            System.out.println("写出响应");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
//            System.out.println(fhr.uri());
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
}

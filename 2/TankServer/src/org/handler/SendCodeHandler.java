package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import org.dao.MybatisTest;
import org.dao.UserDao;
import org.temp.DealStrSub;
import org.temp.Tempfield;

import java.util.HashMap;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class SendCodeHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        MybatisTest player = new MybatisTest();
        List<InterfaceHttpData> parmList = null;
        DealStrSub d=new DealStrSub();
        String json = null;
        DefaultFullHttpResponse response = null;
        HttpPostRequestDecoder decoder = null;
        if (fhr.uri().equals("/code")){
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
            String registmsg = null;
            String message = null;
            HashMap map = new HashMap();
            String[] user = null;
            for (InterfaceHttpData l : parmList) {
                registmsg = String.valueOf(l);
                message = registmsg.substring(7, registmsg.length());
                System.out.println(message);
                user = message.split("=");
                map.put(user[0], user[1]);
            }
            //获取到前端提交的代码
            d.fire((String) map.get("code"));
            d.path((String) map.get("code"));

            json = "{\"status\":\"1\"}";
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            System.out.println("code-------------->" + json);
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
}

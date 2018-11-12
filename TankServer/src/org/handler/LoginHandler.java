package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import org.dao.MybatisTest;
import org.dao.UserDao;
import org.temp.Tempfield;

import java.util.HashMap;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class LoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        MybatisTest player = new MybatisTest();
        List<InterfaceHttpData> parmList = null;
        String json = null;
        UserDao userDao=new UserDao();
        Tempfield temp=new Tempfield();
        DefaultFullHttpResponse response = null;
        HttpPostRequestDecoder decoder = null;
        if (fhr.uri().equals("/log")) {
            System.out.println(fhr.uri());
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
            String passwd = player.findCustomerTest((String) map.get("account"));
//            System.out.println("passwd----------------->" + passwd);
            if (passwd.equals(map.get("password"))) {
                json = "{\"status\":\"1\"}";
                temp.setUid((String) map.get("account"));
                temp.setUname(userDao.search((String) map.get("account")));
                System.out.println("login----------------->"+userDao.search((String) map.get("account")));
            } else {
                json = "{\"status\":\"0\"}";
            }
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            System.out.println("response-------------->" + json);
//            response.release();
            response.retain();
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}

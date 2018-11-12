package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;

import org.dao.MybatisTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
/////接受登录请求并且返回参数
public class DoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr = (FullHttpRequest) msg;
        HttpMethod method = fhr.method();
        String uri=fhr.uri();
        MybatisTest player = new MybatisTest();
        List<InterfaceHttpData> parmList = null;
        String json = "{\"status\":\"0\"}";

        DefaultFullHttpResponse response = null;
        if (fhr.uri().equals("/log")) {
            HttpPostRequestDecoder decoder = null;

            System.out.println(fhr.uri());
            System.out.println(fhr.uri());
//            QueryStringDecoder uriDecoder = new QueryStringDecoder(fhr.uri());t
//            Map<String, List<String>> parameters = uriDecoder.parameters();
//            System.out.println(uriDecoder.parameters());
//            String path = uriDecoder.path();
//            String mURI = uriDecoder.uri();
//
//            if (path.startsWith("//")) {
//                path = path.substring(1);
//            }
//            System.out.println(path);
//            QueryStringDecoder decoder1 = new QueryStringDecoder(fhr.uri());
//            System.out.println( decoder1.parameters());
            System.out.println("------------");
            System.out.println(fhr);
            decoder = new HttpPostRequestDecoder(fhr);

            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            System.out.println("---"+httpdecoder);
            parmList = httpdecoder.getBodyHttpDatas();
            String registmsg = null;
            String message = null;
            HashMap map = new HashMap();
            String[] user = null;
            for (InterfaceHttpData l : parmList) {///????
                registmsg = String.valueOf(l);
                message = registmsg.substring(7, registmsg.length());
                System.out.println(message);
                user = message.split("=");
                map.put(user[0], user[1]);  ///将account=123和passwd=xxx写到，map里面
            }
            String passwd = player.findCustomerTest((String) map.get("account"));
//            System.out.println("passwd----------------->" + passwd);
            if (passwd.equals(map.get("password"))) {
                json = "{\"status\":\"1\"}";
            } else {
                json = "{\"status\":\"0\"}";
            }
            //讲json放入响应头中
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            System.out.println("response-------------->" + json);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}

package org.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import org.dao.Connector;
import org.dao.MybatisTest;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest  fhr= (FullHttpRequest) msg;
        Connector conn=new Connector();
        if ((fhr.uri()).equals("/registmsg")){
            System.out.println(fhr.uri());
            List<InterfaceHttpData> parmList=null;
            System.out.println(fhr.uri());
            HttpPostRequestDecoder decoder=null;
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
//            for (InterfaceHttpData parm : parmList) {
//                Attribute data = (Attribute) parm;
//                parmMap.put(data.getName(), data.getValue());
//            }
            String registmsg=null;
            String message=null;
            HashMap map=new HashMap();
            String[] user=null;
            for (InterfaceHttpData l:parmList){
               registmsg=String.valueOf(l);
               message=registmsg.substring(7,registmsg.length());
                System.out.println(message);
                user=message.split("=");
                map.put(user[0],user[1]);
            }
            String uid= (String) map.get("account");
            String uname= (String) map.get("username");
            String upasswd= (String) map.get("password");
            String utel= (String) map.get("phone_number");

            String sql="select hid,hcount from house";
            Statement statement=conn.connect().createStatement();
            ResultSet rs=statement.executeQuery(sql);

            MybatisTest player=new MybatisTest();
            player.addCustomer(uid,uname,upasswd,utel);
            System.out.println("insert success");

        }else{
            ctx.fireChannelRead(msg);
        }

    }
}

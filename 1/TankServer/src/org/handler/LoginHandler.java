package org.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.dao.MybatisTest;
import org.pojo.User;

public class LoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("准备接受信息");
        ByteBuf result = (ByteBuf)msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        System.out.println("assda");
        String resultstr = new String (result1);
        System.out.println("读取到的账户和密码:"+resultstr);

        //将用户的账号的密码拆开
        String account= "lisi";
        String password= "123";

        MybatisTest m  = new MybatisTest();
        String password1 = m.findCustomerTest(account);//根据数据库的账号找到密码；

        User u = m.findCustomerTest1(account); //获取玩家的名字
        String cname = u.getUname();

        String response ="";
        if (password.equals(password1)) {//检验账号密码是否正确
            System.out.println("账号密码正确");
            response = "true";
            ByteBuf encoded = ctx.alloc().buffer(4*response.length());
            encoded.writeBytes(response.getBytes());
            ctx.write(encoded);
        }else{
            response = "false";
            ByteBuf encoded = ctx.alloc().buffer(4*response.length());
            encoded.writeBytes(response.getBytes());
            ctx.write(encoded);
        }
        ctx.writeAndFlush(msg);
    }
}

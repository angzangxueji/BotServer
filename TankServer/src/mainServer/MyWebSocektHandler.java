package mainServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.handler.DoHandler;
import org.handler.HallPageHandler;
import org.handler.WelcomePageHandler;
import service.Service;
import service.serviceImp.NewServiceImpl;
import service.serviceImp.ServiceImpl;

import java.util.Date;


public class MyWebSocektHandler extends ChannelInboundHandlerAdapter {
    private WebSocketServerHandshaker handshaker;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler added:"+ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler removed:"+ctx.channel().id().asLongText());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("新连接进入:"+ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端链接关闭");
    }
    //接受，处理，响应的方法,服务端处理客户端websocket请求的方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, msg);

        }else if (msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx,(WebSocketFrame)msg);
        }
    }
    /*处理客户端向服务端发起http握手请求的业务
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx, Object msg) throws Exception {
        //客户端向服务端建立http请求时会带有一些特殊的请求头    所以用 Upgrade的websocket去比较
        FullHttpRequest req = (FullHttpRequest) msg;
        if (!req.decoderResult().isSuccess()||(!"websocket".equals(req.headers().get("Upgrade")))){
//            WelcomePageHandler s = new WelcomePageHandler();
//            s.channelRead(ctx,req);
            DoHandler d = new DoHandler();
            d.channelRead(ctx,msg);
            HallPageHandler p = new HallPageHandler();
            p.channelRead(ctx,msg);
            System.out.println("解析http");

            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory=new WebSocketServerHandshakerFactory("ws://localhost:9999/ws",null,false);
        handshaker=webSocketServerHandshakerFactory.newHandshaker(req);
        if (handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            //向客户端发送握手，完成握手
            ChannelFuture channelFuture=handshaker.handshake(ctx.channel(),req);
            if (channelFuture.isSuccess()){
                Service service=new ServiceImpl(ctx,req);
                //将上下文和请求发送给业务层启动线程去工作
                new Thread(service).start();
            }

       }
    }

    //服务端向客户端响应消息
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame wsframe) {
        //判断是否关闭websocket指令
        if (wsframe instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame) wsframe.retain());
            System.out.println("flag1");
            return;
        }
        //判断是否ping消息
        if (wsframe instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(wsframe.content().retain()));
            System.out.println("flag2");
        }
        //判断是否二进制数据
        if (!(wsframe instanceof TextWebSocketFrame)){
            System.out.println("flag3");
            throw new UnsupportedOperationException(String.format("%s frame types not supported",wsframe.getClass().getName()));
        }

        Service service=new NewServiceImpl(ctx,wsframe);
        new Thread(service).start();
        String request = ((TextWebSocketFrame)wsframe).text();
        System.out.println("服务端收到客户端的消息=====>>"+request);
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString()+ctx.channel().id()+"===>"+request );
        //群发，服务端向每个连接上来的客户端群发信息
        ctx.channel().writeAndFlush(tws);

    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
        if (res.status().code()!=200){ //请求失败的处理信息
            ByteBuf byteBuf= Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(byteBuf);
            System.out.println("返回信息，关闭1");
            byteBuf.release();
//            HttpUtil.setContentLength(res,res.content().readableBytes());
        }
        //服务端向客户端发送数据
        ChannelFuture channelFuture=ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req)||res.status().code()!=200){
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

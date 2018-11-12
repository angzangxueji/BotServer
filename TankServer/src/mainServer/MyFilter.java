package mainServer;

//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.DefaultFullHttpRequest;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.HttpObject;
//import io.netty.handler.codec.http.HttpRequest;
//import server.control.PageControl;
//import server.serverUtils.StrHandleUtil;
//
//public class MyFilter extends SimpleChannelInboundHandler<HttpObject> {
//    private  void doDispatcher(){
//
//    }
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//        if (msg instanceof HttpRequest) {
//            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
//            String uri = fullHttpRequest.uri();
//            StrHandleUtil strHandleUtil = new StrHandleUtil(uri);
//            System.out.println(strHandleUtil.getUri());
//            System.out.println(strHandleUtil.getParameter("method"));
//            System.out.println(strHandleUtil.getParameter("name"));
//            System.out.println(strHandleUtil.getParameter("pass"));
//            System.out.println(strHandleUtil.getParameters());
//            PageControl pageControl = new PageControl();
//            pageControl.dispatcher(uri);
//        }
//    }
//}

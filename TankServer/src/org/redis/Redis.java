package org.redis;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Redis extends ChannelHandlerAdapter {
    private ConnectionPool connectionPool;
    public Redis(ConnectionPool connectionPool){
        this.connectionPool=connectionPool;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg){
        JSONObject jsonObject = (JSONObject)msg;
        String json="";
        String usrscore = (String)jsonObject.get("usrscore");
        String usrid = (String)jsonObject.get("usrid");
        Double score = (Double)jsonObject.get("score");
        Jedis jedis=connectionPool.getConnection();
        jedis.zadd(usrscore,score,usrid);
        jedis.zrange(usrscore,0,-1);
        Set<Tuple> tuples = jedis.zrangeWithScores("usrscore",0,-1);

        List<String> list=new ArrayList<>();
        for (Tuple tuple : tuples) {
            String str=(tuple.getElement()+":"+tuple.getScore());
            list.add(str);
            System.out.println(str);
        }


//        Iterator iterator=list.iterator();
//        while (iterator.hasNext()){
//            for (Iterator iter = list.iterator(); iter.hasNext();) {
//                String str = (String)iter.next();
//                System.out.println(str);
//            }
//        }
        String result2= JSON.toJSONString(list);
        System.out.println(result2);
        ctx.write(result2);

    }

    public static void main(String[] args) {
        ConnectionPool connectionPool = new ConnectionPool();
        Redis redis = new Redis(connectionPool);
    }
}

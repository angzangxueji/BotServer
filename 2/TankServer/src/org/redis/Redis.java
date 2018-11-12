package org.redis;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import jdk.nashorn.internal.parser.JSONParser;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Phaser;

public class Redis extends ChannelHandlerAdapter {
    private ConnectionPool connectionPool;
    public Jedis jedis;

    public Redis(ConnectionPool connectionPool, Jedis jedis) {
        this.connectionPool = connectionPool;
        this.jedis = jedis;
    }

//     public void Redisinit(JSONArray jsonArray){
//         String usrscore="usrscore";
//         List<String> list=new ArrayList<>();
//
//         ///判断是否存在
//         if (jedis.exists(usrscore)) {
//
//             SortRedis();
//         }else {
//             SortRedisMysql(jsonArray);
//         }
//
//         String result2= JSON.toJSONString(list);
//         System.out.println(result2);
//     }
     //将数据库中的内容传入redis
public void SortRedisMysql(JSONArray jsonArray){
    String usrscore="usrscore";
    JSONObject jsonObject ;
    List<String> list=new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
        jsonObject = (JSONObject) jsonArray.get(i);
        String usrid = (String) jsonObject.get("username");
        int score = Integer.parseInt((String) jsonObject.get("score"));   ///将json转换为String再转换为Integer
        jedis.zadd(usrscore, score, usrid);

    }

    ///在redis里排序
    jedis.zrevrange(usrscore,0,-1);
    Set<Tuple> tuples = jedis.zrevrangeWithScores(usrscore,0,-1);

    for (Tuple tuple : tuples) {
        String str=(tuple.getElement()+":"+tuple.getScore());
        list.add(str);
        System.out.println(str);
    }
}
    public void SortRedis(){
        String usrscore="usrscore";
        JSONObject jsonObject ;
        List<String> list=new ArrayList<>();
        jedis.zrevrange(usrscore,0,-1);  ///从大到小排序    zrange是从小到大排序
        Set<Tuple> tuples = jedis.zrevrangeWithScores(usrscore,0,-1);

        for (Tuple tuple : tuples) {
            String str=(tuple.getElement()+":"+tuple.getScore());
            list.add(str);
            System.out.println(str);
        }
    }
    public static void main(String[] args) {
        ConnectionPool connectionPool = new ConnectionPool();
         Jedis jedis=connectionPool.getConnection();

//        sql s = new sql();
//        JSONArray jsonArray = s.JsonTest();
        Redis r = new Redis(connectionPool,jedis);

    }
}

package org.redis;


import java.sql.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class sql {

    public Connection getConnection(){

        String driver = "com.mysql.jdbc.Driver";
//这里我的数据库名字是Person，改成你自己的数据库名
        String url = "jdbc:mysql://localhost:3306/Spring";
        String user = "root";
        String pwd = "1234567";
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public JSONArray JsonTest()  {


        JSONArray array = new JSONArray();
       try {
            Connection con = getConnection();
            Statement stet = con.createStatement();

            String sql = "select * from UserAnd";

             ResultSet rs = stet.executeQuery(sql);
           /*从元数据获得列数*/
            ResultSetMetaData metaData =rs.getMetaData();
            int columnCount= metaData.getColumnCount();

            while(rs.next()){
              JSONObject jsonObj = new JSONObject();
              for(int i = 1; i <= columnCount;i++)
              {
                String columnName = metaData.getColumnLabel(i);  ///返回此列暗含的标签
                String value =rs.getString(columnName);  ///获得字符串
                jsonObj.put(columnName, value);
              }
             array.add(jsonObj);    ///将生成的json加入到json数组中
            }
        System.out.println("转换JSON数据：");
        System.out.println(array.toString());
        con.close();

         } catch (Exception e) {
           e.printStackTrace();// TODO: handle exception
         }
       return array;
    }

    public void  UpdateMysql(String username,int uscore){
        String sql="update UserAnd set score=score+"+uscore+" where username="+"'"+username+"'";//生成一条mysql语句
        System.out.println(sql);
        Connection con = getConnection();
        Statement stet = null;
        try {
            stet = con.createStatement();
            stet.executeUpdate(sql);//执行SQL语句
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("修改数据库成功");
    }

    public static void main(String[] args) {
        sql s = new sql();
        String username = "www";
        s.UpdateMysql(username,5);
    }

}


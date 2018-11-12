package org.dao;

import org.pojo.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

    private User user=new User();
    private Connector conn=new Connector();
    private Statement sm=null;
    private ResultSet rs=null;
    //查询大厅显示玩家的名字，把前端接收到json取出来uid
    public String search(String uid){
        String uname=null;
        try {
        String sql="select uname from player where uid=?";
        PreparedStatement psm=conn.connect().prepareCall(sql);
        psm.setString(1,uid);
        ResultSet rs=psm.executeQuery();
        while (rs.next()) {
            uname = rs.getString("uname");
        }
        psm.close();
        conn.connect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uname;
    }

}

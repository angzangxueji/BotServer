package mainServer;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class DealStrSub {
    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param soap
     * @return
     */
    public static List<String> getSubUtil(String soap,String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }

    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样

     */
    public static String getSubUtilSimple(String soap,String rgex){
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(1);
        }
        return "";
    }
    public static int fire(String code)
    {
        String rgex = "var fire_choose=(.*?);";
        String str = getSubUtilSimple(code,rgex);
        int fire_value=parseInt(str);
        return fire_value;
    }
    public static int path(String code)
    {
        String rgex = "var path_choose=(.*?);";
        String str = getSubUtilSimple(code,rgex);
        int path_value=parseInt(str);
        return path_value;
    }


    public static void main(String[] args) {

        String sss = "var fire_choose=0;\n" +
                "                     function fire(fire_choose) {\n" +
                "                         var a=fire_choose;\n" +
                "                         if (tank.alive === true) {\n" +
                "                           if (game.time.now > nextFire && bullets.countDead() > 0) {\n" +
                "                                  nextFire = game.time.now + fireRate;\n" +
                "\n" +
                "                                  var bullet = bullets.getFirstExists(false);\n" +
                "\n" +
                "                                  bullet.reset(turret.x, turret.y);\n" +
                "                             switch (a) {\n" +
                "\n" +
                "                                case 0:\n" +
                "                                    //炮筒朝敌方坦克射击\n" +
                "                                    bullet.rotation = game.physics.arcade.moveToObject(bullet,EnemyTank.tank, 1000);\n" +
                "                                    turret.rotation= this.game.physics.arcade.angleBetween(tank, EnemyTank.tank);\n" +
                "                                   break;"+"var path_choose=1;";

        System.out.println(fire(sss));
        System.out.println(path(sss));
    }
}

import redis
from apiTankcode.sendServerTankCode import Tank
class redisUtil:
    @staticmethod
    def getConnections():
        # re = redis.Redis(host="127.0.0.1", port=6379,db=0)
        # 设置最大连接数，链接参数 以及连接类。
        re1=redis.ConnectionPool(host="127.0.0.1", port=6379,db=0)
        rdc = redis.StrictRedis(connection_pool=re1)
        return rdc
    @staticmethod
    def getTankInfo(username):
        connect=redisUtil.getConnections()
        userTankInfo=connect.hgetall(username)
        # print(userTankInfo[b'HP'].decode("utf-8"))
        print(userTankInfo)

        tankinfo=Tank(userTankInfo[b'HP'].decode("utf-8"),userTankInfo[b'shotRange'].decode("utf-8")
                      ,userTankInfo[b'username'].decode("utf-8"),userTankInfo[b'fire'].decode("utf-8"))

        print(tankinfo)
        return tankinfo


    @staticmethod
    def FireResult(username,choose):
        connect = redisUtil.getConnections()
        if choose.__eq__(True):
            otherPlayer=connect.hget("roomid",username).decode("utf-8")
            myFire=int(connect.hget(username,"fire").decode("utf-8"))
            # myHP=connect.get(username,"HP")
            otherPlayerHP=int(connect.hget(otherPlayer,"HP").decode("utf-8"))
            if otherPlayerHP-myFire>0:
                otherPlayerNewHP=otherPlayerHP-myFire
            else:
                otherPlayerNewHP=0
            connect.hset(otherPlayer, "HP", otherPlayerNewHP)
            print("fire result is True.")
        elif choose.__eq__(False):
            print("fire result is False.")
        else:
            print("fire result is error.")

    '''
    及时的修改tank的坐标。
    步骤：
    1.获取tank在地图中的index（New/Old）
    2.更新地图信息，以及用户对应的信息。
    '''
    @staticmethod
    def ModifyLocalSpot(tank,oldLocation):
        connect = redisUtil.getConnections()
        x=int(tank.getLocalspot()[0])
        y=int(tank.getLocalspot()[1])
        newindex=(x-1)*5+y

        username=tank.getUserName()
        roomid=connect.hget("mapinfo",username)

        connect.hset("room_id_list",username,newindex)#更新room_id_list用户的内容。

        mytankOldRecord=connect.hget(roomid,oldLocation).decode("utf-8")
        mytankOldRecord=eval(mytankOldRecord)
        print("====================mytankNewRecord before"+mytankOldRecord.__str__())
        print(oldLocation)
        print(newindex)
        mytankOldRecord['player']=newindex  #更新map中tank最新数据的数据。
        connect.hset(roomid,oldLocation,mytankOldRecord)
        print("====================mytankNewRecord after" + mytankOldRecord.__str__())

        mytankNewRecord = connect.hget(roomid, newindex).decode("utf-8")
        print("====================mytankNewRecord before"+mytankNewRecord.__str__())
        mytankNewRecord = eval(mytankNewRecord)
        mytankNewRecord['player'] = 0  # 还原map中tank的地图的数据。
        connect.hset(roomid,newindex,mytankNewRecord)
        print("====================mytankNewRecord after" + mytankNewRecord.__str__())



'''
在ConnectionPool的实例中, 有两个list, 依次是_available_connections, _in_use_connections,
分别表示可用的连接集合和正在使用的连接集合, 在上面的get_connection中, 我们可以看到获取连接的过程是
    1.从可用连接集合尝试获取连接,
    2.如果获取不到, 重新创建连接
    3.将获取到的连接添加到正在使用的连接集合
'''

# tankinfo=redisUtil.getTankInfo("qiao12223")
# connect = redisUtil.getConnections()
# aaa=connect.hget("50",1).decode("utf-8")
# data=eval(aaa)
# data['player']=0
# print(data)
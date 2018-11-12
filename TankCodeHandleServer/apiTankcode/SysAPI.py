import json

from config.redisUtil import redisUtil


class SysAPI:
    def shoot(self):
        print("HP down ")
    def SpeedUp(self):
        print("speed up")
    def SpeedDown(self):
        print("speed down")

    '''
    扫描器：
        args: scanner 扫描器id
              mylocaltion 该玩家tank的location
              roomid 房间号（地图的id）
              redis redis连接2
    '''
    def Scanner(self,scanner,username,roomid,redis):
        wide=5
        mylocaltion=redis.hget("room_id_list",username).decode("utf-8")
        tempMyLocation=int(mylocaltion)
        print(str(tempMyLocation)+"========")
        loadList = []
        mytankLocation = []
        otherTankLocation = []
        fortList = []
        for j in range(-scanner, scanner + 1):
            print("===============" + str(j))
            for i in range(-scanner, scanner + 1):
                if tempMyLocation % 5 == 0:
                    q = tempMyLocation + wide * j + i
                    if q <= 0 or q > 25:
                        continue
                    left = (int(tempMyLocation / wide) + j - 1) * wide + 1
                    right = left + wide - 1
                    if left <= q <= right and q != tempMyLocation:
                        print(str(q) + " left:" + str(left) + " right:" + str(right))
                        # print(q)
                        record = redis.hget(roomid, q)
                        # print("-------------------1")
                        # print(type(record), record)
                        aaa = record.decode("utf-8")[0:str(record.decode("utf-8")).__len__()]
                        recordDict = eval(aaa)
                        # print(recordDict['id'])  # 获取是否有player 0=>null 1=>player1 2=>player2   {'player':'1','result':'3'}(player,T/F)  3=>winner 4=>loser
                        # print("-------------------2")
                        result = self.__toList__(recordDict, loadList, mytankLocation, otherTankLocation, fortList)
                        loadList.append(result['loadList'])
                        tankList = result['otherTankLocation']
                        fortList = result['fortList']
                        MyLocation = result['mytankLocation']
                else:
                    q = tempMyLocation + wide * j + i
                    if q <= 0 or q > 25:
                        continue
                    # print(q)
                    left = (int(tempMyLocation / wide) + j) * wide + 1
                    right = left + wide - 1
                    if left <= q <= right and q != tempMyLocation:
                        print(str(q) + " left:" + str(left) + " right:" + str(right))
                        record = redis.hget(roomid, q)
                        # print("-------------------1")
                        # print(type(record), record)
                        aaa = record.decode("utf-8")[0:str(record.decode("utf-8")).__len__()]
                        recordDict = eval(aaa)
                        # print(recordDict['id'])  # 获取是否有player 0=>null 1=>player1 2=>player2   {'player':'1','result':'3'}(player,T/F)  3=>winner 4=>loser
                        # print("-------------------2")
                        result = self.__toList__(recordDict, loadList, mytankLocation, otherTankLocation, fortList)
                        loadList.append(result['loadList'])
                        tankList = result['otherTankLocation']
                        fortList = result['fortList']
                        MyLocation = result['mytankLocation']
        #去掉一些奇怪的东西。
        mylist = []
        for i in range(0, len(loadList), 2):
            mylist.append(loadList[i])
        return (mylist, tankList, fortList, MyLocation)



    def __toList__(self,ScannerPlayer,loadList,mytankLocation,otherTankLocation,fortList):
        if ScannerPlayer['player'].__eq__("0"):
            if ScannerPlayer['type'].__eq__("fort"):
                fortList.append(ScannerPlayer['id'])
            elif ScannerPlayer['type'].__eq__("load"):
                loadList.append(ScannerPlayer['id'])

            else:
                print("ScannerPlayer['player'].__eq__('0') is error. ")
        elif ScannerPlayer['player'].__eq__("1"):
            mytankLocation.append(ScannerPlayer['id'])
        elif ScannerPlayer['player'].__eq__("2"):
            otherTankLocation.append(ScannerPlayer['id'])
        else:
            print(ScannerPlayer['player']['playerone'])
        result={"loadList":loadList,"mytankLocation":mytankLocation,"otherTankLocation":otherTankLocation,"fortList":fortList}
        # print(result)
        return result

# a=SysAPI()
# a.Scanner(1,8,1,1)
import json
# a=b"MapLoad{'room_id'='30', 'id'=12, 'type'='load', 'map_id'='2','isOtherPlayer'=False}".decode("utf-8")
# str1=str(a,encoding="utf-8")
# data=eval(str1)
# b=a.replace("=",":")
# print(b)
#
# c=b[7:b.__len__()]
# print(c)
# a=eval(c)
# print(a["isOtherPlayer"])

# params = json.loads(b)
# print(params)

# print(c.__dict__)

# import json
#
# str = '{"params":{"id":222,"offset":0},{"nodename":"topic"}'
#
# params = json.loads(str)
#
# print (params['params']['id'])
# aaa={'playerone':'1','result':'3'}
# print(aaa['playerone'])
MySystemAPI = SysAPI()
redisConnection =redisUtil.getConnections()
(loadList, tankList, fortList, MyLocation)=MySystemAPI.Scanner(1,"qiao13", "50", redisConnection)
print("====================")
# mylist=[]
# for i in range(0,len(loadList),2):
#      mylist.append(loadList[i])
# print(mylist)
print(loadList)
print("====================")

#!/usr/bin/python
import json

from apiTankcode.sendServerTankCode import MyTank, ReponseTank


def TankTemplate(tank,name):
    # 传入动态的用户tankcode modules 和方法名 ---->也就是说前端只能写一些函数，
    # 我这这调用这些函数
    # clsname = "jiaoben111"
    # method1 = "myCodeHP"
    clsname = name
    method1 = "myCodeHP1"

    obj = __import__(clsname)  # import module and classname
    c = getattr(obj, clsname)
    obj = c()  # new class
    print(obj)
    # obj.echo()
    mtd = getattr(obj, method1)(tank)
    # mtd() # call def
    tank.setDirection(mtd)
    reponsetank =ReponseTank(tank.getLocalspot(),tank.getDirection())
    print(reponsetank.__dict__)
    myClassDict=reponsetank.__dict__
    myClassJson=json.dumps(myClassDict)
    print(myClassJson)
    return myClassJson
    # return tank

def TankTemplate2(username,tankMethod,mytank):
    clsname = username
    method1 = tankMethod
    obj = __import__(clsname)
    result = getattr(obj, method1)(mytank)
    # obj = c()
    # print(obj)
    return result


# mytank = MyTank(50, [5, 1], "left", [True, [7, 8]])
# mytank=TankTemplate(mytank)
# print("=============")
# print(mytank.__str__())
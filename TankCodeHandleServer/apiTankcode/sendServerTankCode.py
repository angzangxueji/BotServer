from enum import IntEnum, unique


@unique
class Direction(IntEnum):
    up=1
    down=2
    left=3
    right=4


"""
把tank的行驶状态进行封装
    正常行驶
    攻击敌人
   s 远离敌人
"""
@unique
class MoveStatus(IntEnum):
    NormalDriving=1
    Attack=2
    Escape=3

class Environment:
    def __init__(self,isExistTank,localspot):
        self.__isExistTank=isExistTank
        self.__localSpot=localspot

    def __str__(self):
        return "isExistTank:"+str(self.__isExistTank)+" localspot:"+str(self.__localSpot)

    def getIsExistTank(self):
        return self.__isExistTank

    def getLocalSpot(self):
        return self.__localSpot


class Tank:
    def __init__(self,HP,scanningRange,username,fire):
        self.__HP=HP
        self.__scanningRange=scanningRange
        self.__localspot=[]
        self.__username=username
        self.__fire=fire

    def __str__(self):
        return "{" \
               "HP:"+str(self.__HP)+",scanningRange:"+str(self.__scanningRange)+\
               ",localspot:"+str(self.__localspot)+",username:"+str(self.__username)+\
               ",fire:"+str(self.__fire)+\
               "}"

    def getHP(self):
        return self.__HP
    def setHP(self,HP):
        self.__HP=HP
    def getScanningRange(self):
        return self.__scanningRange
    def setScanningRange(self,scanningRange):
        self.__scanningRange=scanningRange
    def __Scanner__(self):
        return self.getScanningRange()
    def isFire(self,list):
        if int(self.getScanningRange())<int(TankUtils.getListDistance(self,list)):
            return True
        else:
            return False
    def getUserName(self):
        return self.__username
    def getLocalspot(self):
        return self.__localspot

    def setLocalspot(self,localspot):
        self.__localspot=[]
        self.__localspot.append(localspot[0])
        self.__localspot.append(localspot[1])


class MyTank(Tank):
    def __init__(self,HP,scanningRange,localspot,direction,environment):
        super().__init__(HP,scanningRange)
        self.__localspot=localspot
        self.__direction=direction
        self.__environment=Environment(environment[0],environment[1])



    def __str__(self):
        return super().__str__()+" direction: "+str(self.__direction)+"   environment: "+\
               self.__environment.__str__()+"   localspot: "+str(self.__localspot)

    def getLocalspot(self):
        return self.__localspot

    def setLocalspot(self,localspot):
        self.__localspot[0]=localspot[0]
        self.__localspot[1]=localspot[1]


    def getDirection(self):
        return self.__direction

    def setDirection(self,direction):
        self.__direction=direction

    def getEnvironment(self):
        return self.__environment

    def setEnvironment(self,environment):
        self.__environment=Environment(environment[0],environment[1])

    def getDistance(self):
        return TankUtils.Distance(self.getLocalspot(),self.getEnvironment().getLocalSpot())

    def isFire(self,list):
        if self.getScanningRange()<TankUtils.getListDistance(self,list):
            return "ture"
        else:
            return "flase"


class ReponseTank(MyTank):
    def __init__(self,localspot,direction):
        self.localspot=localspot
        self.direction=direction

    def getLocalspot(self):
        return self.localspot

    def setLocalspot(self,localspot):
        self.localspot=localspot

    def getDirection(self):
        return self.direction

    def setDirection(self,direction):
        self.direction=direction
    def __str__(self):
        return " direction: "+str(self.direction)+"   localspot: "+str(self.localspot)



from math import sqrt
class TankUtils:
    @staticmethod
    def Distance(myTankLocalspot,otherTankLocalspot):
        x=abs(myTankLocalspot[0]-otherTankLocalspot[0])
        y=abs(myTankLocalspot[1]-otherTankLocalspot[1])
        return sqrt(x*x+y*y)

    @staticmethod
    def getDistance(self,mytank,otherTankLocalspot):
        x=mytank.getLocalspot()[0]-otherTankLocalspot[0]
        y=mytank.getLocalspot()[1]-otherTankLocalspot[1]
        return sqrt(x*x+y*y)

    @staticmethod
    def getListDistance(self,list):
        x=list[0]
        y=list[1]
        return sqrt(x * x + y * y)

    @staticmethod
    def GetDirection(myTankLocalspot,otherTankLocalspot):
        x = myTankLocalspot[0] - otherTankLocalspot[0]
        y = myTankLocalspot[1] - otherTankLocalspot[1]
        list=[]
        list.append(x)
        list.append(y)

        return list

    @staticmethod
    def getAttackNext(mytank,list):
        localSpotTemp=[]
        if list[0]>list[1]:
            print(int(mytank.getLocalspot()[0])-1)
            localSpotTemp.append(int(mytank.getLocalspot()[0])-1)
            localSpotTemp.append(int(mytank.getLocalspot()[1]))
            mytank.setLocalspot(localSpotTemp)
        else:
            localSpotTemp[0] = int(mytank.getLocalspot[0])
            localSpotTemp[1] = int(mytank.getLocalspot[1])-1
            mytank.setLocalspot(localSpotTemp)
        print("=================")
        print(mytank.getLocalspot())
        print("=================")
        return mytank

    @staticmethod
    def getSaveLiveNext(mytank,list):
        otherTankLocalspot=[]
        if list[0]>list[1]:
            otherTankLocalspot.append(int(mytank.getLocalspot()[0])-1)
            otherTankLocalspot.append(int(mytank.getLocalspot()[1]))
            mytank.setLocalspot(otherTankLocalspot)
        else:
            otherTankLocalspot.append(int(mytank.getLocalspot()[0]))
            otherTankLocalspot.append(int(mytank.getLocalspot()[1])- 1)
            mytank.setLocalspot(otherTankLocalspot)
        return mytank
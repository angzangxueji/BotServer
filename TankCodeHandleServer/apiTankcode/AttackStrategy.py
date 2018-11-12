from apiTankcode.sendServerTankCode import TankUtils

# 攻击策略  目前分为 攻击和存活
from config.redisUtil import redisUtil


class AttackStrategy:
    def attack(self,mytank,othertank,otherTankLocalspot,MyTankLocation):
        '''
        1. 获取自己和敌人的基本信息
        2. 判断自己和敌人的最短，并反馈下一步走向哪里
            如果距离较短，查看自己的攻击范围是否够
                如果可以攻击 发起攻击 ---》对各项数据的操作。
            返回自己和敌人的状态。
        '''
        # print("-------------------------otherTankLocalspot")
        # print(otherTankLocalspot)
        # print("-------------------------MyTankLocation")
        print(MyTankLocation)
        myTank=[]
        myTank.append(int(int(MyTankLocation)/5))
        if int(int(MyTankLocation))%5==0:
            myTank.append(5)
        else:
            myTank.append(int(MyTankLocation) % 5)

        # print("-------------------------mytank")
        # print(myTank)
        otherTank=[]
        otherTank.append(int(int(otherTankLocalspot)/5))
        if int(int(otherTankLocalspot))%5==0:
            otherTank.append(5)
        else:
            otherTank.append(int(otherTankLocalspot) % 5)

        # print("===========otherTanklist")
        # print(otherTank)
        # print("===========otherTanklist")
        list=TankUtils.GetDirection(myTank,otherTank)
        print(list)

        FireResultChoose=mytank.isFire(list)
        redisUtil.FireResult(mytank.getUserName(),FireResultChoose)
        print("FireResultChoose is " + str(FireResultChoose))
        mytank.setLocalspot(myTank)
        othertank.setLocalspot(otherTank)
        mytank=TankUtils.getAttackNext(mytank,list)
        print("=================")
        print(mytank)
        print(othertank)
        print("=================")
        redisUtil.ModifyLocalSpot(mytank,MyTankLocation)

        print("=================")
        print(mytank)
        print(othertank)
        print("=================")
        print("attackMethod")
        # mytank.isFire(otherTankLocalspot)
        '''返回两个tank的基本信息 如果发射炮弹，得对tank进行相应的数据改变。'''
        return (mytank,othertank)

    def saveLife(self,mytank,othertank,otherTankLocalspot,MyTankLocation):
        '''
            1. 获取自己和敌人的基本信息
            2. 判断自己和敌人的最短，并反馈下一步走向哪里
                如果距离较短，查看自己的攻击范围是否够
                    如果可以攻击 发起攻击 ---》对各项数据的操作。
                        返回自己和敌人的状态。
        '''
        print("-------------------------otherTankLocalspot")
        print(otherTankLocalspot)
        print("-------------------------MyTankLocation")
        print(MyTankLocation)
        myTank = []
        myTank.append(int(int(MyTankLocation) / 5))
        myTank.append(int(MyTankLocation) % 5)
        print("-------------------------mytank")
        print(myTank)
        otherTank = []
        otherTank.append(int(int(otherTankLocalspot) / 5))
        otherTank.append(int(otherTankLocalspot) % 5)

        print("===========otherTanklist")
        print(otherTank)
        print("===========otherTanklist")
        list = TankUtils.GetDirection(myTank, otherTank)
        print(list)
        FireResultChoose = mytank.isFire(list)
        print("FireResultChoose is " + str(FireResultChoose))
        redisUtil.FireResult(mytank.getUserName(), FireResultChoose)
        mytank.setLocalspot(myTank)
        othertank.setLocalspot(otherTank)
        mytank = TankUtils.getSaveLiveNext(mytank, list)
        redisUtil.ModifyLocalSpot(mytank, MyTankLocation)
        print("saveLifeMethod")
        return (mytank, othertank)

    def gohead(self,mytank,MyTankLocation,tankList):
        print(MyTankLocation)
        myTank = []
        myTank.append(int(int(MyTankLocation) / 5))
        myTank.append(int(MyTankLocation) % 5)
        target=[]
        target.append(int(int(tankList)/5))
        target.append(int(int(tankList)%5))
        list = TankUtils.GetDirection(myTank, target)
        print(list)
        mytank.setLocalspot(myTank)
        mytank = TankUtils.getAttackNext(mytank, list)
        redisUtil.ModifyLocalSpot(mytank, MyTankLocation)

        print(mytank)
        print("goHeadMethod")
        # mytank.isFire(otherTankLocalspot)
        '''返回两个tank的基本信息 如果发射炮弹，得对tank进行相应的数据改变。'''
        return mytank
# (mytankResult)=attackStrategy.gohead(mytank,3,19)
# (mytankResult, otherTankResult)=attackStrategy.saveLife(mytank,otherTank,"3","19")
# attackStrategy=AttackStrategy()
#  (mytankResult, otherTankResult)=attackStrategy.saveLife(mytank,otherTank,"3","19")
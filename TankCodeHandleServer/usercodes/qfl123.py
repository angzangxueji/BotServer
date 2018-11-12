from apiTankcode.AttackStrategy import AttackStrategy
from apiTankcode.UserAPI import UserAPI
from config.redisUtil import redisUtil


class qiao12223:
    def seeTank(self,mytank):
        if mytank.getHP() > 50 :
            return UserAPI.attackTank()
        else:
            return UserAPI.saveLife()

    def seeNothing(self,mytank):
        return UserAPI.goHead()

    def seeFort(self,mytank):
        if mytank.getHP() > 50 :
            return UserAPI.attackFort()
        else:
            return UserAPI.saveLife()


tankinfo=redisUtil.getTankInfo("qiao13")
print(tankinfo.__dict__)
attackStrategy = AttackStrategy()
mytank = redisUtil.getTankInfo("qiao13")#从redis中获取mytankinfo。
otherTank=redisUtil.getTankInfo("qiao12223")
tankList=['25']
MyTankLocation=['1']
print("==================>otherTank")
print(otherTank)
print("==================>otherTank")

(mytankResult, otherTankResult)=attackStrategy.attack(mytank,otherTank,"2","25")
# (mytankResult)=attackStrategy.gohead(mytank,3,19)
# (mytankResult, otherTankResult)=attackStrategy.saveLife(mytank,otherTank,"3","19")


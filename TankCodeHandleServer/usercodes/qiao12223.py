from apiTankcode.sendServerTankCode import Direction
class qiao12223:
    def myCodeHP1(self,mytank):
        print(mytank)
        if mytank.getHP() > 30:
            if mytank.getEnvironment().getIsExistTank() == False:
                mytank.setDirection(Direction.up)
                return mytank.getDirection()
            elif mytank.getEnvironment().getIsExistTank() == True:
                if (mytank.getDistance() < 10):
                    mytank.setDirection(Direction.down)
                    return mytank.getDirection()
                else:
                    mytank.setDirection('aaa')
                    return mytank.getDirection()
            else:
                mytank.setDirection(Direction.up)
                return mytank.getDirection()
        elif mytank.getHP() < 100:
            print('<100')
            return mytank.getDirection()
        else:
            print('>=30')
            return mytank.getDirection()

    # def myCodeGongJi(self,mytank):

    '''
    set攻击模式（暴力/智取）
    if see tank 
        if mytank.HP>histank.HP//或者是当我的血小于 多少% 
            gongji();
            我和对方是否存活。
            
            默认情况是暴力攻击。
            
            
    
    '''


    '''
    攻击时tank的行驶状态。
    
    gongji(tankinfos)
        get our infomation(localspot ) 返回值为 我们该怎么走去进攻 
        判断是否发炮
    
    撤退():
        get our infomation(localspot ) 返回值为 我们该怎么走撤退：
        
    快速前行()
         get our infomation(localspot ) 返回值为 我们该怎么走 快速前行的方向：
           
     
     tank的状态（一个属性） live or dead
     
     攻击的时候减血  在攻击之后进行判断目前血量 并查看自己的状态。
     
       
    '''

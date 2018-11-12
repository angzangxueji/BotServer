from apiTankcode.sendServerTankCode import Direction
class jiaoben111:
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
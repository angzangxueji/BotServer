#!/usr/bin/env python

from socket import *

from pip._vendor.distlib.compat import raw_input

from apiTankcode.sendServerTankCode import MyTank
from usercodes.TankTemplate import TankTemplate

# HOST = '0.0.0.0'
# PORT = 21567
# BUFSIZ = 1024
# ADDR = (HOST, PORT)
#
# tcpSerSock = socket(AF_INET, SOCK_STREAM)
# tcpSerSock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
# tcpSerSock.bind(ADDR)
# tcpSerSock.listen(5)
#
# while True:
#     print('waiting for connection...')
#     tcpCliSock, addr = tcpSerSock.accept()
#     print('...connected from:', addr)
#     while True:
#         data = tcpCliSock.recv(BUFSIZ)
#         print(data.decode()+"===1")
#         if not data or data.decode()=="quit":
#             # print(data)
#             tcpCliSock.send(("hello(Server quit) " + data.decode("utf-8")).encode("utf-8"))
#             break
#         if data.decode() !="\n":
#             # print(data.decode())
#             str1=data.decode()[1:str(data.decode()).__len__()-1]
#             list=str1.split(";")
#             print(list)
#             # print(list[3])
#             environment=list[3][1:list[3].__len__()-2]
#             '''收到的用户环境数据处理。'''
#             listEnvironmentargs=environment.split(",")
#             userTank1 = MyTank(int(list[0]), list[1], list[2], list[3])
#             userTank1.setEnvironment(listEnvironmentargs)
#             result1=TankTemplate(userTank1)
#
#             print("============userTankEnvironmentInfo")
#             print(result1.__str__())
#             print("============userTankEnvironmentInfo")
#             tcpCliSock.send(("hello(Server) "+data.decode("utf-8")).encode("utf-8"))
#         else:
#             tcpCliSock.send(("hello(Server null) "+data.decode("utf-8")).encode("utf-8"))
#     tcpCliSock.close()
#

import redis
pool=redis.ConnectionPool(host='192.168.178.1',port=6379)
r = redis.StrictRedis(connection_pool=pool)
while True:
    input = raw_input("publish:")
    if input == 'over':
        print ('停止发布')
        break;
    r.publish('news.test2', input)

import json
import os
import time
from flask import Flask, render_template, request,  jsonify, send_from_directory
from werkzeug.utils import secure_filename, escape
from apiTankcode.AttackStrategy import AttackStrategy
from apiTankcode.SysAPI import SysAPI
from apiTankcode.sendServerTankCode import MyTank
from usercodes.TankTemplate import TankTemplate, TankTemplate2

app = Flask(__name__,static_folder="mystatic",template_folder="mytemplates")

'''
接受playing的user数据。
'''
@app.route('/playingArgs',methods={"POST","GET"})
def playingArgs():
    print("hello world")
    data = request.get_data().decode("utf-8")
    json_re = json.loads(data)
    '''使用redis查找出玩家所需要的信息。
    redisConnection = RedisUtils.connection()
    redisConnection.set()'''

    environment =json_re.get("environment")
    print(environment)
    listEnvironmentArgs=environment[1:environment.__len__()-1]
    listEnvironmentArgs=listEnvironmentArgs.split(",")
    userTank1 = MyTank(int(json_re.get("HP")), json_re.get("localspot"), json_re.get("direction"), listEnvironmentArgs)
    userTank1.setEnvironment(listEnvironmentArgs)
    name = json_re.get("username")

    '''通过TankTemplate函数调用玩家对应的类'''
    result = TankTemplate(userTank1,name)
    # for(1) in : to 2)
    # return "ok"
    return "["+name+"结果是:  ]"+result

@app.route('/playingOneResive',methods=("POST","GET"))
def playingOneResive():
    from config.redisUtil import redisUtil
    redisConnection =redisUtil.getConnections()
    attackStrategy = AttackStrategy()
    MySystemAPI = SysAPI()

    '''接受netty传输的数据'''
    data = request.get_data().decode("utf-8")
    json_re =json.loads(data)
    username=json_re.get("username")
    roomid =json_re.get("roomid")

    '''通过redis获取玩家的相关数据。
    MyTankLocation = redisConnection.hget("room_id_list", username)
    OtherTankLocation =redisConnection.hget("room_id_list", otherTankUserName)
    print("===========MyTankLocation======>"+MyTankLocation.__str__())
    print("===========OtherTankLocation======>"+OtherTankLocation.__str__())
    '''
    otherTankUserName = redisConnection.hget("roomid", username)

    while True:
        (loadList, tankList, fortList,MyTankLocation) = MySystemAPI.Scanner(1,username, roomid, redisConnection)
        myTankHP=redisConnection.hget(username,'HP')
        otherTankHP=redisConnection.hget(otherTankUserName,'HP')
        mytank = redisUtil.getTankInfo(username)#从redis中获取mytankinfo。
        otherTank=redisUtil.getTankInfo(otherTankUserName)

        if tankList.__len__() != 0:
            #可以判断一下用户是否有写相关内容
            tankMethod="seeTank"
            '''tank完成一个动作需要分为两部分：1.判断tank的动作 2.执行该动作。'''
            result=TankTemplate2(username,tankMethod,mytank)
            if result.__eq__("attack"):
                (mytankResult, otherTankResult)=attackStrategy.attack(mytank,otherTank,tankList,MyTankLocation)
                '''对处理后的tank数据进行修改。'''
            elif result.__eq__("saveLife"):
                (mytankResult, otherTankResult) =attackStrategy.saveLife(MyTankLocation,tankList)
                print("saveLife")
            else:
                print("error")

        elif fortList.__len__()!=0:
            tankMethod = "seeFort"
            result = TankTemplate2(username, tankMethod, mytank)
        elif loadList.__len__()!=0:
            tankMethod = "seeNothing"
            result = TankTemplate2(username, tankMethod, mytank)
            attackStrategy.gohead(mytank,tankList)


        elif myTankHP.__eq__('0') or otherTankHP.__eq__('0')   :
            #判断游戏是否结束
            break
        else:
            print("error")

    else:
        print("xxx")



    '''
    开始游戏后，会将tank的数据存到相应的位置，保证双方都能看到，当移动位置时，就会时时的发送数据。
    调用该用户的python文件，并返回tank的状态及相关数据。
    在这得获得tank本身的数据，从redis中获取tank数据，所以得写一个scanner的方法，查看周围的数据有哪些。
    
    
    '''

    return "ok"


# 用于测试上传，稍后用到
@app.route('/upload', methods=['GET'])
def upload_test():
    return render_template('upload.html')

UPLOAD_FOLDER='upload'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
basedir = os.path.abspath(os.path.dirname(__file__))
ALLOWED_EXTENSIONS = set(['txt', 'png', 'jpg', 'xls', 'JPG', 'PNG', 'xlsx', 'gif', 'GIF'])

# 用于判断文件后缀
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/upload', methods=['POST'], strict_slashes=False)
def api_upload():
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER'])
    if not os.path.exists(file_dir):
        os.makedirs(file_dir)
    f = request.files['myfile']  # 从表单的file字段获取文件，myfile为该表单的name值
    if f and allowed_file(f.filename):  # 判断是否是允许上传的文件类型
        fname = secure_filename(f.filename)
        print(fname)
        ext = fname.rsplit('.', 1)[1]  # 获取文件后缀
        unix_time = int(time.time())
        new_filename = str(unix_time) + '.' + ext  # 修改了上传的文件名
        f.save(os.path.join(file_dir, new_filename))  # 保存文件到upload目录
        print("=======success")
        return jsonify({"errno": 0, "errmsg": "上传成功", "new_filename": new_filename})
    else:
        return jsonify({"errno": 1001, "errmsg": "上传失败"})


@app.route('/download/<filename>')
def download(filename):
    if request.method=="GET":
        return send_from_directory('upload',filename,as_attachment=True)



if __name__ == '__main__':
    app.run(host="0.0.0.0",port=8888,debug=False)

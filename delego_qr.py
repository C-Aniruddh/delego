from flask import Flask, render_template, url_for, request, session, redirect, send_from_directory
from flask_pymongo import PyMongo
import json
from bson.json_util import dumps
import bcrypt
import random

app = Flask(__name__)
app.config['MONGO_DBNAME'] = 'delego'
app.config['MONGO_URI'] = 'mongodb://localhost:27017/delego'
mongo = PyMongo(app)

@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/add_ids')
def add_ids():
    committees = mongo.db.committees
    delegaterange = (1, 350, 1)
    for x in delegaterange:
        find_delegate = committees.find_one({'Numid' : x})
        delegate_name = find_delegate['Name']
        delegate_identifier = bcrypt.hashpw(delegate_name.encode('utf-8'), bcrypt.gensalt())
        committees.update_one({'Numid': int(x)}, {"$set": {'identifier': delegate_identifier}})

    return 'DONE!'
@app.route('/user_details/<user_id>')
def user_details(user_id):
    committees = mongo.db.newdb
    find_user = committees.find_one({'identifier' : str(user_id)})
    if (bcrypt.hashpw(find_user['name'].encode('utf-8'),
                      user_id.encode('utf-8')) == find_user['identifier'].encode('utf-8')):
        user_name = find_user['name']
        user_country = find_user['country']
        user_committee = find_user['committee']
        user_role = find_user['role']
        user_portfolios = find_user['portfolio']
        user_rsvp = find_user['rsvp']
        user_image = find_user['image']
        user_numid = find_user['numid']
        user_email = find_user['email_id']
        user_phonenumber = find_user['phone_number']
        """user_name = find_user['name']
        user_country = find_user['country']
        user_committee = find_user['committee']
        user_role = find_user['role']
        user_portfolios = find_user['portfolio']
        user_rsvp = find_user['rsvp']
        user_image = find_user['image']
        user_numid = find_user['numid']"""
        return json.dumps({'Name': user_name, 'Country' : user_country, 'Committee' : user_committee, 'Numid':str(user_numid), 'Role': user_role, 'Portfolios' :user_portfolios, 'RSVP' : user_rsvp, 'Image' : user_image, 'Email' : user_email, 'Phone' : user_phonenumber})
    else:
        return json.dumps({'Name' : 'Authentication Token failed.', 'Country' : 'Authentication Token failed', 'Committee' : 'Authentication Token failed', 'Numid': 'Authentication Token failed', 'Role': 'Authentication Token failed', 'Portfolios':'Authentication Token failed', 'RSVP' : 'Authentication Token failed', 'Image' : 'Authentication Token failed'})

@app.route('/user_arrival/<user_id>')
def user_arrival(user_id):
    committees = mongo.db.newdb
    committees.update_one({'identifier': user_id}, {"$set": {'rsvp': 'Arrived'}})
    return json.dumps({'set_arrival' : user_id})

@app.route('/allusers', methods=['POST', 'GET'])
def allusers():
    committees = mongo.db.committees
    users = mongo.db.users
    all_users = committees.find({}, {'_id': False})
    return dumps({'delegate' : all_users})

@app.route('/user_sort/<user_type>', methods=['POST', 'GET'])
def user_sort(user_type):
    committees = mongo.db.newdb

    if (user_type == 'All'):
        find_users = committees.find({}, {'_id': False})
        return dumps({'delegate': find_users})
    else:
        find_users = committees.find({'committee' : user_type}, {'_id': False})
        return dumps({'delegate': find_users})

@app.route('/search_delegate/<query>', methods=['POST', 'GET'])
def search_delegate(query):
    committees = mongo.db.newdb
    users = mongo.db.users
    find_users = committees.find({'name' : {'$regex' : query, '$options' : 'i'}})
    return dumps({'delegate': find_users})

@app.route('/all_names', methods=['POST', 'GET'])
def all_names():
    committees = mongo.db.newdb
    users = mongo.db.users
    find_users = committees.find({}, {'name':True, '_id': False})
    return dumps(find_users)

@app.route('/by_committee/<committee_name>', methods=['POST', 'GET'])
def by_committee(committee_name):
    print "Received Committee Name :"+ str(committee_name)
    committees = mongo.db.newdb
    find_users = committees.find({'committee' : committee_name}, {'_id' : False, 'portfolio':False, 'role':False, 'image' : False})#'Name' : True, 'Country' : True, 'Role' : True, 'Committee' : True})
#    for x in find_users:
#        print 'Committee Idiot: ' + str(x)
    return dumps({'delegate' :find_users})

#@app.route('/login/', methods=['POST'])
@app.route('/login', methods=['POST', 'GET'])
def login():
    print "Username : " + str(request.form['username']) + "     PAssword : " + str(request.form['password'])
    global login_flag
    users = mongo.db.users
    login_user = users.find_one({'name' : request.form['username']})
    login_username = login_user['name']
    login_mail = login_user['email']
    login_fullname = login_user['fullname']
    login_user_type = login_user['user_type']
    randint = random.randint(100000,999999)
    #data = str(request.values)
    #print data
    auth_string = '%s%s' % (request.form['username'], randint)
    #auth_token = bcrypt.hashpw(request.form['username'].encode('utf-8'), bcrypt.gensalt())
    #print login_user
    if login_user:
       print "Inside login_user"
       if bcrypt.hashpw(request.form['password'].encode('utf-8'), login_user['password'].encode('utf-8'))==login_user['password'].encode('utf-8'):
            print "Inside bcrypt"
            auth_token = bcrypt.hashpw(auth_string.encode('utf-8'), bcrypt.gensalt())
            print "Auth token :  " +  str(auth_token)
            login_flag = 1#session['username'] = request.form['username']
            return json.dumps({'auth_token' : auth_token, 'login' : 'success', 'email':login_mail, 'username':login_username, 'fullname' : login_fullname, 'type':login_user_type})#return redirect(url_for('index'))
    return json.dumps({'login': 'unsuccessful'})


@app.route('/register', methods=['POST', 'GET'])
def register():
    if request.method == 'POST':
        users = mongo.db.users
        user_fname = request.form['name']
        user_email = request.form['email']
        user_type = 'user'
        existing_user = users.find_one({'name': request.form['username'], 'email': user_email})
        if existing_user is None:
            hashpass = bcrypt.hashpw(request.form['pass'].encode('utf-8'), bcrypt.gensalt())
            users.insert(
                {'fullname': user_fname, 'email': user_email, 'name': request.form['username'], 'password': hashpass,
                 'user_type': user_type})
            return redirect(url_for('index'))

        return 'A user with that Email id/username already exists'

    return render_template('register.html')


@app.route('/add_notification', methods=['POST', 'GET'])
def add_notification():
    if request.method == 'POST':
        notifications = mongo.db.notifications
        notification_title = request.form['title']
        notification_content = request.form['content']
        find_all = notifications.find()
        all_count = find_all.count()
        notification_id = str(all_count + 1)
        notifications.insert({'numid' : notification_id, 'title':notification_title, 'content':notification_content})
        return 'Done'
    return render_template('newnotif.html')

@app.route('/notifications', methods=['POST', 'GET'])
def notifications():
    notifications = mongo.db.notifications
    find_notifs = notifications.find({}, {'_id': False})
    return dumps({'notification': find_notifs})

if __name__ == '__main__':
    app.secret_key = 'mysecret'
    app.run(host='0.0.0.0', port=5000)

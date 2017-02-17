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
        """user_name = find_user['name']
        user_country = find_user['country']
        user_committee = find_user['committee']
        user_role = find_user['role']
        user_portfolios = find_user['portfolio']
        user_rsvp = find_user['rsvp']
        user_image = find_user['image']
        user_numid = find_user['numid']"""
        return json.dumps({'Name': user_name, 'Country' : user_country, 'Committee' : user_committee, 'Numid':str(user_numid), 'Role': user_role, 'Portfolios' :user_portfolios, 'RSVP' : user_rsvp, 'Image' : user_image})
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

@app.route('/login/<json_data>', methods=['POST', 'GET'])
def login(json_data):
    users = mongo.db.users
    details = json.loads(json_data)
    login_user = details['name']
    login_pass = details['password']
    find_user = users.find_one({'name' : login_user})
    randint = random.randint(100000, 999999)
    ecoding_string = '%s%s' %(login_user, randint)
    print login_pass

    if (bcrypt.hashpw(find_user['password'].encode('utf-8'),
                      login_pass.encode('utf-8')) == find_user['password'].encode('utf-8')):
        auth_token = bcrypt.hashpw(ecoding_string.encode('utf-8'), bcrypt.gensalt())
        users.update_one({'name': login_user}, {"$set": {'auth_token': auth_token}})
        return json.dumps({'login_status' : 'successful', 'auth_token' : auth_token})


    return json.dumps(details)

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

if __name__ == '__main__':
    app.secret_key = 'mysecret'
    app.run(host='0.0.0.0', port=5000)

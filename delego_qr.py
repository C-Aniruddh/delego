from flask import Flask, render_template, url_for, request, session, redirect, send_from_directory
from flask_pymongo import PyMongo
import json
from bson.json_util import dumps


app = Flask(__name__)
app.config['MONGO_DBNAME'] = 'delego'
app.config['MONGO_URI'] = 'mongodb://localhost:27017/delego'
mongo = PyMongo(app)


@app.route('/')
def hello_world():
    return 'Hello World!'

@app.route('/user_details/<user_id>')
def user_details(user_id):
    users = mongo.db.users
    find_user = users.find_one({'user_id' : user_id})
    user_name = find_user['name']
    user_age = find_user['age']
    user_type = find_user['type']
    user_image = find_user['user_image']
    return json.dumps({'name': user_name, 'user_age' : user_age, 'user_type' : user_type, 'user_id':user_id, 'user_image':user_image})

@app.route('/user_arrival/<user_id>')
def user_arrival(user_id):
    users = mongo.db.users
    users.update_one({'user_id': user_id}, {"$set": {'rsvp': 'arrived'}})
    return json.dumps({'set_arrival' : user_id})

@app.route('/allusers', methods=['POST', 'GET'])
def allusers():
    users = mongo.db.users
    all_users = users.find({}, {'_id': False})
    return dumps({'delegate' : all_users})

@app.route('/user_sort/<user_type>', methods=['POST', 'GET'])
def user_sort(user_type):
    users = mongo.db.users
    if (user_type == 'All'):
        find_users = users.find({}, {'_id': False})
        return dumps({'delegate': find_users})
    elif (user_type == 'Owners'):
        find_users = users.find({'type' : 'owner'}, {'_id': False})
        return dumps({'delegate': find_users})
    elif (user_type == 'Hosts'):
        find_users = users.find({'type' : 'host'}, {'_id': False})
        return dumps({'delegate': find_users})
    elif (user_type == 'Exhibitioners'):
        find_users = users.find({'type' : 'exhibitioner'}, {'_id': False})
        return dumps({'delegate': find_users})
    else:
        find_users = users.find({'type' : 'delegate'}, {'_id': False})
        return dumps({'delegate': find_users})

@app.route('/search_delegate/<query>', methods=['POST', 'GET'])
def search_delegate(query):
    users = mongo.db.users
    find_users = users.find({'name' : {'$regex' : query, '$options' : 'i'}})
    return dumps({'delegate': find_users})

@app.route('/all_names', methods=['POST', 'GET'])
def all_names():
    users = mongo.db.users
    find_users = users.find({}, {'name':True, '_id': False})
    return dumps(find_users)
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

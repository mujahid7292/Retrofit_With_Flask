from app import app
from flask import (jsonify, request, send_from_directory,
            abort, render_template)
import secrets
import os

@app.route("/")
def index():
    return 'Index'

# 2. Send Objects In Request Body.
@app.route('/api/SendObjectsInRequestBody', methods=['POST'])
def sendObjectsInRequestBody():
    # We will receive json as python dictionary. {}
    data = request.get_json()
    #print(type(data))
    # Here topics is python list []
    topics = data['topics']
    #print(type(topics))
    for counter, topic in enumerate(topics):
        print(f"{counter}. {topic}")

    #data["id"] = 5887
    data.update({"id":5887})
    return jsonify(data)

# 3. Send Single File To Server.

app.config['ALLOWED_EXTENSIONS'] = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

def allowed_file(filename):
    print(filename)
    if "." in filename and filename.rsplit(".", 1)[1] \
        in app.config['ALLOWED_EXTENSIONS']:
        return True
    return False



def save_file(file):
    # We will create random directory name.
    file_dir_name = secrets.token_hex(16)
    # 16 means, it will give us 16 character token.
    # Now we will create the file directory
    os.makedirs(
        os.path.join(
            app.config['UPLOAD_DIRECTORY'],
            file_dir_name
        )
    )
    # Now we will save the file.
    file.save(
        os.path.join(
            app.config['UPLOAD_DIRECTORY'],
            file_dir_name,
            file.filename
        )
    )
    # Now we will save the file directory path in a variable.
    file_dir = os.path.join(
                    app.config['UPLOAD_DIRECTORY'],
                    file_dir_name
                )
    return file_dir

@app.route("/api/upload-single-file", methods=['POST'])
def uploadSingleFile():
    description = request.form.get("Description")
    print(f"Description: {description}")
    uploaded_files = request.files.getlist("file[]")
    #print(uploaded_files)
    for file in uploaded_files:
        #print(file)
        if file and allowed_file(file.filename):
            file_dir = save_file(file)
            print(file_dir)
        else:
            return jsonify({"message": "Invalid file extension."})
    return jsonify({"message":"File saved to server."})

# 4. Passing Multiple Parts Along a File with @PartMap

@app.route("/api/upload-single-file-with-multi-parts", methods=['POST'])
def uploadSingleFileWithMultiParts():
    pythonDictionary = request.form
    print(f"Type: {pythonDictionary}")
    for k, v in pythonDictionary.items():
            print(f"Key: {k} | Value: {v}")
    uploaded_files = request.files.getlist("file[]")
    #print(uploaded_files)
    for file in uploaded_files:
        #print(file)
        if file and allowed_file(file.filename):
            file_dir = save_file(file)
            print(file_dir)
        else:
            return jsonify({"message": "Invalid file extension."})
    return jsonify({"message":"File saved to server."})

@app.route("/api/upload-single-file-with-part-map", methods=['POST'])
def uploadSingleFileWithPartMap():
    pythonDictionary = request.form
    print(f"Type: {pythonDictionary}")
    for k, v in pythonDictionary.items():
            print(f"Key: {k} | Value: {v}")
    uploaded_files = request.files.getlist("file[]")
    #print(uploaded_files)
    for file in uploaded_files:
        #print(file)
        if file and allowed_file(file.filename):
            file_dir = save_file(file)
            print(file_dir)
        else:
            return jsonify({"message": "Invalid file extension."})
    return jsonify({"message":"File saved to server."})


# 5. Upload Multiple Files to Server.
@app.route("/api/upload-multiple-files", methods=['POST'])
def uploadMultipleFiles():
    pythonDictionary = request.form
    print(f"Type: {pythonDictionary}")
    for k, v in pythonDictionary.items():
            print(f"Key: {k} | Value: {v}")
    uploaded_files = request.files.getlist("file[]")
    #print(uploaded_files)
    for file in uploaded_files:
        #print(file)
        if file and allowed_file(file.filename):
            file_dir = save_file(file)
            print(file_dir)
        else:
            return jsonify({"message": "Invalid file extension."})
    return jsonify({"message":"File saved to server."})

@app.route("/api/upload-dynamic-amount-of-files", methods=['POST'])
def uploadDynamicAmountOfFiles():
    pythonDictionary = request.form
    print(f"Type: {pythonDictionary}")
    for k, v in pythonDictionary.items():
            print(f"Key: {k} | Value: {v}")
    uploaded_files = request.files.getlist("file[]")
    #print(uploaded_files)
    for file in uploaded_files:
        #print(file)
        if file and allowed_file(file.filename):
            file_dir = save_file(file)
            print(file_dir)
        else:
            return jsonify({"message": "Invalid file extension."})
    return jsonify({"message":"File saved to server."})

# 6. Custom Request Headers
@app.route("/api/custom-request-headers", methods=['POST'])
def customRequestHeaders():
    headers = request.headers
    #print(f"Headers: \n{headers}")
    for k, v in headers.items():
            print(f"Key: {k} | Value: {v}")

    return jsonify({"message":"Custom Headers Received."})

# 8. Download Files from Server.
@app.route("/api/get-file/<string:file_name>")
def downloadFile(file_name):
    try:
        return send_from_directory(
            directory=app.config['DOWNLOAD_DIRECTORY'],
            filename=file_name,
            as_attachment=False
        )
    except FileNotFoundError:
        print("File Not Found")
        abort(404)

# 9. Error Handling.
@app.errorhandler(404)
def error_404(error):
    return jsonify({
        "status_code": 404,
        "message": "User not found." 
    }), 404

@app.errorhandler(403)
def error_403(error):
    return jsonify({
        "status_code": 403,
        "message": "You don't have permission." 
    }), 403

@app.errorhandler(500)
def error_500(error):
    return jsonify({
        "status_code": 500,
        "message": "Internal Server Error." 
    }), 500

@app.route("/api/error_handling/<int:error_code>", methods=['GET', 'POST'])
def error_handling(error_code):
    print(f"Error code sent by retrofit: {error_code}")
    abort(error_code)
    return "No Error occured."

# 10. Send Data Form-Urlencoded
@app.route("/api/form-url-encoded", methods=['GET','POST'])
def formUrlEncoded():
    if request.method == 'POST':
        data = request.form

        full_name = data.get('full_name', None)
        username = data.get('username', None)
        password = data.get('password', None)
        age = data.get('age', None)
        topics = data.get('topics', None)

        print(f" full_name: {full_name}\n username: {username}\n \
password: {password} \n age: {age}\n topics: {topics}")
        print(f"topics type: {type(topics)}")
        return jsonify({"message": "Form-UrlEncoded Data Received."})
    return render_template("form_url_encoded.html")

# 11. Send Plain Text Request Body
@app.route("/api/plain-text-request-body", methods=['POST'])
def sendPlainTextRequestBody():
    print(f"MimeType: {request.mimetype}")
    print(f"ContentType: {request.content_type}")
    data = request.data.decode('UTF-8')
    print(f"Data Type: {type(data)}")
    print(data)
    return jsonify({"message": "Plain Text Request Body Received."})

# 12.Add Query Parameters to Every Request.
@app.route("/api/query-parameters")
def queryParameters():
    query_params = request.args
    if query_params:
        for key, value in query_params.items():
            print(f"Key: {key} | Value: {value}")
        serialized = ", ".join(f"Key: {key} | Value: {value}" for key, value in query_params.items())
        return f"Query Received: {serialized}", 200
    else:
        return jsonify({"message": "No Query Parameters Received."})

# 13. Basic Authentication
@app.route("/api/basic-authentication")
def basicAuth():
    auth = request.authorization
    print(f'Auth: {auth}')
    if auth:
        username = auth.username
        password = auth.password
        return f"username: {username} | password: {password}"
    return "Empty Authorization."
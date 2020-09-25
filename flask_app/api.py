from flask import (Flask, jsonify, request, send_from_directory,
            abort)
import secrets
import os

app = Flask(__name__)
app.config['UPLOAD_DIRECTORY'] = '/media/mujahid7292/Data/Retrofit_With_Flask/flask_app/uploaded_files'
app.config['DOWNLOAD_DIRECTORY'] = '/media/mujahid7292/Data/Retrofit_With_Flask/flask_app/download_files'

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

if __name__ == '__main__':
    app.run(debug=True)
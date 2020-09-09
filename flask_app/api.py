from flask import Flask, jsonify, request

app = Flask(__name__)

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
@app.route("/api/upload-single-file", methods=['POST'])
def uploadSingleFile():
    return ''

if __name__ == '__main__':
    app.run(debug=True)
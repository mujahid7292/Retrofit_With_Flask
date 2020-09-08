from flask import Flask

app = Flask(__name__)

@app.route('/api/register')
def register():
    return "Register"

if __name__ == '__main__':
    app.run(debug=True)
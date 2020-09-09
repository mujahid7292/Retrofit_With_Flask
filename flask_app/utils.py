from api import app

app.config["ALLOWED_EXTENSIONS"] = set([['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif']])

def allowed_file():
    
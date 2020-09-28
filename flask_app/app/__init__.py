from flask import Flask

app = Flask(__name__)

app.config['UPLOAD_DIRECTORY'] = '/media/mujahid7292/Data/Retrofit_With_Flask/flask_app/uploaded_files'
app.config['DOWNLOAD_DIRECTORY'] = '/media/mujahid7292/Data/Retrofit_With_Flask/flask_app/download_files'

from app import api
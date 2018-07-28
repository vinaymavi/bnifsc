from flask import Flask, render_template, request
app = Flask(__name__)


@app.route('/extract/')
def index():
    return render_template('auth.html')


@app.route('/extract/auth', methods=['POST'])
def auth():
    passcode = request.form['passcode']
    return render_template('extract.html', passcode=passcode)

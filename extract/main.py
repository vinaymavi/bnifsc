from flask import Flask, render_template, request
app = Flask(__name__)
from google.cloud import pubsub_v1
import base64
import json
import logging
logging.getLogger().setLevel(logging.INFO)

PROJECT = 'bnifsc-beta'
PUB_SUB_TOPIC_BANK_TRANSFORM = 'transform-bank'
publisher = pubsub_v1.PublisherClient()
topic_path = publisher.topic_path(PROJECT, PUB_SUB_TOPIC_BANK_TRANSFORM)


@app.route('/extract/')
def index():
    return render_template('auth.html')


@app.route('/extract/auth', methods=['POST'])
def auth():
    passcode = request.form['passcode']
    return render_template('extract.html', passcode=passcode)


@app.route('/extract/transform-bank', methods=['POST'])
def transform_bank():
    """
    This HTTP request trigger automatically by pub-sub subscriber.
    """
    data = request.get_json()
    logging.info(data)
    if 'message' in data and 'data' in data['message']:
        message_str = data['message']['data']
        logging.info(message_str)
        logging.info(base64.b64decode(message_str))
    logging.info("Transform Bank Calling...")
    return "Transform Bank Calling..."

@app.route('/extract/transform-bank-push', methods=['GET'])
def transform_bank_push():
    logging.info("Transform Bank Push Calling...")
    for n in range(1, 10):
        data = u'Message number {}'.format(n)
        # Data must be a bytestring
        data = data.encode('utf-8')
        publisher.publish(topic_path, data=data)

    return "Transform Bank Push Calling..."


if __name__ == '__main__':
    # This is used when running locally. Gunicorn is used to run the
    # application on Google App Engine. See entrypoint in app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)
from flask import Flask, render_template, request
app = Flask(__name__)
from google.cloud import pubsub_v1
import base64
import json
import logging
from google.cloud import bigquery

# Instantiates a client
bigquery_client = bigquery.Client()

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
    logging.info("TRANSFORM BANK REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    if 'message' in data and 'data' in data['message']:
        message_str = data['message']['data']
        logging.info("ENCODED STRING = {}".format(message_str))
        logging.info("DECODED STRING = {}".format(base64.b64decode(message_str)))
    logging.info("TRANSFORM BANK REQUEST END")
    return "Transform Bank Calling..."

@app.route('/extract/transform-bank-push', methods=['GET'])
def transform_bank_push():
    """
    Push Banks data to pub-sub.
    """
    logging.info("########## BANK DATA PUBLISHING START#########")
    query = (
    'SELECT * FROM `bnifsc-beta.BNIFSC_IFSC_CODE.Dataprep_Bank_List` '
    'LIMIT 100')
    query_job = bigquery_client.query(
        query,
        # Location must match that of the dataset(s) referenced in the query.
        location='US')  # API request - starts the query

    for row in query_job:  # API request - fetches results
        logging.info("******** NEW ROW START**********")
        logging.info("PUB_SUB:PUSH:BANK_NAME {}".format(row))
        data = {
            "bank_id":row['bank_id'],
            "bank":row['bank'],
            "old_bank_name":row['old_bank_name'],
        }
        data_str = data=json.dumps(data)
        logging.info("Data = {}".format(data_str))
        publisher.publish(topic_path,data_str)
        logging.info("******** NEW ROW END**********")
    logging.info("########## BANK DATA PUBLISHING STOP#########")        
    return "Transform Bank Push Calling..."


if __name__ == '__main__':
    # This is used when running locally. Gunicorn is used to run the
    # application on Google App Engine. See entrypoint in app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)
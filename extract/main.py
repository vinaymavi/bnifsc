from flask import Flask, render_template, request
app = Flask(__name__)
from google.cloud import pubsub_v1
import json
import logging
import utils
from pubsub import PubSub
from bigquery import BigQuery

pubsub = PubSub()
bigquery = BigQuery()

logging.getLogger().setLevel(logging.INFO)

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
    message_data = utils.get_request_data(data)
    logging.info("TRANSFORM BANK REQUEST END")
    return "Transform Bank Calling..."

@app.route('/extract/transform-state', methods=['POST'])
def transform_state():
    """
    This HTTP request trigger automatically by pub-sub subscriber.
    """
    data = request.get_json()
    logging.info("TRANSFORM STATE REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    message_data = utils.get_request_data(data)
    logging.info("TRANSFORM STATE REQUEST END")
    return "Transform state Calling..."

@app.route('/extract/transform-district', methods=['POST'])
def transform_district():
    """
    This HTTP request trigger automatically by pub-sub subscriber.
    """
    data = request.get_json()
    logging.info("TRANSFORM DISTRICT REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    message_data = utils.get_request_data(data)
    logging.info("TRANSFORM DISTRICT REQUEST END")
    return "Transform district Calling..."

@app.route('/extract/transform-city', methods=['POST'])
def transform_city():
    """
    This HTTP request trigger automatically by pub-sub subscriber.
    """
    data = request.get_json()
    logging.info("TRANSFORM CITY REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    message_data = utils.get_request_data(data)
    logging.info("TRANSFORM CITY REQUEST END")
    return "Transform city Calling..."

@app.route('/extract/transform-branch', methods=['POST'])
def transform_branch():
    """
    This HTTP request trigger automatically by pub-sub subscriber.
    """
    data = request.get_json()
    logging.info("TRANSFORM BRANCH REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    message_data = utils.get_request_data(data)
    logging.info("TRANSFORM BRANCH REQUEST END")
    return "Transform branch Calling..."


@app.route('/extract/transform-bank-push', methods=['GET'])
def transform_bank_push():
    """
    Push Banks data to pub-sub.
    """
    logging.info("########## BANK DATA PUBLISHING START#########")
    query_job = bigquery.bank_list()

    for row in query_job:  # API request - fetches results
        logging.info("******** NEW ROW START**********")
        logging.info("PUB_SUB:PUSH:BANK_NAME {}".format(row))
        data = {
            "bank_id": row['bank_id'],
            "bank": row['bank'],
            "old_bank_name": row['old_bank_name'],
        }
        logging.info("Data = {}".format(data))
        pubsub.publish_transform_bank(data)
        logging.info("******** NEW ROW END**********")
    logging.info("########## BANK DATA PUBLISHING STOP#########")
    return "Transform Bank Push Calling..."


@app.route('/extract/transform-state-push', methods=['GET'])
def transform_state_push():
    """
    Push Banks data to pub-sub.
    """
    logging.info("########## state DATA PUBLISHING START#########")
    query_job = bigquery.state_list()

    for row in query_job:  # API request - fetches results
        logging.info("******** NEW ROW START**********")
        logging.info("PUB_SUB:PUSH:state_NAME {}".format(row))
        data = {
            "state_id": row['state_id'],
            "state": row['state'],
            "old_state_name": row['old_state_name'],
        }
        logging.info("Data = {}".format(data))
        pubsub.publish_transform_state(data)
        logging.info("******** NEW ROW END**********")
    logging.info("########## state DATA PUBLISHING STOP#########")
    return "Transform state Push Calling..."


@app.route('/extract/transform-district-push', methods=['GET'])
def transform_district_push():
    """
    Push districts data to pub-sub.
    """
    logging.info("########## district DATA PUBLISHING START#########")
    query_job = bigquery.district_list()

    for row in query_job:  # API request - fetches results
        logging.info("******** NEW ROW START**********")
        logging.info("PUB_SUB:PUSH:district_NAME {}".format(row))
        data = {
            "district_id": row['district_id'],
            "district": row['district'],
            "old_district_name": row['old_district_name'],
        }
        logging.info("Data = {}".format(data))
        pubsub.publish_transform_district(data)
        logging.info("******** NEW ROW END**********")
    logging.info("########## district DATA PUBLISHING STOP#########")
    return "Transform district Push Calling..."


@app.route('/extract/transform-city-push', methods=['GET'])
def transform_city_push():
    """
    Push citys data to pub-sub.
    """
    logging.info("########## city DATA PUBLISHING START#########")
    query_job = bigquery.city_list()

    for row in query_job:  # API request - fetches results
        logging.info("******** NEW ROW START**********")
        logging.info("PUB_SUB:PUSH:city_NAME {}".format(row))
        data = {
            "city_id": row['city_id'],
            "city": row['city'],
            "old_city_name": row['old_city_name'],
        }
        logging.info("Data = {}".format(data))
        pubsub.publish_transform_city(data)
        logging.info("******** NEW ROW END**********")
    logging.info("########## city DATA PUBLISHING STOP#########")
    return "Transform city Push Calling..."


@app.route('/extract/transform-branch-push', methods=['GET'])
def transform_branch_push():
    """
    Push branchs data to pub-sub.
    """
    logging.info("########## branch DATA PUBLISHING START#########")
    query_job = bigquery.branch_list()

    for row in query_job:  # API request - fetches results
        logging.info("******** NEW ROW START**********")
        logging.info("PUB_SUB:PUSH:branch_NAME {}".format(row))
        data = {
            "branch_id": row['branch_id'],
            "branch": row['branch'],
            "old_branch_name": row['old_branch_name'],
        }
        logging.info("Data = {}".format(data))
        pubsub.publish_transform_branch(data)
        logging.info("******** NEW ROW END**********")
    logging.info("########## branch DATA PUBLISHING STOP#########")
    return "Transform branch Push Calling..."

if __name__ == '__main__':
    # This is used when running locally. Gunicorn is used to run the
    # application on Google App Engine. See entrypoint in app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)

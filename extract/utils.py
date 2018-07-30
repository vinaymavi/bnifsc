
import logging
import base64
import json
logging.getLogger().setLevel(logging.INFO)

def b64encode(data):
    return base64.b64encode(data)

def b64decode(data):
    return base64.b64decode(data)

def get_request_data(data):
    if 'message' in data and 'data' in data['message']:
        message_str = data['message']['data']
        logging.info("ENCODED STRING = {}".format(message_str))
        message_data = b64decode(message_str)
        logging.info("DECODED STRING = {}".format(message_data))
        json_data = json.loads(message_data)
        logging.info("JSON DATA = {}".format(json_data))
        return json_data

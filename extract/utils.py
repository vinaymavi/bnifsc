
import logging
import base64
logging.getLogger().setLevel(logging.INFO)

def get_request_data(data):
    if 'message' in data and 'data' in data['message']:
        message_str = data['message']['data']
        logging.info("ENCODED STRING = {}".format(message_str))
        message_data = base64.b64decode(message_str)
        logging.info("DECODED STRING = {}".format(message_data))
        return message_data
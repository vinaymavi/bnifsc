
import base64
import logging

def temp_load_req(request):
    logging.getLogger().setLevel(logging.INFO)
    data = request.get_json()
    logging.info("TEMP LOAD REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    if 'message' in data and 'data' in data['message']:
        message_str = data['message']['data']
        logging.info("ENCODED STRING = {}".format(message_str))
        message_data = base64.b64decode(message_str)
        logging.info("DECODED STRING = {}".format(message_data))
    logging.info("TEMP LOAD REQUEST START")
    return "Temp load request calling..."

# Function deployment command.(currently --runtime flag no supported by cloud sdk.)
# please use cloud console.
# gcloud beta functions deploy temp_load_req --runtime python37 --trigger-http
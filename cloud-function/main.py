
import base64
import logging

def temp_load_req(data,context):
    logging.getLogger().setLevel(logging.INFO)    
    logging.info("TEMP LOAD REQUEST START")
    logging.info("JSON DATA = {}".format(data))
    if 'data' in data:
        message_str = data['data']
        logging.info("ENCODED STRING = {}".format(message_str))
        message_data = base64.b64decode(message_str)
        logging.info("DECODED STRING = {}".format(message_data))
    logging.info("TEMP LOAD REQUEST END")
    return "Temp load request calling..."

# Function deployment command.(currently --runtime flag no supported by cloud sdk.)
# please use cloud console.
# gcloud beta functions deploy temp_load_req --runtime python37  --trigger-resource load-temp --trigger-event google.pubsub.topic.publish --project bnifsc-beta_req --runtime python37  --trigger-resource load-temp --trigger-event google.pubsub.topic.publish --project bnifsc-beta
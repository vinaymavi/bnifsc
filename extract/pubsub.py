from google.cloud import pubsub_v1
import json
import logging
logging.getLogger().setLevel(logging.INFO)

# TODO this values should read from configuration.
PROJECT = 'bnifsc-beta'
#pub-sub topics
PUB_SUB_TOPIC_BANK_TRANSFORM = 'transform-bank'
PUB_SUB_TOPIC_STATE_TRANSFORM = 'transform-state'
PUB_SUB_TOPIC_DISTRICT_TRANSFORM = 'transform-district'
PUB_SUB_TOPIC_CITY_TRANSFORM = 'transform-city'
PUB_SUB_TOPIC_BRANCH_TRANSFORM = 'transform-branch'

PUB_SUB_TOPIC_BANK_LOAD = 'load-bank'
PUB_SUB_TOPIC_STATE_LOAD = 'load-state'
PUB_SUB_TOPIC_DISTRICT_LOAD = 'load-district'
PUB_SUB_TOPIC_CITY_LOAD = 'load-city'
PUB_SUB_TOPIC_BRANCH_LOAD = 'load-branch'
PUB_SUB_TOPIC_TEMP_LOAD = 'load-temp'

publisher = pubsub_v1.PublisherClient()
# pub-sub topic paths
TOPIC_BANK_TRANSFORM_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_BANK_TRANSFORM)
TOPIC_STATE_TRANSFORM_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_STATE_TRANSFORM)
TOPIC_DISTRICT_TRANSFORM_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_DISTRICT_TRANSFORM)
TOPIC_CITY_TRANSFORM_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_CITY_TRANSFORM)
TOPIC_BRANCH_TRANSFORM_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_BRANCH_TRANSFORM)

TOPIC_BANK_LOAD_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_BANK_LOAD)
TOPIC_STATE_LOAD_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_STATE_LOAD)
TOPIC_DISTRICT_LOAD_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_DISTRICT_LOAD)
TOPIC_CITY_LOAD_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_CITY_LOAD)
TOPIC_BRANCH_LOAD_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_BRANCH_LOAD)
TOPIC_TEMP_LOAD_PATH = publisher.topic_path(
    PROJECT, PUB_SUB_TOPIC_TEMP_LOAD)


class PubSub(object):
    def __init__(self, *args, **kwargs):
        pass

    def publish_transform_bank(self, data):
        self.__publish(TOPIC_BANK_TRANSFORM_PATH, data)

    def publish_transform_state(self, data):
        self.__publish(TOPIC_STATE_TRANSFORM_PATH, data)

    def publish_transform_district(self, data):
        self.__publish(TOPIC_DISTRICT_TRANSFORM_PATH, data)

    def publish_transform_city(self, data):
        self.__publish(TOPIC_CITY_TRANSFORM_PATH, data)

    def publish_transform_branch(self, data):
        self.__publish(TOPIC_BRANCH_TRANSFORM_PATH, data)

    def publish_load_bank(self, data):
        self.__publish(TOPIC_TEMP_LOAD_PATH, data)

    def publish_load_state(self, data):
        self.__publish(TOPIC_TEMP_LOAD_PATH, data)

    def publish_load_district(self, data):
        self.__publish(TOPIC_TEMP_LOAD_PATH, data)

    def publish_load_city(self, data):
        self.__publish(TOPIC_TEMP_LOAD_PATH, data)

    def publish_load_branch(self, data):
        self.__publish(TOPIC_TEMP_LOAD_PATH, data)

    def __publish(self, topic_path, data):
        if isinstance(data, basestring):
            logging.info("THIS IS {} a String instacne".format(data))

        data_str = data if isinstance(data, basestring) else json.dumps(data)
        logging.info("Data Str = {}".format(data_str))
        publisher.publish(topic_path, data_str)

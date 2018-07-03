const PubSub = require("@google-cloud/pubsub");
const logging = require("./logging");

const pubsub = PubSub({
  projectId: process.env["GOOGLE_CLOUD_PROJECT"] || "bnifsc-beta"
});

/**
 * Push data to pubsub server
 * @param {String} topic_name
 * @param {Object} data
 */
function publish_data(topic_name, data) {
  const topic = pubsub.topic(topic_name);
  const publisher = topic.publisher();
  return publisher.publish(Buffer.from(JSON.stringify(data)));
}

const BnifscPubSub = {
  name:"BnifscPubsub",
  push_data_to_prepration: data => {
    const TOPIC_NAME = "nodejs";
    return publish_data(TOPIC_NAME, data);
  },
  push_data_to_upload: data => {
    const TOPIC_NAME = "";
    return publish_data(TOPIC_NAME, data);
  }
};

module.exports = BnifscPubSub;

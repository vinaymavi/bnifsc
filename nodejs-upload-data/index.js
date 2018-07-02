"use strict";
// TODO test cases pending.
// [START app]
const express = require("express");
var bodyParser = require("body-parser");
const PubSub = require("@google-cloud/pubsub");
const BigQuery = require("@google-cloud/bigquery");

const logging = require("./lib/logging");
const app = express();
const bigquery = new BigQuery();

const query = "SELECT *  FROM `bnifsc-beta:BNIFSC_IFSC_CODE.ifsc_sample_data`";

// Add the request logger before anything else so that it can
// accurately log requests.
// [START requests]
app.use(logging.requestLogger);

app.use(bodyParser.json());

const TOPIC_NAME = "nodejs";
const pubsub = PubSub({
  projectId: "bnifsc-beta"
});

app.get("/", (req, res) => {
  res
    .status(200)
    .send("Hello, world!")
    .end();
});

app.get("/nodejs", (req, res) => {
  const topic = pubsub.topic(TOPIC_NAME);
  const publisher = topic.publisher();
  publisher.publish(
    Buffer.from(JSON.stringify({ msg: "Hello World" })),
    err => {
      if (err) {
        console.log("Error occurred while queuing background task", err);
        res
          .status(500)
          .send("Server Error")
          .end();
      } else {
        console.log("Data pushed successfully.");
        res
          .status(200)
          .send("Data pushed successfully.")
          .end();
      }
    }
  );
});

app.get("/nodejs/subscriber", (req, res) => {
  res
    .status(200)
    .send("GET - subscriber calling")
    .end();
});

app.get("/nodejs/bigquery", (req, res) => {
  bigquery
    .createQueryStream(query)
    .on("error", err => {
      logging.error(JSON.stringify(err));
      res.status(500).send(JSON.stringify(err));
    })
    .on("data", function(row) {
      logging.info(JSON.stringify(row));
    })
    .on("end", function() {
      logging.info("QUERY STREAM END");
      res.status(200).send("Data loaded from bigquery.");
    });
});

app.post("/nodejs/subscriber", (req, res) => {
  logging.info(JSON.stringify(req.body));
  // data is base64 encoded
  const message_data = Buffer.from(req.body.message.data, "base64").toString();
  // const message_data = req.body.message.data;
  logging.info("New Build.");
  logging.info(`sent message =${message_data}`);
  res
    .status(200)
    .send(
      `Post - subscriber calling, data received=${JSON.stringify(req.body)}`
    )
    .end();
});

// Add the error logger after all middleware and routes so that
// it can log errors from the whole application. Any custom error
// handlers should go after this.
// [START errors]
app.use(logging.errorLogger);

// Start the server
const PORT = process.env.PORT || 8080;

app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log("Press Ctrl+C to quit.");
});

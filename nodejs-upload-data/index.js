"use strict";

// [START app]
const express = require("express");
const PubSub = require("@google-cloud/pubsub");
const app = express();
const TOPIC_NAME = "nodejs";
const pubsub = PubSub({
  projectId: 'bnifsc-beta'
});

app.get("/", (req, res) => {
  res
    .status(200)
    .send("Hello, world!")
    .end();
});

app.get("/nodejs", (req, res) => {
  console.log(req.body);
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
          .send("Hello, world!")
          .end();
      }
    }
  );
});

app.get("/nodejs/subscriber", (req, res) => {
  console.log(req.body);
  res
    .status(200)
    .send("GET - subscriber calling")
    .end();
});

app.post("/nodejs/subscriber", (req, res) => {
  console.log(req.body);
  res
    .status(200)
    .send("Post - subscriber calling")
    .end();
});

// Start the server
const PORT = process.env.PORT || 8080;

app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log("Press Ctrl+C to quit.");
});

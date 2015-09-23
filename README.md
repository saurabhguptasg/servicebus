# servicebus
***

demo app to connect with azure and aws service buses

This app is to be used in conjuction with the [cf-DotNetContoso app](https://github.com/saurabhguptasg/cf-DotNetContoso "cf-DotNetContoso") and serves as the message pump into the Azure Service Bus queue that has been made availabe as a user provided service (please refer to the appropriate `cf cups` command in the above repo's README file)

To inject messages into the queue, invoke this endpoint:

    http://bus.domain.com/sb/send

The above command will pump 10 messages into the queue with random data. if you want a continuous pump, you can invoke the above endpoint via a `curl` command

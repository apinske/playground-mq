# playground-mq
* Run MQ `docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq`
* Configure Backout `curl -k -u admin:passw0rd https://localhost:9443/ibmmq/console/internal/ibmmq/qmgr/QM1/queue/DEV.QUEUE.1 -X PUT -H 'Content-Type: application/json;charset=utf-8' -H 'ibm-mq-csrf-token: value' --data '{"attributes":{"MQCA_BACKOUT_REQ_Q_NAME":"DEV.QUEUE.2","MQIA_BACKOUT_THRESHOLD":3},"type":"MQQT_LOCAL"}'`
* Run Test `mvn clean test`

```
2022-01-28 23:18:58.244  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610034b610 (1): ok
2022-01-28 23:18:58.298  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610034b610 (2): ok
2022-01-28 23:18:58.343  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610034b610 (3): ok
2022-01-28 23:18:58.764  INFO 44937 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020208c68f4610034b610
2022-01-28 23:19:03.245  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610134b610 (1): fail
2022-01-28 23:19:03.302  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610134b610 (2): fail
2022-01-28 23:19:03.359  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610134b610 (3): fail
2022-01-28 23:19:03.425  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610234b610 (1): fail
2022-01-28 23:19:03.488  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610234b610 (2): fail
2022-01-28 23:19:03.549  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610234b610 (3): fail
2022-01-28 23:19:03.636  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610334b610 (1): fail
2022-01-28 23:19:03.732  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610334b610 (2): fail
2022-01-28 23:19:03.808  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610334b610 (3): fail
2022-01-28 23:19:03.897  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610434b610 (1): fail
2022-01-28 23:19:03.993  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610434b610 (2): fail
2022-01-28 23:19:04.092  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610434b610 (3): fail
2022-01-28 23:19:04.228  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610534b610 (1): fail
2022-01-28 23:19:04.340  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610534b610 (2): fail
2022-01-28 23:19:04.449  INFO 44937 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020208c68f4610534b610 (3): fail
2022-01-28 23:19:09.570  INFO 44937 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020208c68f4610134b610
2022-01-28 23:19:09.586  INFO 44937 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020208c68f4610234b610
2022-01-28 23:19:09.600  INFO 44937 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020208c68f4610334b610
2022-01-28 23:19:09.620  INFO 44937 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020208c68f4610434b610
2022-01-28 23:19:09.641  INFO 44937 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020208c68f4610534b610
```

* https://www.ibm.com/docs/en/ibm-mq/9.1?topic=applications-handling-poison-messages-in-mq-classes-jms

> If an application receives messages synchronously, by calling one of the following methods, the IBM MQ classes for JMS requeue a poison message within the unit of work that was active when the application tried to get the message:
> This means that if the application is using either a transacted JMS context or session, then the moving of the message to the backout queue is not committed until the transaction is committed.

> If an application is receiving messages asynchronously via a MessageListener, the IBM MQ classes for JMS requeue poison messages without affecting message delivery. The requeue process takes place outside of any unit of work associated with actual message delivery to the application.

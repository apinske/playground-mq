# playground-mq
* Run MQ `docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq`
* Configure Backout `curl -k -u admin:passw0rd https://localhost:9443/ibmmq/console/internal/ibmmq/qmgr/QM1/queue/DEV.QUEUE.1 -X PUT -H 'Content-Type: application/json;charset=utf-8' -H 'ibm-mq-csrf-token: value' --data '{"attributes":{"MQCA_BACKOUT_REQ_Q_NAME":"DEV.QUEUE.2","MQIA_BACKOUT_THRESHOLD":3},"type":"MQQT_LOCAL"}'`
* Run Test `mvn clean test`
    * set `playground.mq-workaround.enabled=false` to reproduce issue

* https://www.ibm.com/docs/en/ibm-mq/9.1?topic=applications-handling-poison-messages-in-mq-classes-jms

> If an application receives messages synchronously, by calling one of the following methods, the IBM MQ classes for JMS requeue a poison message within the unit of work that was active when the application tried to get the message:
> This means that if the application is using either a transacted JMS context or session, then the moving of the message to the backout queue is not committed until the transaction is committed.

> If an application is receiving messages asynchronously via a MessageListener, the IBM MQ classes for JMS requeue poison messages without affecting message delivery. The requeue process takes place outside of any unit of work associated with actual message delivery to the application.

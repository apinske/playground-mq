# playground-mq
* Run MQ `docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq`
* Configure Backout (3 -> DEV.QUEUE.2): https://localhost:9443/ibmmq/console/#/qmgr/QM1/queue/local/DEV.QUEUE.1/configuration/properties
* Run Test `mvn clean test`

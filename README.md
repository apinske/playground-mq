# playground-mq
* Run MQ `docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq`
* Configure Backout (3 -> DEV.QUEUE.2): https://localhost:9443/ibmmq/console/#/qmgr/QM1/queue/local/DEV.QUEUE.1/configuration/properties
* Run Test `mvn clean test`
* Observe Log 
```
2022-01-28 21:04:33.403  INFO 40184 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020209fddf36100458815 (1): ok
2022-01-28 21:04:33.454  INFO 40184 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020209fddf36100458815 (2): ok
2022-01-28 21:04:33.495  INFO 40184 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020209fddf36100458815 (3): ok
2022-01-28 21:04:33.600  INFO 40184 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : received backout message: ID:414d5120514d312020202020202020209fddf36100458815
2022-01-28 21:04:38.420  INFO 40184 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020209fddf36101458815 (1): fail
2022-01-28 21:04:38.458  INFO 40184 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020209fddf36101458815 (2): fail
2022-01-28 21:04:38.505  INFO 40184 --- [ntContainer#1-1] e.p.p.p.PlaygroundMqApplication$Listener : received message: ID:414d5120514d312020202020202020209fddf36101458815 (3): fail
```

* Sequence
  * MQ enlist
  * TX.rollback
    * RM.delist (with TM_FAIL) -> XA_RBROLLBACK
    * RM.rollback -> XAER_NOTA

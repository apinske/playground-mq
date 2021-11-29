# playground-mq
* Run MQ `docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq`
* Run Test `mvn clean test`
* Observe Warn-Log 
```
2021-11-29 21:29:41.425  WARN 58282 --- [ntContainer#0-1] com.arjuna.ats.jta                       : ARJUNA016045: attempted rollback of < formatId=131077, gtrid_length=29, bqual_length=36, tx_uid=0:ffffc0a86415:f529:61a53832:10, node_name=1, branch_uid=0:ffffc0a86415:f529:61a53832:12, subordinatenodename=null, eis_name=0 > (com.ibm.mq.jmqi.JmqiXAResource@41393ba0) failed with exception code XAException.XAER_NOTA

javax.transaction.xa.XAException: The method 'xa_rollback' has failed with errorCode '-4'.
	at com.ibm.mq.jmqi.JmqiXAResource.rollback(JmqiXAResource.java:881) ~[com.ibm.mq.allclient-9.2.4.0.jar:9.2.4.0 - p924-L211104]
	at com.arjuna.ats.internal.jta.resources.arjunacore.XAResourceRecord.topLevelAbort(XAResourceRecord.java:362) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.arjuna.coordinator.BasicAction.doAbort(BasicAction.java:3032) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.arjuna.coordinator.BasicAction.doAbort(BasicAction.java:3011) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.arjuna.coordinator.BasicAction.Abort(BasicAction.java:1674) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator.cancel(TwoPhaseCoordinator.java:124) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.arjuna.AtomicAction.abort(AtomicAction.java:186) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple.rollbackAndDisassociate(TransactionImple.java:1377) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at com.arjuna.ats.internal.jta.transaction.arjunacore.BaseTransaction.rollback(BaseTransaction.java:143) ~[narayana-jta-5.10.6.Final.jar:5.10.6.Final (revision: 8702d)]
	at org.springframework.transaction.jta.JtaTransactionManager.doRollback(JtaTransactionManager.java:1062) ~[spring-tx-5.3.13.jar:5.3.13]
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.processRollback(AbstractPlatformTransactionManager.java:835) ~[spring-tx-5.3.13.jar:5.3.13]
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:699) ~[spring-tx-5.3.13.jar:5.3.13]
	at org.springframework.jms.listener.AbstractPollingMessageListenerContainer.receiveAndExecute(AbstractPollingMessageListenerContainer.java:251) ~[spring-jms-5.3.13.jar:5.3.13]
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.invokeListener(DefaultMessageListenerContainer.java:1237) ~[spring-jms-5.3.13.jar:5.3.13]
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.executeOngoingLoop(DefaultMessageListenerContainer.java:1227) ~[spring-jms-5.3.13.jar:5.3.13]
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.run(DefaultMessageListenerContainer.java:1120) ~[spring-jms-5.3.13.jar:5.3.13]
	at java.base/java.lang.Thread.run(Thread.java:829) ~[na:na]
```

* Sequence
  * MQ enlist
  * TX.rollback
    * RM.delist (with TM_FAIL) -> XA_RBROLLBACK
    * RM.rollback -> XAER_NOTA

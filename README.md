# playground-mq
* Run Test `mvn clean test`
* Observe Log (sample, race-condition)
```
2022-01-19 00:53:20.676  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener :    new Thing [id=1, name=0123456789abcdefghijklmnopqrstuvwxyz]
2022-01-19 00:53:20.767  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrstuvwxy]
2022-01-19 00:53:20.795  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrstuvwx]
2022-01-19 00:53:20.820  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrstuvw]
2022-01-19 00:53:20.837  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrstuv]
2022-01-19 00:53:20.851  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrstu]
2022-01-19 00:53:20.874  INFO 48054 --- [ntContainer#0-4] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrst]
2022-01-19 00:53:20.890  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqrs]
2022-01-19 00:53:20.909  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopqr]
2022-01-19 00:53:20.918  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnopq]
2022-01-19 00:53:20.927 ERROR 48054 --- [ntContainer#0-4] e.p.p.p.PlaygroundMqApplication$Listener : failed Thing [id=1, name=0123456789abcdefghijklmnopqr], expected 0123456789abcdefghijklmnopq
2022-01-19 00:53:20.935  WARN 48054 --- [ntContainer#0-4] o.s.j.l.DefaultMessageListenerContainer  : Setup of JMS message listener invoker failed for destination 'Queue' - trying to recover. Cause: JTA transaction unexpectedly rolled back (maybe due to a timeout); nested exception is javax.transaction.RollbackException: ARJUNA016053: Could not commit transaction.
2022-01-19 00:53:20.938  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmnop]
2022-01-19 00:53:20.947  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmno]
2022-01-19 00:53:20.957  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklmn]
2022-01-19 00:53:20.970  INFO 48054 --- [ntContainer#0-5] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijklm]
2022-01-19 00:53:20.981  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijkl]
2022-01-19 00:53:20.990  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghijk]
2022-01-19 00:53:21.000  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghij]
2022-01-19 00:53:21.010  INFO 48054 --- [ntContainer#0-5] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefghi]
2022-01-19 00:53:21.019  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefgh]
2022-01-19 00:53:21.030  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdefg]
2022-01-19 00:53:21.040  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcdef]
2022-01-19 00:53:21.048  INFO 48054 --- [ntContainer#0-5] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcde]
2022-01-19 00:53:21.057  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abcd]
2022-01-19 00:53:21.069  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789abc]
2022-01-19 00:53:21.078  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789ab]
2022-01-19 00:53:21.087  INFO 48054 --- [ntContainer#0-5] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789a]
2022-01-19 00:53:21.094  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456789]
2022-01-19 00:53:21.103  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=012345678]
2022-01-19 00:53:21.113  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=01234567]
2022-01-19 00:53:21.122  INFO 48054 --- [ntContainer#0-5] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123456]
2022-01-19 00:53:21.133  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=012345]
2022-01-19 00:53:21.140  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=01234]
2022-01-19 00:53:21.148  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0123]
2022-01-19 00:53:21.157  INFO 48054 --- [ntContainer#0-5] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=012]
2022-01-19 00:53:21.166  INFO 48054 --- [ntContainer#0-3] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=01]
2022-01-19 00:53:21.178  INFO 48054 --- [ntContainer#0-2] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=0]
2022-01-19 00:53:21.185  INFO 48054 --- [ntContainer#0-1] e.p.p.p.PlaygroundMqApplication$Listener : update Thing [id=1, name=]
```

* possible Sequence
  * TX1.MQ commit
  * TX2.MQ read
  * TX2.DB read
  * TX1.DB commit

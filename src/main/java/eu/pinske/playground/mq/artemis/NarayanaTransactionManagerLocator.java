package eu.pinske.playground.mq.artemis;

import javax.transaction.TransactionManager;

import org.apache.activemq.artemis.service.extensions.transactions.TransactionManagerLocator;

public class NarayanaTransactionManagerLocator implements TransactionManagerLocator {
    @Override
    public TransactionManager getTransactionManager() {
        return com.arjuna.ats.jta.TransactionManager.transactionManager();
    }
}

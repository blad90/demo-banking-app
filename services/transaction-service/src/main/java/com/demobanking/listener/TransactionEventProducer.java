package com.demobanking.listener;

import com.demobanking.entity.Transaction;
import com.demobanking.events.Transactions;
import com.demobanking.events.Transactions.TransactionState;
import com.demobanking.events.Transactions.TransactionCreatedEvent;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionEventProducer {

    private final KafkaTemplate<String, byte[]> template;

    public void publishTransactionCreated(String sagaId, Transaction transaction){
        TransactionCreatedEvent transactionCreatedEvent = TransactionCreatedEvent.newBuilder()
                .setSourceAccountNumber(transaction.getSourceAccountNumber())
                .setDestinationAccountNumber(transaction.getDestinationAccountNumber())
                .setCorrelationId(String.valueOf(transaction.getCorrelationId()))
                .setTransactionType(Transactions.TransactionType.TRANSFER)
                .setTransactionState(TransactionState.TRAN_COMPLETED)
                .setAmount(String.valueOf(transaction.getTransactionAmount()))
                .setDescription(transaction.getDescription())
                .setSagaId(sagaId)
                .build();
        byte[] data = transactionCreatedEvent.toByteArray();
        template.send("TRANSACTION_CREATED_EVENTS_TOPIC", sagaId, data);
    }

//    public void publishAccountNotCreated(){
//        template.send("ACCOUNT_NOT_CREATED_EVENTS_TOPIC", "ERROR TRYING CREATING ACCOUNT", "Account cannot be created");
//    }
}

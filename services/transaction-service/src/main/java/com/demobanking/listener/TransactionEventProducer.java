package com.demobanking.listener;

import com.demobanking.entity.Transaction;
import com.demobanking.events.Transactions.TransactionState;
import com.demobanking.events.Transactions.TransactionCreatedEvent;
import com.demobanking.events.Transactions.TransactionCancelledEvent;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionEventProducer {

    private final KafkaTemplate<String, byte[]> template;

    public void publishTransactionCreated(String sagaId, Transaction transaction){
        TransactionCreatedEvent.Builder builder = TransactionCreatedEvent.newBuilder()
                .setSourceAccountNumber(transaction.getSourceAccountNumber())
                .setCorrelationId(String.valueOf(transaction.getCorrelationId()))
                .setTransactionType(transaction.getType())
                .setTransactionState(TransactionState.TRAN_COMPLETED)
                .setAmount(String.valueOf(transaction.getTransactionAmount()))
                .setDescription(transaction.getDescription())
                .setSagaId(sagaId);
        // DEBIT/CREDIT transactions only involve one account - transfers are the only case with a destination.
        if (transaction.getDestinationAccountNumber() != null) {
            builder.setDestinationAccountNumber(transaction.getDestinationAccountNumber());
        }
        byte[] data = builder.build().toByteArray();
        template.send("TRANSACTION_CREATED_EVENTS_TOPIC", sagaId, data);
    }

    public void publishTransactionCancelled(String sagaId, UUID correlationId){
        TransactionCancelledEvent transactionCancelledEvent = TransactionCancelledEvent.newBuilder()
                .setSagaId(sagaId)
                .setCorrelationId(String.valueOf(correlationId))
                .build();
        byte[] data = transactionCancelledEvent.toByteArray();
        template.send("TRANSACTION_CANCELLED_EVENTS_TOPIC", sagaId, data);
    }

//    public void publishAccountNotCreated(){
//        template.send("ACCOUNT_NOT_CREATED_EVENTS_TOPIC", "ERROR TRYING CREATING ACCOUNT", "Account cannot be created");
//    }
}

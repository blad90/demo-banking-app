package com.banking.transaction.listener;

import com.banking.transaction.entity.Transaction;
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

    private final KafkaTemplate<String, Message> template;

    public void publishTransactionCreated(Transaction transaction){
        TransactionCreatedEvent transactionCreatedEvent = TransactionCreatedEvent.newBuilder()
                .setSourceAccountNumber(transaction.getSourceAccountNumber())
                .setDestinationAccountNumber(transaction.getDestinationAccountNumber())
                .setCorrelationId(String.valueOf(transaction.getCorrelationId()))
                .setTransactionType(Transactions.TransactionType.TRANSFER)
                .setTransactionState(TransactionState.TRAN_COMPLETED)
                .setAmount(String.valueOf(transaction.getTransactionAmount()))
                .setDescription(transaction.getDescription())
                .build();
        template.send("TRANSACTION_CREATED_EVENTS_TOPIC", String.valueOf(transaction.getId()), transactionCreatedEvent);
    }

//    public void publishAccountNotCreated(){
//        template.send("ACCOUNT_NOT_CREATED_EVENTS_TOPIC", "ERROR TRYING CREATING ACCOUNT", "Account cannot be created");
//    }
}

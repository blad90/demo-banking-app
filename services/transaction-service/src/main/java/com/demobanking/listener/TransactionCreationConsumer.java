package com.demobanking.listener;

import com.demobanking.service.ITransactionService;
import com.demobanking.events.Transactions.TransferCommand;
import com.demobanking.events.Transactions.CreateTransactionCommand;
import com.demobanking.events.Transactions.CancelTransactionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionCreationConsumer {

    private final ITransactionService transactionService;

    @KafkaListener(topics = "CREATE_TRANSACTION_CMD",
            groupId = "TRANSACTION_EVENT_GROUP",
            containerFactory = "createTransactKafkaListenerContainerFactory")
    public void onTransactionCreateEvent(CreateTransactionCommand createAccountCommand) {
        transactionService.createTransaction(createAccountCommand);
    }
    @KafkaListener(topics = "TRANSFER_CMD",
            groupId = "TRANSACTION_EVENT_GROUP",
            containerFactory = "transferKafkaListenerContainerFactory")
    public void onTransferEvent(TransferCommand transferCommand){
        transactionService.transfer(transferCommand.getSagaId(), transferCommand);
    }

    @KafkaListener(topics = "CANCEL_TRANSACTION_CMD",
            groupId = "TRANSACTION_EVENT_GROUP",
            containerFactory = "cancelTransactionKafkaListenerContainerFactory")
    public void onCancelTransactionEvent(CancelTransactionCommand cancelTransactionCommand){
        transactionService.cancelTransactionByCorrelationId(cancelTransactionCommand);
    }
}

package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printGreen;
import static com.renata.demoartifactor.appui.PrintUI.printPurple;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRed;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellow;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.TransactionService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class TransactionView implements Renderable {

    private final ServiceFactory serviceFactory;

    public TransactionView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public void displayTransactions() {
        TransactionService transactionService = serviceFactory.getTransactionService();

        Set<Transaction> transactionsSet = transactionService.getAll(transaction -> true);

        if (transactionsSet.isEmpty()) {
            printRedMessage("Немає жодної транзакції.");
        } else {
            List<Transaction> transactionList = new ArrayList<>(transactionsSet);
            transactionList.sort(Comparator.comparing(transaction -> transaction.getDate()));

            printBlueMessage(
                "Користувач - тип транзакції - предмет - ціна предмету - дата транзакції (yyyy-mm-dd)");

            for (int i = 0; i < transactionList.size(); i++) {
                Transaction transaction = transactionList.get(i);
                String username = transaction.getUser().getUsername();
                String transactionType = transaction.getType().getName();
                String itemName = transaction.getItem().getName();
                double value = transaction.getValue();
                String date = transaction.getFormattedDate();

                System.out.println(printRed(username + ": ")
                    + printYellow(transactionType) + " - " + "'" + itemName + "'" + " за "
                    + printGreen(value + " $") + " здійснено " + printPurple(date));
            }
        }
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Транзакції ===");
        displayTransactions();
    }
}

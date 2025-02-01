package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import java.io.IOException;

public final class TransactionView implements Renderable {

    private final ServiceFactory serviceFactory;

    public TransactionView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    //TODO show all the transaction history, export it to an .xls file after closing the app
    // update every time the app is closed if a new transaction was made
    // only available for admins
    // only readable (readOnly)

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Транзакції ==="); //ваші колекції
        //displayTransactions();
    }
}

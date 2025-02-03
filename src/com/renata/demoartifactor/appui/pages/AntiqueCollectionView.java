package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

import com.renata.demoartifactor.appui.PageFactory;
import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public final class AntiqueCollectionView implements Renderable {

    private final ServiceFactory serviceFactory;

    public AntiqueCollectionView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void displayUserCollections() {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        AuthService authService = serviceFactory.getAuthService();

        User currentUser = authService.getUser();
        List<AntiqueCollection> collections = antiqueCollectionService.getAllByOwner(
            currentUser.getUsername());

        if (collections.isEmpty()) {
            printRedMessage("У вас ще немає колекцій.");
        } else {
            for (int i = 0; i < collections.size(); i++) {
                AntiqueCollection collection = collections.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, collection.getName(),
                    collection.getDescription());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print(
                printBlue("Виберіть номер колекції для перегляду (0 для повернення): "));
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= collections.size()) {
                AntiqueCollection selectedCollection = collections.get(choice - 1);
                viewSingleCollection(selectedCollection);
            } else if (choice == 0) {
                printYellowMessage("Повернення до головного меню...");
            } else {
                printRedMessage("Невірний номер, спробуйте ще раз.");
            }
        }
    }

    private void viewSingleCollection(AntiqueCollection collection) {
        printYellowMessage("\n==== Деталі колекції ====");
        System.out.println(printBlue("Назва: ") + collection.getName());
        System.out.println(printBlue("Опис: ") + collection.getDescription());
        printYellowMessage("\n=== Предмети колекції ===");

        PageFactory pageFactory = new PageFactory(serviceFactory);
        Renderable itemViewPage = pageFactory.createItemView(collection);
        try {
            itemViewPage.render();
        } catch (IOException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Доступні колекції ===");
        displayUserCollections();
    }
}
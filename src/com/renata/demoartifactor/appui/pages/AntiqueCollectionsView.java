package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

import com.renata.demoartifactor.appui.PageFactory;
import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public final class AntiqueCollectionsView implements Renderable {

    private final ServiceFactory serviceFactory;

    public AntiqueCollectionsView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void displayAllCollections() {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();

        Set<AntiqueCollection> collectionsSet = antiqueCollectionService.getAll();

        if (collectionsSet.isEmpty()) {
            printRedMessage("Немає доступних колекцій.");
        } else {
            List<AntiqueCollection> collectionsList = collectionsSet.stream()
                .collect(Collectors.toList());

            for (int i = 0; i < collectionsList.size(); i++) {
                AntiqueCollection collection = collectionsList.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, collection.getName(),
                    collection.getOwner().getUsername());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print(
                printBlue("Виберіть номер колекції для перегляду (0 для повернення): "));
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= collectionsList.size()) {
                AntiqueCollection selectedCollection = collectionsList.get(choice - 1);
                viewSingleCollection(selectedCollection);
                viewItemsInCollection(selectedCollection);
            } else if (choice == 0) {
                printYellowMessage("Повернення до головного меню.");
            } else {
                printRedMessage("Невірний номер, спробуйте ще раз.");
            }
        }
    }

    private void viewSingleCollection(AntiqueCollection collection) {
        printYellowMessage("\n=== Деталі колекції ====");
        System.out.println(printBlue("Назва: ") + collection.getName());
        System.out.println(printBlue("Опис: ") + collection.getDescription());
        System.out.println(printBlue("Власник: ") + collection.getOwner().getUsername());
    }

    private void viewItemsInCollection(AntiqueCollection collection) {
        printYellowMessage("\n=== Предмети колекції ====");
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
        printBlueMessage("Назва колекції - Власник");
        displayAllCollections();
    }
}

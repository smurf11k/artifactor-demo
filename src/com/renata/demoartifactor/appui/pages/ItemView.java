package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printHeader;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public final class ItemView implements Renderable {

    private final ServiceFactory serviceFactory;
    private final AntiqueCollection selectedCollection;

    public ItemView(ServiceFactory serviceFactory, AntiqueCollection selectedCollection) {
        this.serviceFactory = serviceFactory;
        this.selectedCollection = selectedCollection;
    }

    private void displayItemsInCollection(AntiqueCollection collection) {
        ItemService itemService = serviceFactory.getItemService();
        List<Item> items = itemService.getAllByCollection(collection);

        if (items.isEmpty()) {
            printRedMessage("У колекції немає предметів.");
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, item.getName(), item.getDescription());
            }

            Scanner scanner = new Scanner(System.in);
            printHeader("Виберіть номер предмета для перегляду (0 для повернення): ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= items.size()) {
                Item selectedItem = items.get(choice - 1);
                viewSingleItem(selectedItem);
            } else if (choice == 0) {
                printYellowMessage("Повернення до колекцій.");
            } else {
                printRedMessage("Невірний номер, спробуйте ще раз.");
            }
        }
    }

    public void viewSingleItem(Item item) {
        printYellowMessage("=== Деталі предмета ====");
        System.out.println("Назва: " + item.getName());
        System.out.println("Опис: " + item.getDescription());
        System.out.println("Тип: " + item.getItemType().getName());
        System.out.println("Вартість: " + item.getValue() + " $");
        System.out.println("Рік створення: " + item.getCreatedDate());
        System.out.println("Дата отримання: " + item.getDateAquired());
        System.out.println("Колекція: " + item.getCollection().getName());
    }

    @Override
    public void render() throws IOException {
        displayItemsInCollection(selectedCollection);
    }
}

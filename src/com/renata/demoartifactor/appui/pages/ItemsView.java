package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printHeader;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public final class ItemsView implements Renderable {

    private final ServiceFactory serviceFactory;

    public ItemsView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void displayItems() {
        ItemService itemService = serviceFactory.getItemService();

        Set<Item> allItemsSet = itemService.getAll(item -> true);

        if (allItemsSet.isEmpty()) {
            System.out.println("Немає доступних предметів.");
        } else {
            List<Item> allItems = new ArrayList<>(allItemsSet);

            for (int i = 0; i < allItems.size(); i++) {
                Item item = allItems.get(i);
                System.out.printf("%d. %s - %s - %s $ - %s%n", i + 1, item.getName(),
                    item.getCollection().getName(), item.getValue(),
                    item.getCollection().getOwner().getUsername());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Виберіть номер предмета для перегляду (0 для повернення): ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= allItems.size()) {
                Item selectedItem = allItems.get(choice - 1);
                viewSingleItem(selectedItem);
            } else if (choice == 0) {
                System.out.println("Повернення до головного меню.");
            } else {
                System.out.println("Невірний номер, спробуйте ще раз.");
            }
        }
    }

    private void viewSingleItem(Item item) {
        printYellowMessage("\n=== Деталі предмета ===");
        System.out.println("Назва: " + item.getName());
        System.out.println("Опис: " + item.getDescription());
        System.out.println("Тип: " + item.getItemType().getName());
        System.out.println("Вартість: " + item.getValue() + " $");
        System.out.println("Рік створення: " + item.getCreatedDate());
        System.out.println("Дата отримання: " + item.getDateAquired());
        System.out.println("Колекція: " + item.getCollection().getName());
        System.out.println("Власник колекції: " + item.getCollection().getOwner().getUsername());
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Доступні предмети ===");
        printHeader("Назва предмету - Колекція - Вартість предмету - Власник");
        displayItems();
    }
}

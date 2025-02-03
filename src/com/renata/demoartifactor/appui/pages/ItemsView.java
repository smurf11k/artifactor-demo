package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPromptBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
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
            printRedMessage("Немає доступних предметів.");
        } else {
            List<Item> allItems = new ArrayList<>(allItemsSet);

            for (int i = 0; i < allItems.size(); i++) {
                Item item = allItems.get(i);
                System.out.printf("%d. %s - %s - %s $ - %s%n", i + 1, item.getName(),
                    item.getCollection().getName(), item.getValue(),
                    item.getCollection().getOwner().getUsername());
            }

            Scanner scanner = new Scanner(System.in);
            printPromptBlue("Виберіть номер предмета для перегляду (0 для повернення): ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= allItems.size()) {
                Item selectedItem = allItems.get(choice - 1);
                viewSingleItem(selectedItem);
            } else if (choice == 0) {
                printYellowMessage("Повернення до головного меню...");
            } else {
                printRedMessage("Невірний номер, спробуйте ще раз.");
            }
        }
    }

    private void viewSingleItem(Item item) {
        printYellowMessage("\n=== Деталі предмета ===");
        System.out.println(printBlue("Назва: ") + item.getName());
        System.out.println(printBlue("Опис: ") + item.getDescription());
        System.out.println(printBlue("Тип: ") + item.getItemType().getName());
        System.out.println(printBlue("Вартість: ") + item.getValue() + " $");
        System.out.println(printBlue("Рік створення: ") + item.getCreatedDate());
        System.out.println(printBlue("Дата отримання: ") + item.getDateAquired());
        System.out.println(printBlue("Колекція: ") + item.getCollection().getName());
        System.out.println(
            printBlue("Власник колекції: ") + item.getCollection().getOwner().getUsername());
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Доступні предмети ===");
        printBlueMessage("Назва предмету - Колекція - Вартість предмету - Власник");
        displayItems();
    }
}

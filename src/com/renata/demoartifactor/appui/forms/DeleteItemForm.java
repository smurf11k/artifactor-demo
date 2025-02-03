package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellow;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public final class DeleteItemForm implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public DeleteItemForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void processItemDeletion() throws IOException {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        ItemService itemService = serviceFactory.getItemService();

        List<AntiqueCollection> collections = antiqueCollectionService.getAuthorizedUserCollections();

        if (collections.isEmpty()) {
            printRedMessage("У вас немає колекцій для видалення предметів.");
            return;
        }

        printBlueMessage("Доступні колекції для видалення предмету:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, collections.get(i).getName());
        }
        System.out.print(printBlue("Виберіть номер колекції з якої хочете видалити предмет: "));
        int collectionChoice = Integer.parseInt(reader.readLine());
        AntiqueCollection collection = collections.get(collectionChoice - 1);

        List<Item> userItems = itemService.getAllByCollection(collection);

        if (userItems.isEmpty()) {
            printRedMessage("У вас немає предметів в цій колекції.");
            return;
        }

        printBlueMessage("Доступні предмети для видалення:");
        for (int i = 0; i < userItems.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, userItems.get(i).getName());
        }
        System.out.print(printBlue("Виберіть номер предмета для видалення: "));
        int itemChoice = Integer.parseInt(reader.readLine());
        Item item = userItems.get(itemChoice - 1);

        System.out.println(printYellow("Видалення предмета: ") + item.getName());

        System.out.print(printBlue("Ви впевнені, що хочете видалити цей предмет? (+/-): "));
        String confirmation = reader.readLine();
        if (confirmation.equalsIgnoreCase("+")) {
            itemService.delete(item.getId());
            JsonRepositoryFactory.getInstance().commit();
            printGreenMessage("Предмет успішно видалено!");
        } else {
            printRedMessage("Видалення скасовано.");
        }
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Видалення предмета ===");
        try {
            processItemDeletion();
        } catch (IOException e) {
            System.err.println("Помилка при введенні даних: " + e.getMessage());
        }
    }
}

package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printHeader;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public final class DeleteCollectionForm implements Renderable {

    private final ServiceFactory serviceFactory;

    public DeleteCollectionForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void deleteCollection() {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        AuthService authService = serviceFactory.getAuthService();
        ItemService itemService = serviceFactory.getItemService();

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
            printHeader("Виберіть номер колекції для видалення (0 для повернення): ");
            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printRedMessage("Невірний формат введення.");
                return;
            }

            if (choice > 0 && choice <= collections.size()) {
                AntiqueCollection selectedCollection = collections.get(choice - 1);

                List<Item> itemsToDelete = itemService.getAllByCollection(selectedCollection);
                for (Item item : itemsToDelete) {
                    itemService.delete(item.getId());
                }

                antiqueCollectionService.delete(selectedCollection.getId());
                JsonRepositoryFactory.getInstance().commit();

                printGreenMessage("Колекцію та всі її предмети успішно видалено!");
            } else if (choice == 0) {
                printYellowMessage("Повернення до головного меню.");
            } else {
                printRedMessage("Невірний номер, спробуйте ще раз.");
            }
        }

    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Видалення колекції ===");
        deleteCollection();
    }
}

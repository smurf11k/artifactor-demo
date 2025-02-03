package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printPromptBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.ADD_COLLECTION;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.ADD_ITEM;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.DELETE_COLLECTION;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.DELETE_ITEM;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.EDIT_COLLECTION;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.EDIT_ITEM;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.EXIT;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.LOG_OUT;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.MARKETPLACE;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.VIEW_COLLECTION;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.VIEW_COLLECTIONS;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.VIEW_ITEMS;
import static com.renata.demoartifactor.appui.pages.MainMenuView.MainMenu.VIEW_TRANSACTIONS;

import com.renata.demoartifactor.appui.PageFactory;
import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.entity.impl.User.Role;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class MainMenuView implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final User.Role userRole;
    PageFactory pageFactory;
    ServiceFactory serviceFactory;

    public MainMenuView(Role userRole, ServiceFactory serviceFactory) {
        this.userRole = userRole;
        this.pageFactory = PageFactory.getInstance(serviceFactory);
        this.serviceFactory = serviceFactory;
    }

    private void process(MainMenu selectedItem) throws IOException {
        switch (selectedItem) {
            case VIEW_COLLECTION -> pageFactory.createAntiqueCollectionView().render();

            case ADD_COLLECTION -> pageFactory.createAddCollectionForm().render();

            case EDIT_COLLECTION -> pageFactory.createEditCollectionForm().render();

            case DELETE_COLLECTION -> pageFactory.createDeleteCollectionForm().render();

            case ADD_ITEM -> pageFactory.createAddItemForm().render();

            case EDIT_ITEM -> pageFactory.createEditItemForm().render();

            case DELETE_ITEM -> pageFactory.createDeleteItemForm().render();

            case MARKETPLACE -> pageFactory.createMarketplaceView(userRole).render();

            case VIEW_COLLECTIONS -> pageFactory.createAntiqueCollectionsView().render();

            case VIEW_ITEMS -> pageFactory.createItemsView().render();

            case VIEW_TRANSACTIONS -> pageFactory.createTransactionView().render();

            case LOG_OUT -> {
                printYellowMessage("Вихід з акаунту...");
                serviceFactory.getAuthService().logout();
                pageFactory.createAuthView().render();
            }
            case EXIT -> printRedMessage("Вихід з програми...");
            default -> printRedMessage("Неправильний вибір");
        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurpleMessage("\n\n=== Головне меню ===");
            System.out.println("1. " + VIEW_COLLECTION.getName());
            System.out.println("2. " + ADD_COLLECTION.getName());
            System.out.println("3. " + EDIT_COLLECTION.getName());
            System.out.println("4. " + DELETE_COLLECTION.getName());
            System.out.println("5. " + ADD_ITEM.getName());
            System.out.println("6. " + EDIT_ITEM.getName());
            System.out.println("7. " + DELETE_ITEM.getName());
            System.out.println("8. " + MARKETPLACE.getName());
            System.out.println("9. " + LOG_OUT.getName());
            if (userRole == Role.ADMIN) {
                System.out.println("10. " + VIEW_COLLECTIONS.getName());
                System.out.println("11. " + VIEW_ITEMS.getName());
                System.out.println("12. " + VIEW_TRANSACTIONS.getName());
            }
            System.out.println("0. " + EXIT.getName());
            printPromptBlue("Зробіть вибір: ");

            String choice = reader.readLine();
            MainMenu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> VIEW_COLLECTION;
                    case "2" -> ADD_COLLECTION;
                    case "3" -> EDIT_COLLECTION;
                    case "4" -> DELETE_COLLECTION;
                    case "5" -> ADD_ITEM;
                    case "6" -> EDIT_ITEM;
                    case "7" -> DELETE_ITEM;
                    case "8" -> MARKETPLACE;
                    case "9" -> LOG_OUT;
                    case "10" -> VIEW_COLLECTIONS;
                    case "11" -> VIEW_ITEMS;
                    case "12" -> VIEW_TRANSACTIONS;
                    case "0" -> EXIT;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == EXIT) {
                    process(EXIT);
                    System.exit(0);
                    break;
                }

                process(selectedItem);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    enum MainMenu {
        VIEW_COLLECTION("Перегляд колекцій"),
        ADD_COLLECTION("Створити колекцію"),
        EDIT_COLLECTION("Редагувати колекцію"),
        DELETE_COLLECTION("Видалити колекцію"),
        ADD_ITEM("Створити предмет"),
        EDIT_ITEM("Редагувати предмет"),
        DELETE_ITEM("Видалити предмет"),
        MARKETPLACE("Ринок"),
        VIEW_TRANSACTIONS("Перегляд транзакцій"),
        VIEW_COLLECTIONS("Перегляд всіх колекцій"),
        VIEW_ITEMS("Перегляд предметів"),
        LOG_OUT("Вийти з акаунту"),
        EXIT("Вихід");

        private final String name;

        MainMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

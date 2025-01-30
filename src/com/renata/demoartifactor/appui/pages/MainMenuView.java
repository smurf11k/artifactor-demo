package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
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

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.entity.impl.User.Role;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class MainMenuView implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final User.Role userRole;

    MainMenuView(Role userRole) {
        this.userRole = userRole;
    }

    private void process(MainMenu selectedItem) throws IOException {
        switch (selectedItem) {
            case VIEW_COLLECTION -> {
                System.out.println("Перегляд колекцій");
            }
            case ADD_COLLECTION -> {
                System.out.println("Створення колекції");
            }
            case EDIT_COLLECTION -> {
                System.out.println("Редагування колекцій");
            }
            case DELETE_COLLECTION -> {
                System.out.println("Видалення колекцій");
            }

            case ADD_ITEM -> {
                //AddCollectionForm collectionForm = new(collectionService);
                //collectionForm.render();
                System.out.println("Створення предмету");
            }
            case EDIT_ITEM -> {
                System.out.println("Редагування предмету");
            }
            case DELETE_ITEM -> {
                System.out.println("Видалення предмету");
            }

            case MARKETPLACE -> {
                System.out.println("Магазин");
            }

            case VIEW_COLLECTIONS -> {
                System.out.println("Перегляд всіх колекцій");
            }
            case VIEW_ITEMS -> {
                System.out.println("Перегляд всіх предметів");
            }
            case VIEW_TRANSACTIONS -> {
                System.out.println("Перегляд транзакцій");
            }

            case LOG_OUT -> {
                System.out.println("Вихід з акаунту...");
                // void logout() (AuthService)
                // -> AuthView
            }
            case EXIT -> {
                printRedMessage("Вихід з програми...");
            }
            default -> {
                System.out.println("Неправильний вибір");
            }
        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurpleMessage("=== Головне меню ===");
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
            printGreenMessage("Зробіть вибір: ");

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
        MARKETPLACE("Магазин"), //TODO change
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

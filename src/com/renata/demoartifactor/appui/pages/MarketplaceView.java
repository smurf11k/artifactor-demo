package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printGreen;
import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPromptBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellow;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.BUY_ITEM;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.EXIT;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.RETURN;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.SELL_ITEM;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.VIEW_MARKETPLACE;

import com.renata.demoartifactor.appui.PageFactory;
import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.contract.TransactionService;
import com.renata.demoartifactor.domain.dto.ItemAddDto;
import com.renata.demoartifactor.domain.dto.TransactionAddDto;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Item.ItemType;
import com.renata.demoartifactor.persistance.entity.impl.Transaction;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.entity.impl.User.Role;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class MarketplaceView implements Renderable {

    private final List<Item> marketplaceItems = new ArrayList<>();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final User.Role userRole;
    PageFactory pageFactory;
    ServiceFactory serviceFactory;

    public MarketplaceView(Role userRole, ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.pageFactory = PageFactory.getInstance(serviceFactory);
        this.userRole = userRole;

        for (int i = 0; i < 10; i++) {
            marketplaceItems.add(ItemGenerator.generateRandomItem(UUID.randomUUID()));
        }
    }

    private void process(MarketMenu selectedItem) {
        switch (selectedItem) {
            case VIEW_MARKETPLACE -> showItems();
            case SELL_ITEM -> sellItem();
            case BUY_ITEM -> buyItem();
            case RETURN -> pageFactory.createMainMenuView(userRole);
            case EXIT -> printRedMessage("Вихід з програми...");
            default -> printRedMessage("Неправильний вибір.");
        }
    }

    private void showItems() {
        printPurpleMessage("\n=== Доступні предмети ===");
        int counter = 1;
        for (Item item : marketplaceItems) {
            System.out.println(counter + ". " + item.getName() + " - " + item.getValue() + " $");
            counter++;
        }
        printPromptBlue("Виберіть номер предмету для перегляду (0 для повернення): ");
        try {
            String choice = reader.readLine();
            int index = Integer.parseInt(choice);
            if (index > 0 && index <= marketplaceItems.size()) {
                Item selectedItem = marketplaceItems.get(index - 1);

                printItemDetails(selectedItem);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Неправильний вибір.");
        }
    }

    private void sellItem() {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        ItemService itemService = serviceFactory.getItemService();
        AuthService authService = serviceFactory.getAuthService();

        User currentUser = authService.getUser();
        List<AntiqueCollection> collections = antiqueCollectionService.getAllByOwner(
            currentUser.getUsername());

        if (collections.isEmpty()) {
            printRedMessage("У вас немає колекцій.");
            return;
        }

        printPurpleMessage("\n=== Ваші колекції ===");
        for (int i = 0; i < collections.size(); i++) {
            AntiqueCollection collection = collections.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, collection.getName(),
                collection.getDescription());
        }

        Scanner scanner = new Scanner(System.in);
        printPromptBlue("Виберіть номер колекції для перегляду (0 для виходу): ");
        int collectionChoice = Integer.parseInt(scanner.nextLine());

        if (collectionChoice <= 0 || collectionChoice > collections.size()) {
            printYellowMessage("Повернення до меню...");
            return;
        }

        AntiqueCollection selectedCollection = collections.get(collectionChoice - 1);
        List<Item> items = itemService.getAllByCollection(selectedCollection);

        if (items.isEmpty()) {
            printRedMessage("У цій колекції немає предметів.");
            return;
        }

        printYellowMessage("\n=== Предмети у колекції ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s (Стартова ціна: %.2f$)%n", i + 1, items.get(i).getName(),
                items.get(i).getValue());
        }

        printPromptBlue("Виберіть номер предмета для продажу (0 для виходу): ");
        int itemChoice = Integer.parseInt(scanner.nextLine());

        if (itemChoice < 0 || itemChoice > items.size()) {
            printYellowMessage("Повернення до меню...");
            return;
        }

        Item itemToSell = items.get(itemChoice - 1);
        double finalPrice = startAuction(itemToSell);
        createTransaction(Transaction.TransactionType.SELL, itemToSell, finalPrice);

        itemService.delete(itemToSell.getId());
        JsonRepositoryFactory.getInstance().commit();
    }

    private double startAuction(Item item) {
        double startingPrice = item.getValue();
        double finalPrice = startingPrice;
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int auctionRounds = random.nextInt(5) + 3;
        printGreenMessage("Аукціон розпочато!");

        for (int i = 0; i < auctionRounds; i++) {
            printPromptBlue("Чекати наступної ставки чи продати? (1 - чекати, 2 - продати): ");
            String choice = scanner.nextLine();
            if ("2".equals(choice)) {
                break;
            }
            finalPrice += random.nextDouble() * 100;
            finalPrice = Math.round(finalPrice * 100.0) / 100.0;
            System.out.println("Поточна ціна: " + printGreen(finalPrice + " $"));
        }
        return finalPrice;
    }

    private void createTransaction(Transaction.TransactionType type, Item item, double price) {
        AuthService authService = serviceFactory.getAuthService();
        User currentUser = authService.getUser();
        TransactionService transactionService = serviceFactory.getTransactionService();

        TransactionAddDto transactionAddDto = new TransactionAddDto(
            UUID.randomUUID(),
            type,
            LocalDateTime.now(),
            item,
            price,
            currentUser
        );

        transactionService.add(transactionAddDto);
        JsonRepositoryFactory.getInstance().commit();
        System.out.println(printYellow(type.getName()) + " '" + item.getName()
            + "' за " + printGreen(price + " $"));
    }

    private void buyItem() {
        ItemService itemService = serviceFactory.getItemService();
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        AuthService authService = serviceFactory.getAuthService();

        User currentUser = authService.getUser();

        printPurpleMessage("\n=== Доступні предмети ===");
        int counter = 1;
        for (Item item : marketplaceItems) {
            System.out.println(counter + ". " + item.getName() + " - " + item.getValue() + " $");
            counter++;
        }
        printPromptBlue("Виберіть номер предмету для покупки (0 для повернення): ");

        try {
            String choice = reader.readLine();
            int index = Integer.parseInt(choice);
            if (index == 0) {
                printYellowMessage("Повернення до меню...");
                return;
            }

            if (index > 0 && index <= marketplaceItems.size()) {
                Item selectedItem = marketplaceItems.get(index - 1);
                printItemDetails(selectedItem);

                List<AntiqueCollection> collections = antiqueCollectionService.getAllByOwner(
                    currentUser.getUsername());
                if (collections.isEmpty()) {
                    printRedMessage("У вас немає колекцій для додавання предмета.");
                    return;
                }

                printPurpleMessage("\n=== Ваші колекції ===");
                for (int i = 0; i < collections.size(); i++) {
                    System.out.printf("%d. %s%n", i + 1, collections.get(i).getName());
                }
                printPromptBlue("Виберіть номер колекції для додавання предмета (0 для виходу): ");
                int collectionChoice = Integer.parseInt(reader.readLine());

                if (collectionChoice <= 0 || collectionChoice > collections.size()) {
                    printYellowMessage("Повернення до меню...");
                    return;
                }

                AntiqueCollection selectedCollection = collections.get(collectionChoice - 1);

                ItemAddDto itemAddDto = new ItemAddDto(
                    UUID.randomUUID(),
                    selectedItem.getName(),
                    selectedItem.getItemType(),
                    selectedCollection,
                    selectedItem.getValue(),
                    selectedItem.getCreatedDate(),
                    LocalDate.now(),
                    selectedItem.getDescription()
                );

                itemService.add(itemAddDto);
                JsonRepositoryFactory.getInstance().commit();

                createTransaction(Transaction.TransactionType.BUY, selectedItem,
                    selectedItem.getValue());

                printGreenMessage("Предмет успішно придбано!");
            } else {
                printRedMessage("Неправильний вибір.");
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Неправильний вибір.");
        }
    }

    private void printItemDetails(Item item) {
        printYellowMessage("\n=== Деталі предмету ===");
        System.out.println(printBlue("Назва: ") + item.getName());
        System.out.println(printBlue("Тип: ") + item.getItemType().getName());
        System.out.println(printBlue("Вартість: ") + item.getValue() + " $");
        System.out.println(printBlue("Рік створення: ") + item.getCreatedDate());
        System.out.println(printBlue("Дата отримання: ") + item.getDateAquired());
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurpleMessage("\n=== Ринок ===");
            System.out.println("1. " + VIEW_MARKETPLACE.getName());
            System.out.println("2. " + SELL_ITEM.getName());
            System.out.println("3. " + BUY_ITEM.getName());
            System.out.println("4. " + RETURN.getName());
            System.out.println("0. " + EXIT.getName());
            printBlueMessage("Зробіть вибір: ");

            String choice = reader.readLine();
            MarketMenu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> VIEW_MARKETPLACE;
                    case "2" -> SELL_ITEM;
                    case "3" -> BUY_ITEM;
                    case "4" -> RETURN;
                    case "0" -> EXIT;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == EXIT) {
                    process(EXIT);
                    System.exit(0);
                    break;
                }

                if (selectedItem == RETURN) {
                    process(RETURN);
                    break;
                }

                process(selectedItem);

            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    enum MarketMenu {
        VIEW_MARKETPLACE("Переглянути доступні предмети на ринку"),
        SELL_ITEM("Продати предмет"),
        BUY_ITEM("Купити предмет"),
        RETURN("Повернутись в головне меню"),
        EXIT("Вихід");

        private final String name;

        MarketMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class ItemGenerator {

        private static final Random RANDOM = new Random();

        private static final Map.Entry<String, Item.ItemType>[] ITEM_DATA = new Map.Entry[]{
            new AbstractMap.SimpleEntry<>("Античне крісло", Item.ItemType.FURNITURE),
            new AbstractMap.SimpleEntry<>("Золотий ланцюжок", Item.ItemType.JEWELRY),
            new AbstractMap.SimpleEntry<>("Вінтажний Cadillac", Item.ItemType.CLASSIC_CARS),
            new AbstractMap.SimpleEntry<>("Керамічна ваза прикрашена східними узорами",
                Item.ItemType.CERAMICS_AND_PORCELAIN),
            new AbstractMap.SimpleEntry<>("Медаль почесті",
                Item.ItemType.MILITARY_MEMORABILIA),
            new AbstractMap.SimpleEntry<>("Наручний годинник", ItemType.TIMEPIECES),
            new AbstractMap.SimpleEntry<>("Текстильний коврик", ItemType.TEXTILES),
            new AbstractMap.SimpleEntry<>("Колекційна монета 5 копійок", ItemType.COINS_AND_SILVER),
            new AbstractMap.SimpleEntry<>("Колекційна польска книга",
                ItemType.BOOKS_AND_MANUSCRIPTS),
            new AbstractMap.SimpleEntry<>("Картина з вершником",
                ItemType.ART),
            new AbstractMap.SimpleEntry<>("Антикварна керамічна ваза",
                Item.ItemType.CERAMICS_AND_PORCELAIN),
            new AbstractMap.SimpleEntry<>("Ретро автомобіль Ford Mustang",
                Item.ItemType.CLASSIC_CARS),
            new AbstractMap.SimpleEntry<>("Ювелірна каблучка з діамантом", Item.ItemType.JEWELRY),
            new AbstractMap.SimpleEntry<>("Фотографія сакури 20 століття", Item.ItemType.ART),
            new AbstractMap.SimpleEntry<>("Раритетний чайник з порцеляни",
                Item.ItemType.CERAMICS_AND_PORCELAIN),
            new AbstractMap.SimpleEntry<>("Книга про подорожі",
                Item.ItemType.BOOKS_AND_MANUSCRIPTS),
            new AbstractMap.SimpleEntry<>("Годинник з часами доби", Item.ItemType.TIMEPIECES),
            new AbstractMap.SimpleEntry<>("Військова форма західних країв",
                Item.ItemType.MILITARY_MEMORABILIA),
            new AbstractMap.SimpleEntry<>("Декоративна подушка зі старовинним орнаментом",
                Item.ItemType.TEXTILES),
            new AbstractMap.SimpleEntry<>("Колекційна монета 10 грн",
                Item.ItemType.COINS_AND_SILVER)
        };

        public static Item generateRandomItem(UUID id) {
            Map.Entry<String, Item.ItemType> itemData = ITEM_DATA[RANDOM.nextInt(ITEM_DATA.length)];
            String name = itemData.getKey();
            Item.ItemType itemType = itemData.getValue();
            User marketplaceUser = new User(UUID.randomUUID(), "Marketplace123",
                "marketplace@gmail.com", "marketplace", Role.ADMIN);
            AntiqueCollection collection = new AntiqueCollection(UUID.randomUUID(), "Ринок",
                "В цій колекції знаходяться предмети доступні на ринку", LocalDate.now(),
                marketplaceUser);
            double value = Math.round((RANDOM.nextDouble() * 1000 + 50) * 100.0) / 100.0;
            String createdDate = String.valueOf(RANDOM.nextInt(100) + 1300);
            LocalDate dateAquired = LocalDate.now();
            String description = "Цікавий екземпляр антикваріату.";

            return new Item(id, name, itemType, collection, value, createdDate, dateAquired,
                description);
        }
    }
}

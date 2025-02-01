package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printHeader;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.BUY_ITEM;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.EXIT;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.RETURN;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.SELL_ITEM;
import static com.renata.demoartifactor.appui.pages.MarketplaceView.MarketMenu.VIEW_MARKETPLACE;

import com.renata.demoartifactor.appui.PageFactory;
import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Item.ItemType;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.entity.impl.User.Role;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

    private void process(MarketMenu selectedItem) throws IOException {
        switch (selectedItem) {
            case VIEW_MARKETPLACE -> showItems();
            case SELL_ITEM -> sellItem();
            case BUY_ITEM -> buyItem();
            case RETURN -> pageFactory.createMainMenuView(userRole);
            case EXIT -> printRedMessage("Вихід з програми...");
            default -> System.err.println("Неправильний вибір");
        }
    }

    private void showItems() {
        printPurpleMessage("\n=== Доступні предмети ===");
        int counter = 1;
        for (Item item : marketplaceItems) {
            System.out.println(counter + ". " + item.getName() + " - " + item.getValue() + " $");
            counter++;
        }
        printHeader("Виберіть номер предмету для перегляду (0 для повернення): ");
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
        //show user collections
        //choose collection
        //choose item to sell
        //choose price (maybe add bidding so the price would change a couple of times)
        //for example: price start at the item price, then the user has option: wait or sell
        //the price increases for random amount (+random number)
        //user option again wait or sell, and repeat this for 3-5 times
        // and then after the last time just sell it automatically because 'no more bids'
        //process the selling + add transaction log
    }

    private void buyItem() {
        //show marketplace items: itemName - price
        //choose item to buy
        //process the payment + add transaction log
    }

    private void printItemDetails(Item item) {
        printYellowMessage("\n=== Деталі предмету ===");
        System.out.println("Назва: " + item.getName());
        System.out.println("Тип: " + item.getItemType().getName());
        System.out.println("Вартість: " + item.getValue() + " $");
        System.out.println("Рік створення: " + item.getCreatedDate());
        System.out.println("Дата отримання: " + item.getDateAquired());
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
            printHeader("Зробіть вибір: ");

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

    private class ItemGenerator {

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

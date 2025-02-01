package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.dto.ItemAddDto;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item.ItemType;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class AddItemForm implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public AddItemForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void processItemCreation() throws IOException {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        ItemService itemService = serviceFactory.getItemService();

        System.out.print("Впишіть назву предмета: ");
        String name = reader.readLine();

        System.out.println("Виберіть тип предмета:");
        ItemType[] itemTypes = ItemType.values();
        for (int i = 0; i < itemTypes.length; i++) {
            System.out.printf("%d. %s%n", i + 1, itemTypes[i].getName());
        }
        System.out.print("Виберіть номер типу предмета: ");
        int itemTypeChoice = Integer.parseInt(reader.readLine());
        ItemType itemType = itemTypes[itemTypeChoice - 1];

        System.out.print("Додайте опис предмета: ");
        String description = reader.readLine();

        System.out.print("Вкажіть вартість предмета ($): ");
        double value = Double.parseDouble(reader.readLine());

        System.out.print("Вкажіть рік створення предмета (yyyy): ");
        String createdDate = reader.readLine();

        System.out.print("Вкажіть дату отримання предмета (yyyy-mm-dd): ");
        LocalDate dateAquired = LocalDate.parse(reader.readLine());

        List<AntiqueCollection> collections = antiqueCollectionService.getAuthorizedUserCollections();
        if (collections.isEmpty()) {
            System.out.println(
                "У вас немає колекцій. Створіть колекцію перед додаванням предмета.");
            return;
        }

        System.out.println("Виберіть колекцію, до якої додається предмет:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, collections.get(i).getName());
        }
        System.out.print("Виберіть номер колекції: ");
        int collectionChoice = Integer.parseInt(reader.readLine());
        AntiqueCollection collection = collections.get(collectionChoice - 1);

        try {
            ItemAddDto itemAddDto = new ItemAddDto(
                UUID.randomUUID(),
                name,
                itemType,
                collection,
                value,
                createdDate,
                dateAquired,
                description
            );

            itemService.add(itemAddDto);
            JsonRepositoryFactory.getInstance().commit();

            printGreenMessage("Предмет успішно створено!");
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Створення нового предмета ===");
        try {
            processItemCreation();
        } catch (IOException e) {
            System.err.println("Помилка при введенні даних: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}

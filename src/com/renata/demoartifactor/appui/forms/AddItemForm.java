package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

public final class AddItemForm implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public AddItemForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private String readLineWithPrompt(String prompt) throws IOException {
        System.out.print(printBlue(prompt));
        return reader.readLine();
    }

    private int readChoiceWithValidation(String prompt, int maxOption) throws IOException {
        String input = readLineWithPrompt(prompt);
        int choice;
        try {
            choice = Integer.parseInt(input);
            if (choice < 1 || choice > maxOption) {
                throw new IllegalArgumentException("Неправильний вибір.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неправильний вибір.");
        }
        return choice;
    }

    private double readDoubleWithValidation(String prompt) throws IOException {
        String input = readLineWithPrompt(prompt);
        try {
            double value = Double.parseDouble(input);
            if (value < 0) {
                throw new IllegalArgumentException("Вартість предмета не може бути від'ємною.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Введено некоректне значення вартості.");
        }
    }

    private LocalDate readDateWithValidation(String prompt) throws IOException {
        String inputDate = readLineWithPrompt(prompt);
        try {
            LocalDate date = LocalDate.parse(inputDate);
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                    "Дата отримання предмета не може бути в майбутньому.");
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Невірний формат дати. Спробуйте ще раз у форматі yyyy-mm-dd.");
        }
    }

    private void processItemCreation() throws IOException {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        ItemService itemService = serviceFactory.getItemService();

        String name = readLineWithPrompt("Впишіть назву предмета: ");
        if (name.isBlank()) {
            throw new IllegalArgumentException(
                "Назва предмета не може бути порожньою. Спробуйте ще раз.");
        }

        printYellowMessage("Виберіть тип предмета:");
        ItemType[] itemTypes = ItemType.values();
        for (int i = 0; i < itemTypes.length; i++) {
            System.out.printf("%d. %s%n", i + 1, itemTypes[i].getName());
        }

        int itemTypeChoice = readChoiceWithValidation("Виберіть номер типу предмета: ",
            itemTypes.length);
        ItemType itemType = itemTypes[itemTypeChoice - 1];

        String description = readLineWithPrompt("Додайте опис предмета: ");
        double value = readDoubleWithValidation("Вкажіть вартість предмета ($): ");
        String createdDate = readLineWithPrompt("Вкажіть дату створення предмета (yyyy): ");
        LocalDate dateAquired = readDateWithValidation(
            "Вкажіть дату отримання предмета (yyyy-mm-dd): ");

        List<AntiqueCollection> collections = antiqueCollectionService.getAuthorizedUserCollections();
        if (collections.isEmpty()) {
            printRedMessage("У вас немає колекцій. Створіть колекцію перед додаванням предмета.");
            return;
        }

        printBlueMessage("Виберіть колекцію, до якої додається предмет:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, collections.get(i).getName());
        }

        int collectionChoice = readChoiceWithValidation("Виберіть номер колекції: ",
            collections.size());
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
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}

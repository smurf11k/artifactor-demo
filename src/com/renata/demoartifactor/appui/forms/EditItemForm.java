package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPromptBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.dto.ItemUpdateDto;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

public final class EditItemForm implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public EditItemForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void processItemEditing() throws IOException {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        ItemService itemService = serviceFactory.getItemService();

        List<AntiqueCollection> collections = antiqueCollectionService.getAuthorizedUserCollections();

        if (collections.isEmpty()) {
            System.err.println("У вас немає колекцій для редагування.");
            return;
        }

        printBlueMessage("Доступні колекції для редагування:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, collections.get(i).getName());
        }
        printPromptBlue("Виберіть номер колекції: ");
        int collectionChoice = Integer.parseInt(reader.readLine());
        AntiqueCollection collection = collections.get(collectionChoice - 1);

        List<Item> userItems = itemService.getAllByCollection(collection);

        if (userItems.isEmpty()) {
            System.err.println("У вас немає предметів в цій колекції.");
            return;
        }

        printBlueMessage("Доступні предмети для редагування:");
        for (int i = 0; i < userItems.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, userItems.get(i).getName());
        }
        printPromptBlue("Виберіть номер предмета: ");
        int itemChoice = Integer.parseInt(reader.readLine());
        Item item = userItems.get(itemChoice - 1);

        System.out.println(printBlue("Редагування предмета: ") + item.getName());

        printPromptBlue("Нова назва предмета (залиште порожнім для відміни): ");
        String newName = reader.readLine();
        if (!newName.isEmpty()) {
            item.setName(newName);
        }

        printPromptBlue("Новий опис предмета (залиште порожнім для відміни): ");
        String newDescription = reader.readLine();
        if (!newDescription.isEmpty()) {
            item.setDescription(newDescription);
        }

        printPromptBlue("Нова вартість предмета (залиште порожнім для відміни): ");
        String newValue = reader.readLine();
        if (!newValue.isEmpty()) {
            item.setValue(Double.parseDouble(newValue));
        }

        printPromptBlue("Нова дата створення предмета (yyyy) (залиште порожнім для відміни): ");
        String newCreatedDate = reader.readLine();
        if (!newCreatedDate.isEmpty()) {
            item.setCreatedDate(newCreatedDate);
        }

        printPromptBlue(
            "Нова дата отримання предмета (yyyy-mm-dd) (залиште порожнім для відміни): ");
        String newDateAquired = reader.readLine();
        if (!newDateAquired.isEmpty()) {
            item.setDateAquired(LocalDate.parse(newDateAquired));
        }

        printPromptBlue("Ви хочете змінити колекцію предмета? (+/-): ");
        String changeCollectionChoice = reader.readLine();
        if (changeCollectionChoice.equalsIgnoreCase("+")) {
            printBlueMessage("Виберіть нову колекцію для предмета:");
            for (int i = 0; i < collections.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, collections.get(i).getName());
            }
            printPromptBlue("Виберіть номер нової колекції: ");
            int collectionChoiceNew = Integer.parseInt(reader.readLine());
            item.setCollection(collections.get(collectionChoiceNew - 1));
        }

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto(
            item.getId(),
            item.getName(),
            item.getCollection(),
            item.getValue(),
            item.getCreatedDate(),
            item.getDateAquired(),
            item.getDescription()
        );

        itemService.update(itemUpdateDto);
        JsonRepositoryFactory.getInstance().commit();

        printGreenMessage("Предмет успішно оновлено!");
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Редагування предмета ===");
        try {
            processItemEditing();
        } catch (IOException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}

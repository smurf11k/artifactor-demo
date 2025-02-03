package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printBlueMessage;
import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.PrintUI.printYellowMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionUpdateDto;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public final class EditCollectionForm implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public EditCollectionForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void displayUserCollections() {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        AuthService authService = serviceFactory.getAuthService();

        User currentUser = authService.getUser();
        List<AntiqueCollection> collections = antiqueCollectionService.getAllByOwner(
            currentUser.getUsername());

        if (collections.isEmpty()) {
            printRedMessage("У вас ще немає колекцій.");
        } else {
            printBlueMessage("Доступні колекції для редагування:");
            for (int i = 0; i < collections.size(); i++) {
                AntiqueCollection collection = collections.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, collection.getName(),
                    collection.getDescription());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print(
                printBlue("Виберіть номер колекції для редагування (0 для повернення): "));
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= collections.size()) {
                AntiqueCollection selectedCollection = collections.get(choice - 1);
                processCollectionEditing(selectedCollection);
            } else if (choice == 0) {
                printYellowMessage("Повернення до головного меню...");
            } else {
                printRedMessage("Невірний номер, спробуйте ще раз.");
            }
        }
    }

    private void processCollectionEditing(AntiqueCollection collection) {
        try {
            System.out.println(printBlue("Редагування колекції: ") + collection.getName());

            System.out.print(
                printBlue("Введіть нову назву колекції (залиште порожнім для відміни): "));
            String newName = reader.readLine();
            if (!newName.isEmpty()) {
                collection.setName(newName);
            }

            System.out.print(
                printBlue("Введіть новий опис колекції (залиште порожнім для відміни): "));
            String newDescription = reader.readLine();
            if (!newDescription.isEmpty()) {
                collection.setDescription(newDescription);
            }

            AntiqueCollectionUpdateDto updateDto = new AntiqueCollectionUpdateDto(
                collection.getId(),
                collection.getName(),
                collection.getDescription()
            );

            AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
            antiqueCollectionService.update(updateDto);
            JsonRepositoryFactory.getInstance().commit();

            printGreenMessage("Колекцію успішно оновлено!");
        } catch (IOException e) {
            System.err.println("Помилка при введенні даних: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Редагування колекцій ===");
        displayUserCollections();
    }
}

package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPromptBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionAddDto;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.UUID;

public final class AddCollectionForm implements Renderable {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private final ServiceFactory serviceFactory;

    public AddCollectionForm(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void processCollectionCreation() throws IOException {
        AntiqueCollectionService antiqueCollectionService = serviceFactory.getCollectionService();
        AuthService authService = serviceFactory.getAuthService();
        User currentUser = authService.getUser();

        printPromptBlue("Впишіть назву колекції: ");
        String name = reader.readLine();

        printPromptBlue("Впишіть опис колекції: ");
        String description = reader.readLine();

        try {
            AntiqueCollectionAddDto collectionAddDto = new AntiqueCollectionAddDto(
                UUID.randomUUID(),
                name,
                description,
                LocalDate.now(),
                currentUser
            );

            antiqueCollectionService.add(collectionAddDto);
            JsonRepositoryFactory.getInstance().commit();

            printGreenMessage("Колекція успішно створена та збережена у JSON!");
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    @Override
    public void render() throws IOException {
        printPurpleMessage("\n=== Створення нової колекції ===");
        try {
            processCollectionCreation();
        } catch (IOException e) {
            System.err.println("Помилка при введенні даних: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}

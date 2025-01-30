package com.renata.demoartifactor.appui.forms;

import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionAddDto;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.UUID;

public final class AddCollectionForm implements Renderable {

    private final AntiqueCollectionService antiqueCollectionService;
    private final UserRepository userRepository;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public AddCollectionForm(AntiqueCollectionService antiqueCollectionService,
        UserRepository userRepository) {
        this.antiqueCollectionService = antiqueCollectionService;
        this.userRepository = userRepository;
    }

    private void processCollectionCreation() throws IOException {
        System.out.print("Впишіть назву колекції: ");
        String name = reader.readLine();

        System.out.print("Впишіть опис колекції: ");
        String description = reader.readLine();

        System.out.print("Впишіть ім'я власника колекції: ");
        String ownerUsername = reader.readLine();

        User owner = userRepository.findByUsername(ownerUsername)
            .orElseThrow(
                () -> new IllegalArgumentException("Користувач з таким ім'ям не знайдений."));

        try {
            AntiqueCollectionAddDto collectionAddDto = new AntiqueCollectionAddDto(
                UUID.randomUUID(),
                name,
                null,
                description,
                LocalDate.now(),
                owner
            );

            antiqueCollectionService.add(collectionAddDto);
            JsonRepositoryFactory.getInstance().commit(); // maybe move to a different class

            System.out.println("Колекція успішно створена та збережена у JSON!");
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }

    //TODO use ItemsView to show the items listed in the collection
    // but also be able to use ItemsView for the marketplace
    // or maybe add something lese for the marketplace

    //TODO show all the available collections of the authorized user
    //public void showCollections() // maybe move to a different class


    @Override
    public void render() throws IOException {
        printPurpleMessage("=== Створення нової колекції ===");
        try {
            processCollectionCreation();
        } catch (IOException e) {
            System.err.println("Помилка при введенні даних: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}

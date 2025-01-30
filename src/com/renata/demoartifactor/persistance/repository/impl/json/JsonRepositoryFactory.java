package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.exception.JsonFileIOException;
import com.renata.demoartifactor.persistance.repository.RepositoryFactory;
import com.renata.demoartifactor.persistance.repository.contracts.AntiqueCollectionRepository;
import com.renata.demoartifactor.persistance.repository.contracts.ItemRepository;
import com.renata.demoartifactor.persistance.repository.contracts.TransactionRepository;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class JsonRepositoryFactory extends RepositoryFactory {

    private final Gson gson;
    private final UserJsonRepositoryImpl userJsonRepositoryImpl;
    private final AntiqueCollectionJsonRepositoryImpl collectionJsonRepositoryImpl;
    private final ItemJsonRepositoryImpl itemJsonRepositoryImpl;
    private final TransactionJsonRepositoryImpl transactionJsonRepositoryImpl;

    private JsonRepositoryFactory() {
        // Адаптер для типу даних LocalDateTime при серіалізації/десеріалізації
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,
            (JsonSerializer<LocalDateTime>) (localDate, srcType, context) ->
                new JsonPrimitive(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(localDate)));
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,
            (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                LocalDateTime.parse(json.getAsString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                        .withLocale(Locale.of("uk", "UA"))));

        // Адаптер для типу даних LocalDate при серіалізації/десеріалізації
        gsonBuilder.registerTypeAdapter(LocalDate.class,
            (JsonSerializer<LocalDate>) (localDate, srcType, context) ->
                new JsonPrimitive(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy").format(localDate)));
        gsonBuilder.registerTypeAdapter(LocalDate.class,
            (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                LocalDate.parse(json.getAsString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        .withLocale(Locale.of("uk", "UA"))));

        gson = gsonBuilder.setPrettyPrinting().create();

        userJsonRepositoryImpl = new UserJsonRepositoryImpl(gson);
        collectionJsonRepositoryImpl = new AntiqueCollectionJsonRepositoryImpl(gson);
        itemJsonRepositoryImpl = new ItemJsonRepositoryImpl(gson);
        transactionJsonRepositoryImpl = new TransactionJsonRepositoryImpl(gson);
    }

    public static JsonRepositoryFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public UserRepository getUserRepository() {
        return userJsonRepositoryImpl;
    }

    @Override
    public AntiqueCollectionRepository getCollectionRepository() {
        return collectionJsonRepositoryImpl;
    }

    @Override
    public ItemRepository getItemRepository() {
        return itemJsonRepositoryImpl;
    }

    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionJsonRepositoryImpl;
    }

    public void commit() {
        serializeEntities(userJsonRepositoryImpl.getPath(), userJsonRepositoryImpl.findAll());
        serializeEntities(collectionJsonRepositoryImpl.getPath(),
            collectionJsonRepositoryImpl.findAll());
        serializeEntities(itemJsonRepositoryImpl.getPath(), itemJsonRepositoryImpl.findAll());
        serializeEntities(transactionJsonRepositoryImpl.getPath(),
            transactionJsonRepositoryImpl.findAll());
    }

    private <E extends Entity> void serializeEntities(Path path, Set<E> entities) {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            // Скидуємо файлик, перед збереженням!
            writer.write("");
            // Перетворюємо колекцію користувачів в JSON та записуємо у файл
            gson.toJson(entities, writer);

        } catch (IOException e) {
            throw new JsonFileIOException("Не вдалось зберегти дані у json-файл. Детальніше: %s"
                .formatted(e.getMessage()));
        }
    }

    private static class InstanceHolder {

        public static final JsonRepositoryFactory INSTANCE = new JsonRepositoryFactory();
    }
}

package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.exception.JsonFileIOException;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GenericJsonRepository<E extends Entity> implements Repository<E> {

    protected final Set<E> entities;
    private final Gson gson;
    private final Path path;
    private final Type collectionType;

    GenericJsonRepository(Gson gson, Path path, Type collectionType) {
        this.gson = gson;
        this.path = path;
        this.collectionType = collectionType;
        entities = loadAll();
    }

    @Override
    public Optional<E> findById(UUID id) {
        return entities.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public Set<E> findAll() {
        return entities;
    }

    @Override
    public Set<E> findAll(Predicate<E> filter) {
        return entities.stream().filter(filter).collect(Collectors.toSet());
    }

    @Override
    public E add(E entity) {
        entities.remove(entity);
        entities.add(entity);
        return entity;
    }

    @Override
    public boolean remove(E entity) {
        return entities.remove(entity);
    }

    public Path getPath() {
        return path;
    }

    private Set<E> loadAll() {
        try {
            fileNotFound();
            var json = Files.readString(path);
            return isValidJson(json) ? gson.fromJson(json, collectionType) : new HashSet<>();
        } catch (IOException e) {
            throw new JsonFileIOException("Помилка при роботі із файлом %s."
                .formatted(path.getFileName()));
        }
    }

    public void update(E entity) {
        Optional<E> existingEntity = findById(entity.getId());

        if (existingEntity.isPresent()) {
            entities.remove(existingEntity.get());
            entities.add(entity);
            saveAll();
        } else {
            throw new IllegalArgumentException("Сутність з таким ID не знайдена.");
        }
    }

    private void saveAll() {
        try {
            Files.writeString(path, gson.toJson(entities));
        } catch (IOException e) {
            throw new JsonFileIOException(
                "Помилка при збереженні файлу %s.".formatted(path.getFileName()));
        }
    }

    /**
     * Перевірка на валідність формату даних JSON.
     *
     * @param input JSON у форматі рядка.
     * @return результат перевірки.
     */
    private boolean isValidJson(String input) {
        try (JsonReader reader = new JsonReader(new StringReader(input))) {
            reader.skipValue();
            return reader.peek() == JsonToken.END_DOCUMENT;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Якщо файлу не існує, то ми його створюємо.
     *
     * @throws IOException виключення при роботі із потоком вводу виводу.
     */
    private void fileNotFound() throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
}

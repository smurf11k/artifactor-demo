package com.renata.demoartifactor.persistance.repository.impl.json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public enum JsonPathFactory {
    USERS("users.json"),
    ITEMS("items.json"),
    COLLECTIONS("collections.json"),
    TRANSACTIONS("transactions.json");

    private static final String DATA_DIRECTORY = "data";
    private final String fileName;
    private final Path filePath;

    JsonPathFactory(String fileName) {
        this.fileName = fileName;
        this.filePath = Path.of(DATA_DIRECTORY, this.fileName);
        ensureFileExists();
    }

    public Path getPath() {
        return Path.of(DATA_DIRECTORY, this.fileName);
    }

    private void ensureFileExists() {
        File file = filePath.toFile();
        if (!file.exists()) {
            try {
                Files.createDirectories(filePath.getParent()); // Створюємо папку, якщо її немає
                Files.write(filePath, "[]".getBytes()); // Створюємо файл з порожнім JSON-масивом
            } catch (IOException e) {
                throw new RuntimeException("Не вдалося створити JSON-файл: " + filePath, e);
            }
        }
    }
}

package com.renata.demoartifactor.persistance.repository.impl.json;

import com.renata.demoartifactor.persistance.exception.JsonFileIOException;
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
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            throw new JsonFileIOException("Не вдалося створити директорію: " + DATA_DIRECTORY);
        }

        File file = filePath.toFile();
        if (!file.exists()) {
            try {
                Files.createDirectories(
                    filePath.getParent());
                Files.write(filePath, "[]".getBytes());
            } catch (IOException e) {
                throw new JsonFileIOException(
                    "Не вдалося створити JSON-файл: " + filePath + "\n" + e);
            }
        }
    }
}

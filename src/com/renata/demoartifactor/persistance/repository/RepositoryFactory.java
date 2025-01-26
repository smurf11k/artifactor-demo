package com.renata.demoartifactor.persistance.repository;

import com.renata.demoartifactor.persistance.repository.contracts.TagRepository;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import org.apache.commons.lang3.NotImplementedException;

public abstract class RepositoryFactory {

    public static final int JSON = 1;
    public static final int XML = 2;
    public static final int POSTGRESQL = 3;

    public static RepositoryFactory getRepositoryFactory(int whichFactory) {
        return switch (whichFactory) {
            case JSON -> JsonRepositoryFactory.getInstance();
            case XML -> throw new NotImplementedException("Робота з XML файлами не реалізована.");
            case POSTGRESQL ->
                throw new NotImplementedException("Робота з СУБД PostgreSQL не реалізована.");
            default ->
                throw new IllegalArgumentException("Помилка при виборі фабрики репозиторіїв.");
        };
    }

    public abstract TagRepository getTagRepository();

    public abstract UserRepository getUserRepository();

    public abstract void commit();
}

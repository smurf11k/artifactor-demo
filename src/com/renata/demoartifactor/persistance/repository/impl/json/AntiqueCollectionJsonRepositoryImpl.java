package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.AntiqueCollectionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

final class AntiqueCollectionJsonRepositoryImpl
    extends GenericJsonRepository<AntiqueCollection>
    implements AntiqueCollectionRepository {

    AntiqueCollectionJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.COLLECTIONS.getPath(), TypeToken
            .getParameterized(Set.class, AntiqueCollection.class)
            .getType());
    }

    @Override
    public List<AntiqueCollection> findAllByOwner(User owner) {
        return entities.stream()
            .filter(l -> l.getOwner().equals(owner))
            .collect(Collectors.toList());
    }

    @Override
    public AntiqueCollection add(AntiqueCollection collection) {
        super.add(collection);

        JsonRepositoryFactory.getInstance().commit();
        return collection;
    }

    @Override
    public void save(AntiqueCollection collection) {
        Optional<AntiqueCollection> existingCollection = entities.stream()
            .filter(c -> c.getId().equals(collection.getId()))
            .findFirst();

        if (existingCollection.isPresent()) {
            entities.remove(existingCollection.get());
        }

        entities.add(collection);
        JsonRepositoryFactory.getInstance().commit();
    }

    @Override
    public void delete(AntiqueCollection collection) {
        entities.remove(collection);
        JsonRepositoryFactory.getInstance().commit();
    }
}

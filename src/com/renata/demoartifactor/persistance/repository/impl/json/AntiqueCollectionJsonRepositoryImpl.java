package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.domain.exception.EntityNotFoundException;
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
    public void update(AntiqueCollection collection) {
        Optional<AntiqueCollection> existingCollection = entities.stream()
            .filter(c -> c.getId().equals(collection.getId()))
            .findFirst();

        if (existingCollection.isPresent()) {
            super.update(collection);
        } else {
            throw new EntityNotFoundException("Колекція не існує.");
        }
        JsonRepositoryFactory.getInstance().commit();
    }


    @Override
    public boolean remove(AntiqueCollection collection) {
        boolean removed = entities.remove(collection);
        if (removed) {
            JsonRepositoryFactory.getInstance().commit();
        }
        return removed;
    }
}
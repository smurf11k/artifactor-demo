package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.AntiqueCollectionRepository;
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
    public Set<AntiqueCollection> findAllByOwner(User owner) {
        return entities.stream().filter(l -> l.getOwner().equals(owner))
            .collect(Collectors.toSet());
    }

    @Override
    public AntiqueCollection add(AntiqueCollection collection) {
        super.add(collection);

        JsonRepositoryFactory.getInstance().commit();

        return collection;
    }

}

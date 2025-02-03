package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.contracts.ItemRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class ItemJsonRepositoryImpl extends GenericJsonRepository<Item> implements ItemRepository {

    ItemJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.ITEMS.getPath(), TypeToken
            .getParameterized(Set.class, Item.class)
            .getType());
    }

    @Override
    public List<Item> findByCollection(AntiqueCollection collection) {
        return entities.stream()
            .filter(item -> item.getCollection() != null && item.getCollection().equals(collection))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Item item) {
        super.update(item);
    }

    @Override
    public boolean remove(Item item) {
        boolean removed = entities.removeIf(i -> i.getId().equals(item.getId()));
        if (removed) {
            JsonRepositoryFactory.getInstance().commit();
        }
        return removed;
    }
}

package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.contracts.ItemRepository;
import java.util.Set;

final class ItemJsonRepositoryImpl extends GenericJsonRepository<Item> implements ItemRepository {

    ItemJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.ITEMS.getPath(), TypeToken
            .getParameterized(Set.class, Item.class)
            .getType());
    }

    /*
    @Override
    public Set<Item> findByItemType(ItemType itemType) {
        return entities.stream()
            .filter(item -> itemType.equals(item.getItemType()))
            .collect(Collectors.toSet());
    }*/

}

package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Tag;
import com.renata.demoartifactor.persistance.repository.contracts.ItemRepository;
import java.util.Set;
import java.util.stream.Collectors;

final class ItemJsonRepositoryImpl extends GenericJsonRepository<Item> implements ItemRepository {

    ItemJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.ITEMS.getPath(), TypeToken
            .getParameterized(Set.class, Tag.class)
            .getType());
    }

    @Override
    public Set<Item> findAllByCategory(Category category) {
        return entities.stream()
            .filter(item -> category.equals(item.getCategory()))
            .collect(Collectors.toSet());
    }

}

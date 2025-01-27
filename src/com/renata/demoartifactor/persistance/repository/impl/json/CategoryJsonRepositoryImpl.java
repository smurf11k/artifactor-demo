package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Tag;
import com.renata.demoartifactor.persistance.repository.contracts.CategoryRepository;
import java.util.Set;
import java.util.stream.Collectors;

final class CategoryJsonRepositoryImpl
    extends GenericJsonRepository<Category>
    implements CategoryRepository {

    CategoryJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.CATEGORIES.getPath(), TypeToken
            .getParameterized(Set.class, Tag.class)
            .getType());
    }

    //change if needed
    @Override
    public Set<Category> findAllByItem(Item item) {
        return entities.stream()
            .filter(t -> item.getTags().contains(t))
            .collect(Collectors.toSet());
    }
}

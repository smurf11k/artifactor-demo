package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Tag;
import com.renata.demoartifactor.persistance.repository.contracts.TagRepository;
import java.util.Set;
import java.util.stream.Collectors;

final class TagJsonRepositoryImpl extends GenericJsonRepository<Tag> implements TagRepository {

    TagJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.TAGS.getPath(), TypeToken
            .getParameterized(Set.class, Tag.class)
            .getType());
    }

    @Override
    public Set<Tag> findAllByItem(Item item) {
        return entities.stream()
            .filter(t -> item.getTags().contains(t))
            .collect(Collectors.toSet());
    }
}

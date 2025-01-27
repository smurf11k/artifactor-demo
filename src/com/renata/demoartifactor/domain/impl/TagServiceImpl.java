package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.TagService;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Tag;
import com.renata.demoartifactor.persistance.repository.contracts.TagRepository;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public class TagServiceImpl extends GenericService<Tag> implements TagService {

    private final TagRepository tagRepository;

    TagServiceImpl(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }

    @Override
    public Set<Tag> getAllByItem(Item item) {
        return new TreeSet<>(tagRepository.findAllByItem(item));
    }

    @Override
    public Set<Tag> getAll() {
        return getAll(p -> true);
    }

    @Override
    public Set<Tag> getAll(Predicate<Tag> filter) {
        return new TreeSet<>(tagRepository.findAll(filter));
    }
}

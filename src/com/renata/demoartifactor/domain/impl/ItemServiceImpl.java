package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.contracts.ItemRepository;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public class ItemServiceImpl extends GenericService<Item> implements ItemService {

    private final ItemRepository itemRepository;

    ItemServiceImpl(ItemRepository itemRepository) {
        super(itemRepository);
        this.itemRepository = itemRepository;
    }

    @Override
    public Set<Item> getAllByCategory(Category category) {
        return new TreeSet<>(itemRepository.findAllByCategory(category));
    }

    @Override
    public Set<Item> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Item> getAll(Predicate<Item> filter) {
        return new TreeSet<>(itemRepository.findAll(filter));
    }
}

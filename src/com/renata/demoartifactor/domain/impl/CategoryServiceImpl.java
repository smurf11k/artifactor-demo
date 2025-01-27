package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.CategoryService;
import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.contracts.CategoryRepository;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public class CategoryServiceImpl extends GenericService<Category> implements CategoryService {

    private final CategoryRepository categoryRepository;

    CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<Category> getAllByItem(Item item) {
        return new TreeSet<>(categoryRepository.findAllByItem(item));
    }

    @Override
    public Set<Category> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Category> getAll(Predicate<Category> filter) {
        return new TreeSet<>(categoryRepository.findAll(filter));
    }
}

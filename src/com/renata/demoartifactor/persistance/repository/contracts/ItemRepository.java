package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.util.Set;

public interface ItemRepository extends Repository<Item> {

    Set<Item> findAllByCategory(Category category);
}

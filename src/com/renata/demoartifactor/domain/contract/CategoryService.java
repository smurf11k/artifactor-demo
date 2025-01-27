package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import java.util.Set;

public interface CategoryService extends Service<Category> {

    Set<Category> getAllByItem(Item item); //check
}

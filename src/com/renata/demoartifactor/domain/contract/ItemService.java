package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.persistance.entity.impl.Category;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import java.util.Set;

public interface ItemService extends Service<Item> {

    Set<Item> getAllByCategory(Category category); //check
}

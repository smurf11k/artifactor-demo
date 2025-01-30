package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.persistance.entity.impl.Item;

public interface ItemService extends Service<Item> {

    //Set<Item> getAllByType(Type category); //check
}

package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.Repository;

public interface ItemRepository extends Repository<Item> {

    //Set<Item> findByItemType(ItemType itemType);
}

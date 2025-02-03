package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.util.List;

public interface ItemRepository extends Repository<Item> {

    List<Item> findByCollection(AntiqueCollection collection);

    void update(Item item);
}

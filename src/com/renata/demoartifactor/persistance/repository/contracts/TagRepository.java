package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Tag;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.util.Set;

public interface TagRepository extends Repository<Tag> {

    Set<Tag> findAllByItem(Item item);
}

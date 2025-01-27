package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.Tag;
import java.util.Set;

public interface TagService extends Service<Tag> {

    Set<Tag> getAllByItem(Item item);
}

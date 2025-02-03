package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.util.List;

public interface AntiqueCollectionRepository extends Repository<AntiqueCollection> {

    List<AntiqueCollection> findAllByOwner(User owner);

    void update(AntiqueCollection collection);
}

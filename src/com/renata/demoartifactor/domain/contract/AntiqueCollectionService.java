package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionAddDto;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import java.util.Set;

public interface AntiqueCollectionService extends Service<AntiqueCollection> {

    Set<AntiqueCollection> getAllByOwner(String owner);

    AntiqueCollection add(AntiqueCollectionAddDto collectionAddDto);
}

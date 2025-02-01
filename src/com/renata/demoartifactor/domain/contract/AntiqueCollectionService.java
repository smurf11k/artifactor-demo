package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionAddDto;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionUpdateDto;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import java.util.List;
import java.util.UUID;

public interface AntiqueCollectionService extends Service<AntiqueCollection> {

    List<AntiqueCollection> getAllByOwner(String ownerName);

    List<AntiqueCollection> getAuthorizedUserCollections();

    AntiqueCollection add(AntiqueCollectionAddDto collectionAddDto);

    void update(AntiqueCollectionUpdateDto collectionUpdateDto);

    void delete(UUID id);
}

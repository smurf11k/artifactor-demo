package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionAddDto;
import com.renata.demoartifactor.domain.exception.EntityNotFoundException;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.AntiqueCollectionRepository;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

final class AntiqueCollectionServiceImpl extends GenericService<AntiqueCollection> implements
    AntiqueCollectionService {

    private final AntiqueCollectionRepository collectionRepository;
    private final UserRepository userRepository;

    AntiqueCollectionServiceImpl(AntiqueCollectionRepository collectionRepository,
        UserRepository userRepository) {
        super(collectionRepository);
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Set<AntiqueCollection> getAllByOwner(String username) {
        User owner = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Такого власника колекції не існує"));
        return new TreeSet<>(collectionRepository.findAllByOwner(owner));
    }

    @Override
    public Set<AntiqueCollection> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<AntiqueCollection> getAll(Predicate<AntiqueCollection> filter) {
        return new TreeSet<>(collectionRepository.findAll(filter));
    }

    // fix if needed
    @Override
    public AntiqueCollection add(AntiqueCollectionAddDto collectionAddDto) {
        AntiqueCollection collection = new AntiqueCollection(
            collectionAddDto.getId(),
            collectionAddDto.name(),
            collectionAddDto.items(),
            collectionAddDto.description(),
            collectionAddDto.createdDate(),
            collectionAddDto.owner() //current user who creates the collection
        );

        collectionRepository.add(
            collection);

        return collection;
    }

}

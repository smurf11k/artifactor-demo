package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionAddDto;
import com.renata.demoartifactor.domain.dto.AntiqueCollectionUpdateDto;
import com.renata.demoartifactor.domain.exception.EntityNotFoundException;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.AntiqueCollectionRepository;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;

final class AntiqueCollectionServiceImpl extends GenericService<AntiqueCollection> implements
    AntiqueCollectionService {

    private final AntiqueCollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    AntiqueCollectionServiceImpl(AntiqueCollectionRepository collectionRepository,
        UserRepository userRepository, AuthService authService) {
        super(collectionRepository);
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public List<AntiqueCollection> getAllByOwner(String username) {

        User owner = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Такого власника колекції не існує"));

        return collectionRepository.findAllByOwner(owner);
    }

    @Override
    public List<AntiqueCollection> getAuthorizedUserCollections() {
        User currentUser = authService.getUser();
        return getAllByOwner(currentUser.getUsername());
    }

    @Override
    public Set<AntiqueCollection> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<AntiqueCollection> getAll(Predicate<AntiqueCollection> filter) {
        return new TreeSet<>(collectionRepository.findAll(filter));
    }

    @Override
    public AntiqueCollection add(AntiqueCollectionAddDto collectionAddDto) {
        AntiqueCollection collection = new AntiqueCollection(
            collectionAddDto.getId(),
            collectionAddDto.name(),
            collectionAddDto.description(),
            collectionAddDto.createdDate(),
            collectionAddDto.owner()
        );

        collectionRepository.add(
            collection);

        return collection;
    }

    @Override
    public void update(AntiqueCollectionUpdateDto collectionUpdateDto) {

        User currentUser = authService.getUser();

        List<AntiqueCollection> userCollections = getAllByOwner(currentUser.getUsername());

        AntiqueCollection existingCollection = findCollectionById(userCollections,
            collectionUpdateDto.id());

        existingCollection.setName(collectionUpdateDto.name());
        existingCollection.setDescription(collectionUpdateDto.description());

        collectionRepository.save(existingCollection);
    }

    public AntiqueCollection findCollectionById(List<AntiqueCollection> collections, UUID id) {
        for (AntiqueCollection collection : collections) {
            if (collection.getId().equals(id)) {
                return collection;
            }
        }
        throw new EntityNotFoundException("Колекцію з таким ID не знайдено!");
    }

    @Override
    public void delete(UUID id) {
        User currentUser = authService.getUser();
        List<AntiqueCollection> userCollections = getAllByOwner(currentUser.getUsername());

        AntiqueCollection collectionToDelete = findCollectionById(userCollections, id);

        collectionRepository.delete(collectionToDelete);
    }


}

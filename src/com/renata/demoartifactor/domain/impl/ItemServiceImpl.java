package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.dto.ItemAddDto;
import com.renata.demoartifactor.domain.dto.ItemUpdateDto;
import com.renata.demoartifactor.domain.exception.EntityNotFoundException;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.Item;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.exception.EntityArgumentException;
import com.renata.demoartifactor.persistance.repository.contracts.ItemRepository;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;

final class ItemServiceImpl extends GenericService<Item> implements ItemService {

    private final ItemRepository itemRepository;
    private final AntiqueCollectionService antiqueCollectionService;
    private final AuthService authService;

    ItemServiceImpl(ItemRepository itemRepository,
        AntiqueCollectionService antiqueCollectionService,
        AuthService authService) {
        super(itemRepository);
        this.itemRepository = itemRepository;
        this.antiqueCollectionService = antiqueCollectionService;
        this.authService = authService;
    }

    @Override
    public List<Item> getAllByCollection(AntiqueCollection collection) {
        return itemRepository.findByCollection(collection);
    }

    private AntiqueCollection getUserCollection(User user) {
        List<AntiqueCollection> userCollections = antiqueCollectionService.getAllByOwner(
            user.getUsername());

        if (userCollections.isEmpty()) {
            throw new EntityNotFoundException("У користувача немає колекцій");
        }

        return userCollections.getFirst();
    }

    @Override
    public Set<Item> getAll() {
        return getAll(c -> true);
    }

    @Override
    public Set<Item> getAll(Predicate<Item> filter) {
        return new TreeSet<>(itemRepository.findAll(filter));
    }

    @Override
    public Item add(ItemAddDto itemAddDto) {
        try {
            Item item = new Item(
                itemAddDto.getId(),
                itemAddDto.name(),
                itemAddDto.itemType(),
                itemAddDto.antiqueCollection(),
                itemAddDto.value(),
                itemAddDto.createdDate(),
                itemAddDto.dateAquired(),
                itemAddDto.description()
            );

            itemRepository.add(item);
            return item;
        } catch (EntityArgumentException e) {
            throw new IllegalArgumentException(String.join(", ", e.getErrors()));
        }
    }

    @Override
    public void update(ItemUpdateDto itemUpdateDto) {
        User currentUser = authService.getUser();
        AntiqueCollection collection = getUserCollection(
            currentUser);
        List<Item> userItems = getAllByCollection(collection);

        Item existingItem = findItemById(userItems, itemUpdateDto.id());

        existingItem.setName(itemUpdateDto.name());
        existingItem.setCollection(itemUpdateDto.collection());
        existingItem.setValue(itemUpdateDto.value());
        existingItem.setCreatedDate(itemUpdateDto.createdDate());
        existingItem.setDateAquired(itemUpdateDto.dateAquired());
        existingItem.setDescription(itemUpdateDto.description());

        itemRepository.update(existingItem);
    }

    public Item findItemById(List<Item> items, UUID id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        throw new EntityNotFoundException("Предмет з таким ID не знайдено!");
    }

    @Override
    public void delete(UUID itemId) {
        User currentUser = authService.getUser();
        AntiqueCollection collection = getUserCollection(currentUser);
        List<Item> userItems = getAllByCollection(collection);

        Item itemToRemove = findItemById(userItems, itemId);
        itemRepository.remove(itemToRemove);
    }
}

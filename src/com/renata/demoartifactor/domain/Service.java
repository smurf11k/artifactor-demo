package com.renata.demoartifactor.domain;

import com.renata.demoartifactor.persistance.entity.Entity;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface Service<E extends Entity> {

    E get(UUID id);

    Set<E> getAll();

    Set<E> getAll(Predicate<E> filter);

    E add(E entity);

    boolean remove(E entity);
}

package com.renata.demoartifactor;

import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.repository.RepositoryFactory;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;

/**
 * Artifactor – Designed for organizing and managing your antique collections.
 *
 * @author Munkacsy Renata
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        RepositoryFactory repositoryFactory = JsonRepositoryFactory.getInstance();
        ServiceFactory serviceFactory = ServiceFactory.getInstance(
            repositoryFactory);

        Startup startup = new Startup(serviceFactory);
        startup.start();
    }
}

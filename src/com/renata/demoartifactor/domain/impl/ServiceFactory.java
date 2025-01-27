package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.AntiqueCollectionService;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.contract.CategoryService;
import com.renata.demoartifactor.domain.contract.ItemService;
import com.renata.demoartifactor.domain.contract.SignUpService;
import com.renata.demoartifactor.domain.contract.TagService;
import com.renata.demoartifactor.domain.contract.TransactionService;
import com.renata.demoartifactor.domain.contract.UserService;
import com.renata.demoartifactor.domain.exception.DependencyException;
import com.renata.demoartifactor.persistance.repository.RepositoryFactory;

public final class ServiceFactory {

    private static volatile ServiceFactory INSTANCE;
    private final AuthService authService;
    private final AntiqueCollectionService collectionService;
    private final ItemService itemService;
    private final TagService tagService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final SignUpService signUpService;
    private final RepositoryFactory repositoryFactory;

    private ServiceFactory(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        var userRepository = repositoryFactory.getUserRepository();
        authService = new AuthServiceImpl(userRepository);

        collectionService = new AntiqueCollectionServiceImpl(
            repositoryFactory.getCollectionRepository(),
            userRepository
        );
        itemService = new ItemServiceImpl(repositoryFactory.getItemRepository());
        tagService = new TagServiceImpl(repositoryFactory.getTagRepository());
        transactionService = new TransactionServiceImpl(
            repositoryFactory.getTransactionRepository());
        categoryService = new CategoryServiceImpl(repositoryFactory.getCategoryRepository());
        userService = new UserServiceImpl(userRepository);
        signUpService = new SignUpServiceImpl(userService);
    }

    /**
     * Використовувати, лише якщо впевнені, що існує об'єкт RepositoryFactory.
     *
     * @return екземпляр типу ServiceFactory
     */
    public static ServiceFactory getInstance() {
        if (INSTANCE.repositoryFactory != null) {
            return INSTANCE;
        } else {
            throw new DependencyException(
                "Ви забули створити обєкт RepositoryFactory, перед використанням ServiceFactory.");
        }
    }

    public static ServiceFactory getInstance(RepositoryFactory repositoryFactory) {
        if (INSTANCE == null) {
            synchronized (ServiceFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceFactory(repositoryFactory);
                }
            }
        }

        return INSTANCE;
    }

    public AuthService getAuthService() {
        return authService;
    }


    public AntiqueCollectionService getCollectionService() {
        return collectionService;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public TagService getTagService() {
        return tagService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public UserService getUserService() {
        return userService;
    }

    public SignUpService getSignUpService() {
        return signUpService;
    }

    public RepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }
}

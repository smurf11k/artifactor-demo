package com.renata.demoartifactor.appui;

import com.renata.demoartifactor.appui.forms.AddCollectionForm;
import com.renata.demoartifactor.appui.forms.AddItemForm;
import com.renata.demoartifactor.appui.forms.DeleteCollectionForm;
import com.renata.demoartifactor.appui.forms.DeleteItemForm;
import com.renata.demoartifactor.appui.forms.EditCollectionForm;
import com.renata.demoartifactor.appui.forms.EditItemForm;
import com.renata.demoartifactor.appui.pages.AntiqueCollectionView;
import com.renata.demoartifactor.appui.pages.AntiqueCollectionsView;
import com.renata.demoartifactor.appui.pages.AuthView;
import com.renata.demoartifactor.appui.pages.ItemView;
import com.renata.demoartifactor.appui.pages.ItemsView;
import com.renata.demoartifactor.appui.pages.MainMenuView;
import com.renata.demoartifactor.appui.pages.MarketplaceView;
import com.renata.demoartifactor.appui.pages.TransactionView;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.AntiqueCollection;
import com.renata.demoartifactor.persistance.entity.impl.User.Role;

public class PageFactory {

    private static PageFactory instance;
    private final ServiceFactory serviceFactory;

    public PageFactory(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public static PageFactory getInstance(ServiceFactory serviceFactory) {
        if (instance == null) {
            instance = new PageFactory(serviceFactory);
        }
        return instance;
    }

    // pages/views

    public Renderable createMainMenuView(Role userRole) {
        return new MainMenuView(userRole, serviceFactory);
    }

    public AuthView createAuthView() {
        return new AuthView(serviceFactory);
    }

    public Renderable createMarketplaceView(Role userRole) {
        return new MarketplaceView(userRole, serviceFactory);
    }

    public AntiqueCollectionsView createAntiqueCollectionsView() {
        return new AntiqueCollectionsView(serviceFactory);
    }

    // Метод для створення сторінки з колекцією
    public Renderable createAntiqueCollectionView() {
        return new AntiqueCollectionView(serviceFactory);
    }

    // Метод для створення сторінки з предметом, передаючи колекцію
    public Renderable createItemView(AntiqueCollection collection) {
        return new ItemView(serviceFactory, collection);
    }

    public ItemsView createItemsView() {
        return new ItemsView(serviceFactory);
    }

    public TransactionView createTransactionView() {
        return new TransactionView(serviceFactory);
    }

    // forms

    public AddCollectionForm createAddCollectionForm() {
        return new AddCollectionForm(serviceFactory);
    }

    public AddItemForm createAddItemForm() {
        return new AddItemForm(serviceFactory);
    }

    public DeleteCollectionForm createDeleteCollectionForm() {
        return new DeleteCollectionForm(serviceFactory);
    }

    public DeleteItemForm createDeleteItemForm() {
        return new DeleteItemForm(serviceFactory);
    }

    public EditCollectionForm createEditCollectionForm() {
        return new EditCollectionForm(serviceFactory);
    }

    public EditItemForm createEditItemForm() {
        return new EditItemForm(serviceFactory);
    }
}



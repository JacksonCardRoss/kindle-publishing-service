package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;

import javax.inject.Inject;

public class RemoveBookFromCatalogActivity {

    private CatalogDao catalogDao;

    /**
     * Instantiates a new GetBookActivity object.
     *
     * @param catalogDao CatalogDao to access the Catalog table.
     */
    @Inject
    RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    /**
     * Retrieves the book associated with the provided book id and marks the book as inactive
     *
     * @param removeBookFromCatalogRequest Request object containing the book ID associated with the book to get from the Catalog.
     * @return GetBookResponse Response object containing the requested book.
     */
    public RemoveBookFromCatalogResponse execute(RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {
        CatalogItemVersion catalogItem = catalogDao.getBookFromCatalog(removeBookFromCatalogRequest.getBookId());
        catalogItem.setInactive(true);
        catalogDao.saveBookToCatalog(catalogItem);

        return RemoveBookFromCatalogResponse.builder()
                .withBook(catalogItem)
                .build();
    }
}

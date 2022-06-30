package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;

public class BookPublishTask implements Runnable {

    private final CatalogDao catalogDao;
    private final PublishingStatusDao publishingStatusDao;
    @Singleton
    private final BookPublishRequestManager bookPublishRequestManager;


    @Inject
    public BookPublishTask(CatalogDao catalogDao, PublishingStatusDao publishingStatusDao, BookPublishRequestManager bookPublishRequestManager) {
        this.catalogDao = catalogDao;
        this.publishingStatusDao = publishingStatusDao;
        this.bookPublishRequestManager = bookPublishRequestManager;
    }

    public void processPublishRequest(BookPublishRequestManager bookPublishRequestManager) {
        BookPublishRequest bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();
        if (bookPublishRequest != null) {
            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS, bookPublishRequest.getBookId());
            KindleFormattedBook book = KindleFormatConverter.format(bookPublishRequest);
            try {
                CatalogItemVersion newBook = catalogDao.createOrUpdateBook(book);
                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL, newBook.getBookId());
            } catch (BookNotFoundException e) {
                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED, bookPublishRequest.getBookId(), e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        processPublishRequest(bookPublishRequestManager);
    }
}

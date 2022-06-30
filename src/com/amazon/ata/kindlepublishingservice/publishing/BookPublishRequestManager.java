package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequests;

    @Inject
    public BookPublishRequestManager() {
        bookPublishRequests = new ConcurrentLinkedQueue<>();
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        bookPublishRequests.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        BookPublishRequest request = bookPublishRequests.peek();
        if (request != null) {
            return bookPublishRequests.remove();
        }
        return null;
        //throw new PublishingStatusNotFoundException("No BookPublishRequests found in queue");

    }
}

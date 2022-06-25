package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;

import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {
    private Queue<BookPublishRequest> bookPublishRequests = new LinkedList<>();

    public BookPublishRequest addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        if (bookPublishRequest == null) {
            return null;
        }
        bookPublishRequests.add(bookPublishRequest);
        return bookPublishRequest;
    }

    public BookPublishRequest getBookPublishRequestToProcess () {
        if (bookPublishRequests == null) {
            throw new PublishingStatusNotFoundException("No bookPublishRequests found in queue");
        }
        return bookPublishRequests.remove();
    }
}

package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RemoveBookFromCatalogActivityTest {


    private static String BOOK_ID = "book.123";


    @Mock
    private CatalogDao catalogDao;

    @InjectMocks
    private RemoveBookFromCatalogActivity activity;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void execute_bookExists_setsBookAsInactive() {
        // GIVEN
        RemoveBookFromCatalogRequest request = RemoveBookFromCatalogRequest
                .builder()
                .withBookId(BOOK_ID)
                .build();
        CatalogItemVersion previousCatalogItem = new CatalogItemVersion();
        previousCatalogItem.setVersion(1);
        previousCatalogItem.setBookId(BOOK_ID);
        previousCatalogItem.setInactive(false);
        previousCatalogItem.setGenre(BookGenre.FANTASY);

        when(catalogDao.getBookFromCatalog(BOOK_ID)).thenReturn(previousCatalogItem);

        // WHEN
        RemoveBookFromCatalogResponse response = activity.execute(request);

        // THEN
        assertNotNull(response, "Expected request to return a non-null response.");
        assertNotNull(response.getBook(), "Expected a non null book in the response.");
        CatalogItemVersion book = response.getBook();
        assertEquals(BOOK_ID, book.getBookId(), "Expected book in response to contain id passed in request.");
        assertTrue(book.isInactive(), "Expected book in response to be inactive");
    }

    @Test
    public void execute_bookDoesNotExist_throwsException() {
        // GIVEN
        RemoveBookFromCatalogRequest request = RemoveBookFromCatalogRequest
                .builder()
                .withBookId("notAbook.123")
                .build();

        when(catalogDao.getBookFromCatalog("notAbook.123")).thenThrow(new BookNotFoundException("No book found"));

        // WHEN & THEN
        assertThrows(BookNotFoundException.class, () -> activity.execute(request), "Expected activity to " +
                "throw an exception if the book can't be found.");
    }

}

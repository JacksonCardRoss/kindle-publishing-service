package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public CatalogItemVersion saveBookToCatalog(CatalogItemVersion book) {
        dynamoDbMapper.save(book);
        return book;
    }

    public void validateBookExists(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);
        if (book == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook kindleBook) {
        CatalogItemVersion newBook = new CatalogItemVersion();

        if (kindleBook.getBookId() != null) {
            CatalogItemVersion oldBook = getBookFromCatalog(kindleBook.getBookId());
            if (oldBook == null) {
                throw new BookNotFoundException("Book was not found in catalog");
            }
            oldBook.setInactive(true);
            saveBookToCatalog(oldBook);
            newBook.setBookId(oldBook.getBookId());
            newBook.setVersion(oldBook.getVersion() + 1);

        } else { // if book is not found in catalog
            // if book does not exist in catalog - generates new id and sets the version to 1
            newBook.setBookId(KindlePublishingUtils.generateBookId());
            newBook.setVersion(1);
        }
        newBook.setTitle(kindleBook.getTitle());
        newBook.setAuthor(kindleBook.getAuthor());
        newBook.setText(kindleBook.getText());
        newBook.setGenre(kindleBook.getGenre());
        newBook.setInactive(false);
        saveBookToCatalog(newBook);

        return newBook;
    }
}

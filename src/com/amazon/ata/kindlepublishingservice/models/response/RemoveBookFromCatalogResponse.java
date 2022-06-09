package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;

import java.util.Objects;

public class RemoveBookFromCatalogResponse {

    private CatalogItemVersion book;

    public RemoveBookFromCatalogResponse(CatalogItemVersion book) {
        this.book = book;
    }

    public CatalogItemVersion getBook() {
        return book;
    }

    public void setBook(CatalogItemVersion book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveBookFromCatalogResponse that = (RemoveBookFromCatalogResponse) o;
        return Objects.equals(book, that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book);
    }

    public static Builder builder() {
        return new Builder();
    }

    public RemoveBookFromCatalogResponse(Builder builder) {
        this.book = builder.book;
    }
    public static final class Builder {
        private CatalogItemVersion book;

        private Builder() {

        }

        public Builder withBook(CatalogItemVersion book) {
            this.book = book;
            return this;
        }

        public RemoveBookFromCatalogResponse build() { return new RemoveBookFromCatalogResponse(this); }
    }
}

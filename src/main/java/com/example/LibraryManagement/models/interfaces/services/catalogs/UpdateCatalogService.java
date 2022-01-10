package com.example.LibraryManagement.models.interfaces.services.catalogs;

import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Set;

public interface UpdateCatalogService
{
    ResponseEntity<MessageResponse> addLibrary(String name, String streetAddress, String city,
                                                      String zipcode, String country);

    ResponseEntity<MessageResponse> addBookItem(String libraryName, Rack rack, String ISBN, String title,
                                                String publisher, String language, int numberOfPages,
                                                String authorName, Set<String> subjects, BookFormat format,
                                                Date publicationDate, boolean isReferenceOnly, double price);

    ResponseEntity<MessageResponse> modifyBookItem(Long barcode, String ISBN, String title, String publisher,
                                                   String language, int numberOfPages, String authorName,
                                                   Set<String> subjects, BookFormat format, Date publicationDate,
                                                   boolean isReferenceOnly, double price);
}

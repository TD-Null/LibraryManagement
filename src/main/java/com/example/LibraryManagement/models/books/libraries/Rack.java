package com.example.LibraryManagement.models.books.libraries;

import com.example.LibraryManagement.models.books.properties.BookItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * Books can be placed on racks. Each rack will be identified by a rack number
 * and will have a location identifier to describe the physical location of the
 * rack in the library.
 *
 * Each rack is associated with a single library, some of which will associate
 * with the same library.
 *
 * Each rack can contain multiple BookItems of its library.
 */
@Getter
@AllArgsConstructor
public class Rack
{
    @Column(name = "Number")
    private int number;

    @NotBlank
    @Column(name = "Location")
    private String location;
}

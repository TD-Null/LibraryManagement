package com.example.LibraryManagement.models.books.libraries;

import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.datatypes.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * The central part of the organization for which this software has been designed.
 * It has attributes like ‘Name’ to distinguish it from any other libraries and
 * ‘Address’ to describe its location.
 *
 * Each library will contain its own books as well as racks to contain the books
 * for their locations within the library.
 *
 * Librarians can also modify libraries to add book items and additional racks
 * to a given library.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "Name")
})
public class Library
{
    @Id
    @NotBlank
    @Column(name = "Name")
    private String name;

    @NotBlank
    @Column(name = "Street")
    private String streetAddress;

    @NotBlank
    @Column(name = "City")
    private String city;

    @NotBlank
    @Column(name = "Zipcode")
    private String zipcode;

    @NotBlank
    @Column(name = "Country")
    private String country;

    @JsonIgnore
    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE})
    private Set<BookItem> books = new HashSet<>();

    public Library(String name, Address address)
    {
        this.name = name;
        setAddress(address);
    }

    public void setAddress(Address address)
    {
        streetAddress = address.getStreetAddress();
        city = address.getCity();
        zipcode = address.getZipcode();
        country = address.getCountry();
    }

    public Address getAddress()
    {
        return new Address(streetAddress, city, zipcode, country);
    }

    public void addBookItem(BookItem b) { books.add(b); }

    public void removeBookItem(BookItem b) { books.remove(b); }
}

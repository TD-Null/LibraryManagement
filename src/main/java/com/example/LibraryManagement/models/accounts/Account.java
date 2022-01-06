package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 * Description:
 * There will be two types of accounts, a librarian and a member.
 *
 * Librarians will have access to adding and modifying book items,
 * while members will have access to borrowing, renewing, and
 * returning book items from a library.
 *
 * Each account will have a library card, a password, a status,
 * and the personal details of the user.
 */
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "Name")
    private String name;

    @NotBlank
    @Size(min = 6, max = 40)
    @Column(name = "Password")
    private String password;

    @Column(name = "Status", nullable = false)
    private AccountStatus status;

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

    @NotBlank
    @Column(name = "Email")
    private String email;

    @NotBlank
    @Column(name = "Phone_Number")
    private String phoneNumber;

    public Account(String name, String password, AccountStatus status,
                   Address address, String email, String phoneNumber)
    {
        this.name = name;
        this.password = password;
        this.status = status;
        setAddress(address);
        this.email = email;
        this.phoneNumber = phoneNumber;
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
}

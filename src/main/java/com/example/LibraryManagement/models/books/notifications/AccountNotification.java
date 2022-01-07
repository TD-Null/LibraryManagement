package com.example.LibraryManagement.models.books.notifications;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.datatypes.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table
public class AccountNotification
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member", nullable = false)
    private Member member;

    @Column(name = "Date", nullable = false)
    private Date createdOn;

    @Column(name = "Email")
    private String email;

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
    @Column(name = "Content")
    private String content;

    public AccountNotification(Date createdOn, String email, Address address, String content)
    {
        this.createdOn = createdOn;
        this.email = email;
        setAddress(address);
        this.content = content;
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

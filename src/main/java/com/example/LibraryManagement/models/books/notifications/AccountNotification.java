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

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @Column(name = "Date", nullable = false)
    private Date createdOn;

    @Column(name = "Email")
    private String email;

    @JsonIgnore
    @NotBlank
    @Column(name = "Street")
    private String streetAddress;

    @JsonIgnore
    @NotBlank
    @Column(name = "City")
    private String city;

    @JsonIgnore
    @NotBlank
    @Column(name = "Zipcode")
    private String zipcode;

    @JsonIgnore
    @NotBlank
    @Column(name = "Country")
    private String country;

    @NotBlank
    @Column(name = "Content")
    private String content;

    public AccountNotification(Member member, Date createdOn, String email, Address address, String content)
    {
        this.member = member;
        this.createdOn = createdOn;
        this.email = email;
        setAddress(address);
        this.content = content;
    }

    public Address getAddress()
    {
        return new Address(streetAddress, city, zipcode, country);
    }

    public void setAddress(Address address)
    {
        streetAddress = address.getStreetAddress();
        city = address.getCity();
        zipcode = address.getZipcode();
        country = address.getCountry();
    }
}

package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/*
 * Description:
 * Mainly responsible for adding and modifying books, book items, and users.
 * The Librarian will have access to updating accounts on their status as well
 * as check the current status of book items and their lending and reservation
 * statuses.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "Email")
})
public class Librarian extends Account
{
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "card")
    private LibraryCard libraryCard;

    public Librarian(String name, String password, AccountStatus status,
                     Address address, String email, String phoneNumber)
    {
        super(name, password, status, address, email, phoneNumber);
    }
}

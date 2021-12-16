package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Account
{
    private String id;
    private String password;
    private AccountStatus status;
    private Person person;

    public boolean resetPassword()
    {
        return true;
    }
}

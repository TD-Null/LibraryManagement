package com.example.LibraryManagement.models.enums.accounts;

/*
 * Lists the statuses that an account can be labelled with.
 *
 * ACTIVE: When a user first creates their account, it will
 * be ACTIVE and will stay ACTIVE unless the user's status
 * gets updated.
 *
 * CANCELLED: A user's account is CANCELLED when they've
 * requested to cancel their library membership.
 *
 * BLACKLISTED: A user's account is BLACKLISTED when a
 * librarian has blocked their account for miscellaneous
 * reasons.
 *
 * NONE: Status used in the case of being unable to identify
 * the account under any of the other statuses.
 */
public enum AccountStatus
{
    ACTIVE,
    CANCELLED,
    BLACKLISTED
}

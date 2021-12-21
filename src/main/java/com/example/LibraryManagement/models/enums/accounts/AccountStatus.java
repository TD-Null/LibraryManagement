package com.example.LibraryManagement.models.enums.accounts;

/*
 * Lists the statuses that an account can be labelled with.
 *
 * ACTIVE: When a user first creates their account, it will
 * be ACTIVE and will stay ACTIVE unless the user's status
 * gets updated.
 *
 * CLOSED: A user's account will be CLOSED when they have
 * a fine that needs to be paid.
 *
 * CANCELLED: A user's account is CANCELLED when they've
 * requested to cancel their library membership.
 *
 * BLACKLISTED: A user's account is BLACKLISTED when a
 * librarian has blocked their account for miscellaneous
 * reasons.
 *
 * NONE: Status used in the case unable to identify the
 * under any of the other statuses.
 */
public enum AccountStatus
{
    ACTIVE,
    CLOSED,
    CANCELLED,
    BLACKLISTED,
    NONE
}

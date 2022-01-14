package com.example.LibraryManagement.components.repositories.accounts;

import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountNotificationRepository extends JpaRepository<AccountNotification, Long>
{
}

package com.example.LibraryManagement.components.repositories.accounts;

import com.example.LibraryManagement.models.accounts.types.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>
{
}

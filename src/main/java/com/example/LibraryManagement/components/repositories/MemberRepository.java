package com.example.LibraryManagement.components.repositories;

import com.example.LibraryManagement.models.accounts.types.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String>
{
}

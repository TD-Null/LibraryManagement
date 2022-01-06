package com.example.LibraryManagement.components.repositories.books.libraries;

import com.example.LibraryManagement.models.books.libraries.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
}

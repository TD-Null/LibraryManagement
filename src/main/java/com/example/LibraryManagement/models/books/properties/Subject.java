package com.example.LibraryManagement.models.books.properties;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Subject
{
    @Id
    @NotBlank
    @Column(name = "Name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> books = new HashSet<>();
}

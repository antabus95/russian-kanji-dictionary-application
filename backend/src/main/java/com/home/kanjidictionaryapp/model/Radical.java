package com.home.kanjidictionaryapp.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "radicals")
public class Radical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String spelling;

    @Column(unique = true, nullable = false)
    private Integer number;

    @Column(nullable = false)
    private Integer strokeCount;

    @OneToMany(mappedBy = "radical", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RadicalForm> radicalForms = new ArrayList<>();
}

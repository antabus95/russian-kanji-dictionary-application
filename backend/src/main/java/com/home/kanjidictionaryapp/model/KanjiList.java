package com.home.kanjidictionaryapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "kanji_lists")
public class KanjiList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "kanjiList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<KanjiListElement> elements = new ArrayList<>();

}

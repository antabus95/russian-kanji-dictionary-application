package com.home.kanjidictionaryapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kanji_list_elements")
public class KanjiListElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "added_at", columnDefinition = "timestamp(6) without time zone")
    private LocalDateTime addedAt;

    @Column(name = "is_learned")
    private boolean isLearned;

    @ManyToOne
    @JoinColumn(name = "kanji_id")
    private Kanji kanji;

    @ManyToOne
    @JoinColumn(name = "kanji_list_id")
    @JsonBackReference
    private KanjiList kanjiList;

}

package com.home.kanjidictionaryapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="radical_forms")
public class RadicalForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="form_name", nullable=false)
    private String formName;

    @Column(name="spelling", unique=true, nullable=false)
    private String spelling;

    @Column(name = "alt_spelling")
    private String altSpelling;

    @ManyToOne
    @JoinColumn(name = "radical_id")
    @JsonBackReference
    private Radical radical;

    @OneToMany(mappedBy = "radicalForm", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Kanji> kanjies = new ArrayList<>();

}

package com.home.kanjidictionaryapp.repository;


import com.home.kanjidictionaryapp.model.Radical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RadicalRepository extends JpaRepository<Radical, Long>, JpaSpecificationExecutor<Radical> {

    @EntityGraph(attributePaths = "radicalForms")
    @Query(value = """
        SELECT r.* FROM radicals r
        JOIN radical_forms rf ON rf.radical_id = r.id
        WHERE rf.spelling = :spelling OR rf.alt_spelling = :spelling
        """, nativeQuery = true)
    Optional<Radical> findBySpelling(@Param("spelling") String spelling);

    @EntityGraph(attributePaths = "radicalForms")
    Optional<Radical> findByNumber(Integer number);

    @EntityGraph(attributePaths = "radicalForms")
    Page<Radical> findAllByStrokeCount(Integer strokeCount, Pageable pageable);

    @EntityGraph(attributePaths = "radicalForms")
    @Override
    Page<Radical> findAll(Pageable pageable);

}

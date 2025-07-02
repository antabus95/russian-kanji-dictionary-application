package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.Radical;
import com.home.kanjidictionaryapp.model.RadicalForm;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class RadicalSpecification {

    public static Specification<Radical> hasNumber(Integer number) {
        return (root, query, cb) -> number == null ? null : cb.equal(root.get("number"), number);
    }

    public static Specification<Radical> hasStrokeCount(Integer strokeCount) {
        return (root, query, cb) -> strokeCount == null ? null : cb.equal(root.get("strokeCount"), strokeCount);
    }

    public static Specification<Radical> hasSpellingInRadicalForms(String spelling) {
        return (root, query, cb) -> {
            if (spelling == null || spelling.isEmpty()) {
                return null;
            }
            query.distinct(true);
            Join<Radical, RadicalForm> join = root.join("radicalForms", JoinType.LEFT);
            return cb.or(
                    cb.like(cb.lower(join.get("spelling")), "%" + spelling.toLowerCase() + "%"),
                    cb.like(cb.lower(join.get("altSpelling")), "%" + spelling.toLowerCase() + "%")
            );
        };
    }

}

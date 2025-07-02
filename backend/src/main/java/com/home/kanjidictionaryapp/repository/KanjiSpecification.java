package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.Kanji;
import com.home.kanjidictionaryapp.model.KanjiCategory;
import com.home.kanjidictionaryapp.model.KanjiMeaning;
import com.home.kanjidictionaryapp.model.KanjiReading;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class KanjiSpecification {

    public static Specification<Kanji> hasJlptLvl(Integer jlptLvl) {
        return (root, query, cb) -> jlptLvl == null ? null : cb.equal(root.get("jlptLvl"), jlptLvl);
    }

    public static Specification<Kanji> hasKankenLvl(Float kankenLvl) {
        return (root, query, cb) -> kankenLvl == null ? null : cb.equal(root.get("kankenLvl"), kankenLvl);
    }

    public static Specification<Kanji> hasStrokeCount(Integer strokeCount) {
        return (root, query, cb) -> strokeCount == null ? null : cb.equal(root.get("strokeCount"), strokeCount);
    }

    public static Specification<Kanji> hasCategories(List<KanjiCategory> categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.isEmpty()) {
                return null;
            }
            return root.get("category").in(categories);
        };
    }

    public static Specification<Kanji> hasMeaningInBaseMeanings(String meaning) {
        return (root, query, cb) -> {
            if (meaning == null || meaning.isEmpty()) {
                return null;
            }
            query.distinct(true);
            Join<Kanji, KanjiMeaning> join = root.join("baseMeanings", JoinType.LEFT);
            return cb.like(cb.lower(join.get("meaning")), "%" + meaning.toLowerCase() + "%");
        };
    }

    public static Specification<Kanji> hasTextInReadings(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) {
                return null;
            }
            query.distinct(true);
            Join<Kanji, KanjiReading> join = root.join("readings", JoinType.LEFT);
            return cb.equal(join.get("text"), text);
        };
    }

    public static Specification<Kanji> hasSpelling(String spelling) {
        return (root, query, cb) -> spelling == null ? null : cb.equal(root.get("spelling"), spelling);
    }


}


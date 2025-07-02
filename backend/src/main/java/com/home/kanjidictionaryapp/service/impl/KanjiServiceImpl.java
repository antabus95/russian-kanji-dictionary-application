package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.kanji.*;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.kanji.*;
import com.home.kanjidictionaryapp.model.*;
import com.home.kanjidictionaryapp.repository.KanjiRepository;
import com.home.kanjidictionaryapp.repository.KanjiSpecification;
import com.home.kanjidictionaryapp.repository.RadicalFormRepository;
import com.home.kanjidictionaryapp.service.KanjiService;
import com.home.kanjidictionaryapp.service.RadicalFormService;
import com.home.kanjidictionaryapp.util.KanaConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanjiServiceImpl implements KanjiService {

    private final KanjiRepository kanjiRepository;
    private final ReadKanjiMapper readKanjiMapper;
    private final CreateKanjiMapper createKanjiMapper;
    private final ReadUpdateKanjiReadingMapper readUpdateKanjiReadingMapper;
    private final ReadUpdateKanjiMeaningMapper readUpdateKanjiMeaningMapper;
    private final RadicalFormService radicalFormService;
    private final RadicalFormRepository radicalFormRepository;
    private final KanaConverter kanaConverter;

    private ReadKanjiDto mapToReadKanjiDto(Kanji k) {
        return readKanjiMapper.toDto(k, k.getTraditionalFormIds().stream().map(this::getKanjiForm).collect(Collectors.toList()),
                k.getSimplifiedFormIds().stream().map(this::getKanjiForm).collect(Collectors.toList()),
                k.getAlternativeFormIds().stream().map(this::getKanjiForm).collect(Collectors.toList()));
    }

    @Override
    public Optional<ReadKanjiDto> getById(Long id) {
        return kanjiRepository.findById(id).map(this::mapToReadKanjiDto);
    }

    @Override
    public Kanji getKanjiById(Long id) {
        return kanjiRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<ReadKanjiDto> getAll(Pageable pageable) {
        return kanjiRepository.findAll(pageable).map(this::mapToReadKanjiDto);
    }

    @Override
    public Page<ReadKanjiDto> getAllWithFilter(Integer jlptLvl,
                                               Float kankenLvl,
                                               Integer strokeCount,
                                               List<KanjiCategory> categories,
                                               String meaning,
                                               String reading,
                                               String spelling,
                                               Pageable pageable) {

        Specification<Kanji> spec = Specification
                .where(KanjiSpecification.hasJlptLvl(jlptLvl))
                .and(KanjiSpecification.hasKankenLvl(kankenLvl))
                .and(KanjiSpecification.hasStrokeCount(strokeCount))
                .and(KanjiSpecification.hasCategories(categories))
                .and(KanjiSpecification.hasMeaningInBaseMeanings(meaning))
                .and(KanjiSpecification.hasTextInReadings(kanaConverter.convertToHiragana(reading)))
                .and(KanjiSpecification.hasSpelling(spelling));

        Page<Kanji> kanjies = kanjiRepository.findAll(spec, pageable);
        return kanjies.map(this::mapToReadKanjiDto);

    }

    @Override
    public Page<ReadKanjiDto> getAllByRadicalId(Long radicalId, Pageable pageable) {
        return kanjiRepository.findAllByRadicalId(radicalId,pageable).map(this::mapToReadKanjiDto);
    }

    @Override
    public KanjiFormDto getKanjiForm(Long id) {
        Kanji kanji = kanjiRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Kanji not found"));
        return new KanjiFormDto(id, kanji.getSpelling());
    }

    public ReadUpdateRadicalFormDto getRadicalForm(Long id) {
        ReadUpdateRadicalFormDto radicalForm = radicalFormService.getById(id).orElseThrow(()-> new EntityNotFoundException("Radical not found"));
        return new ReadUpdateRadicalFormDto(id, radicalForm.getFormName(), radicalForm.getSpelling(), radicalForm.getAltSpelling(), false);
    }

    @Transactional
    @Override
    public Optional<ReadKanjiDto> update(Long id, UpdateKanjiDto kanji) {
        return kanjiRepository.findById(id)
                .map(entity -> {
                    entity.setSpelling(kanji.getSpelling());
                    List<KanjiMeaning> meanings = kanji.getBaseMeanings().stream()
                                    .map(readUpdateKanjiMeaningMapper::toEntity)
                                            .peek(m-> m.setKanji(entity)).toList();
                    entity.getBaseMeanings().clear();
                    entity.getBaseMeanings().addAll(meanings);

                    List<KanjiReading> readings = kanji.getReadings().stream()
                            .map(readUpdateKanjiReadingMapper::toEntity)
                            .peek(r-> r.setKanji(entity)).toList();
                    entity.getReadings().clear();
                    entity.getReadings().addAll(readings);

                    entity.setMeanings(kanji.getMeanings());
                    entity.setEtymology(kanji.getEtymology());
                    entity.setStrokeCount(kanji.getStrokeCount());
                    entity.setCategory(kanji.getCategory());

                    RadicalForm radicalForm = radicalFormRepository.findById(kanji.getRadicalFormId())
                            .orElseThrow(() -> new EntityNotFoundException("RadicalForm not found"));
                    entity.setRadicalForm(radicalForm);

                    entity.setJlptLvl(kanji.getJlptLvl());
                    entity.setKankenLvl(kanji.getKankenLvl());
                    entity.setJisCode(kanji.getJisCode());
                    entity.setUnicode(kanji.getUnicode());
                    entity.setTraditionalFormIds(kanji.getTraditionalForms());
                    entity.setSimplifiedFormIds(kanji.getSimplifiedForms());
                    entity.setAlternativeFormIds(kanji.getAlternativeForms());
                    return entity;
                }).map(kanjiRepository::saveAndFlush)
                .map(this::mapToReadKanjiDto);
    }

    @Transactional
    @Override
    public ReadKanjiDto create(CreateKanjiDto kanji) {
        RadicalForm radicalForm = radicalFormRepository.findById(kanji.getRadicalFormId())
                .orElseThrow(() -> new EntityNotFoundException("RadicalForm not found"));
        return Optional.of(kanji).map(k -> createKanjiMapper.toEntity(k, radicalForm))
                .map(kanjiRepository::save)
                .map(this::mapToReadKanjiDto)
                .orElseThrow(()-> new RuntimeException("Could not create kanji"));
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return kanjiRepository.findById(id)
                .map(entity -> {
                    kanjiRepository.delete(entity);
                    kanjiRepository.flush();
                    return true;
                }).orElse(false);
    }
}

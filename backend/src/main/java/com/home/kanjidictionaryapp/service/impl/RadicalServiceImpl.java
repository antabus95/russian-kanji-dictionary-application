package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.radical.CreateRadicalDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.model.Radical;
import com.home.kanjidictionaryapp.model.RadicalForm;
import com.home.kanjidictionaryapp.repository.RadicalRepository;
import com.home.kanjidictionaryapp.repository.RadicalSpecification;
import com.home.kanjidictionaryapp.service.RadicalFormService;
import com.home.kanjidictionaryapp.service.RadicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RadicalServiceImpl implements RadicalService {

    private final RadicalRepository radicalRepository;
    private final CreateMapper<Radical, CreateRadicalDto> createRadicalMapper;
    private final ReadUpdateMapper<Radical, ReadUpdateRadicalDto> readUpdateRadicalMapper;
    private final RadicalFormService radicalFormService;

    @Override
    public Optional<ReadUpdateRadicalDto> getById(Long id) {
        return radicalRepository.findById(id).map(readUpdateRadicalMapper::toDto);
    }

    @Override
    public Optional<ReadUpdateRadicalDto> getByNumber(Integer number) {
        return radicalRepository.findByNumber(number).map(readUpdateRadicalMapper::toDto);
    }

    @Override
    public Page<ReadUpdateRadicalDto> getAll(Pageable pageable) {
        return radicalRepository.findAll(pageable).map(readUpdateRadicalMapper::toDto);
    }

    @Override
    public Page<ReadUpdateRadicalDto> getAllWithFilter(Integer strokeCount,
                                                       Integer number,
                                                       String spelling,
                                                       Pageable pageable) {

        Specification<Radical> spec = Specification
                .where(RadicalSpecification.hasStrokeCount(strokeCount))
                .and(RadicalSpecification.hasNumber(number))
                .and(RadicalSpecification.hasSpellingInRadicalForms(spelling));

        Page<Radical> radicals = radicalRepository.findAll(spec, pageable);
        return radicals.map(readUpdateRadicalMapper::toDto);

    }

    @Override
    public Optional<ReadUpdateRadicalDto> getBySpelling(String spelling) {
        return radicalRepository.findBySpelling(spelling).map(readUpdateRadicalMapper::toDto);
    }

    @Transactional
    @Override
    public Optional<ReadUpdateRadicalDto> update(Long id, ReadUpdateRadicalDto radical) {
        Optional<Radical> existingRadical = radicalRepository.findByNumber(radical.getNumber());
        if (existingRadical.isPresent() && !existingRadical.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ключ с таким номером уже существует");
        }
        return radicalRepository.findById(id)
                .map(entity -> {
                    entity.setName(radical.getName());
                    entity.setNumber(radical.getNumber());
                    entity.setStrokeCount(radical.getStrokeCount());
                    entity.setSpelling(radical.getSpelling());

                    List<Long> idsToDelete = radical.getRadicalForms().stream()
                            .filter(f -> Boolean.TRUE.equals(f.get_deleted()) && f.getId() != null)
                            .map(ReadUpdateRadicalFormDto::getId)
                            .toList();

                    idsToDelete.forEach(radicalFormService::deleteById);

                    entity.getRadicalForms().removeIf(f -> idsToDelete.contains(f.getId()));

                    radicalRepository.flush();

                    List<RadicalForm> mergedForms = mergeForms(entity.getRadicalForms(),
                            radical.getRadicalForms(), entity);
                    entity.getRadicalForms().addAll(mergedForms);

                    return entity;
                }).map(radicalRepository::saveAndFlush)
                .map(readUpdateRadicalMapper::toDto);
    }

    private List<RadicalForm> mergeForms(List<RadicalForm> existingForms,
                                         List<ReadUpdateRadicalFormDto> incomingForms,
                                         Radical radical) {
        Map<Long, RadicalForm> existingById = existingForms.stream()
                .filter(f -> f.getId()!=null)
                .collect(Collectors.toMap(RadicalForm::getId, f -> f));

        List<RadicalForm> result = new ArrayList<>();

        for(ReadUpdateRadicalFormDto dto : incomingForms) {
            if (Boolean.TRUE.equals(dto.get_deleted())){
                continue;
            }
            RadicalForm form = (dto.getId()!=null && existingById.containsKey(dto.getId()))
                    ? existingById.get(dto.getId())
                    : new RadicalForm();

            form.setSpelling(dto.getSpelling());
            form.setFormName(dto.getFormName());
            form.setAltSpelling(dto.getAltSpelling());
            form.setRadical(radical);
            result.add(form);

        }
        return result;
    }

    @Transactional
    @Override
    public ReadUpdateRadicalDto create(CreateRadicalDto radical) {
        if (radicalRepository.findByNumber(radical.getNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ключ с таким номером уже существует");
        }
        return Optional.of(radical)
                .map(createRadicalMapper::toEntity)
                .map(radicalRepository::save)
                .map(readUpdateRadicalMapper::toDto)
                .orElseThrow(()-> new RuntimeException("Could not create radical"));
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return radicalRepository.findById(id)
                .map(entity -> {
                    radicalRepository.delete(entity);
                    radicalRepository.flush();
                    return true;
                }).orElse(false);
    }
}

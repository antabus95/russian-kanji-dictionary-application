package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.radical.CreateRadicalFormDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.mapper.radical.CreateRadicalFormMapper;
import com.home.kanjidictionaryapp.mapper.radical.ReadUpdateRadicalFormMapper;
import com.home.kanjidictionaryapp.repository.RadicalFormRepository;
import com.home.kanjidictionaryapp.service.RadicalFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RadicalFormServiceImpl implements RadicalFormService {

    private final RadicalFormRepository radicalFormRepository;
    private final CreateRadicalFormMapper createRadicalFormMapper;
    private final ReadUpdateRadicalFormMapper readUpdateRadicalFormMapper;

    @Override
    public Optional<ReadUpdateRadicalFormDto> getById(Long id) {
        return radicalFormRepository.findById(id).map(readUpdateRadicalFormMapper::toDto);
    }

    @Override
    public List<ReadUpdateRadicalFormDto> getAll() {
        return radicalFormRepository.findAll().stream().map(readUpdateRadicalFormMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ReadUpdateRadicalFormDto create(CreateRadicalFormDto createRadicalFormDto) {
        return Optional.of(createRadicalFormDto)
                .map(createRadicalFormMapper::toEntity)
                .map(radicalFormRepository::save)
                .map(readUpdateRadicalFormMapper::toDto)
                .orElseThrow(()-> new RuntimeException("Could not create radical form"));
    }

    @Transactional
    @Override
    public Optional<ReadUpdateRadicalFormDto> update(ReadUpdateRadicalFormDto readUpdateRadicalFormDto) {
        return radicalFormRepository.findById(readUpdateRadicalFormDto.getId())
                .map(entity -> {
                    entity.setFormName(readUpdateRadicalFormDto.getFormName());
                    entity.setSpelling(readUpdateRadicalFormDto.getSpelling());
                    entity.setAltSpelling(readUpdateRadicalFormDto.getAltSpelling());
                    return entity;
                })
                .map(radicalFormRepository::saveAndFlush)
                .map(readUpdateRadicalFormMapper::toDto);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return radicalFormRepository.findById(id)
                .map(entity -> {
                    radicalFormRepository.delete(entity);
                    radicalFormRepository.flush();
                    return true;
                }).orElse(false);
    }
}

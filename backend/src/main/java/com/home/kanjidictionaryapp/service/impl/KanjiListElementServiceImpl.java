package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListElementRequestDto;
import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListElementResponseDto;
import com.home.kanjidictionaryapp.mapper.kanji.ReadKanjiMapper;
import com.home.kanjidictionaryapp.model.KanjiList;
import com.home.kanjidictionaryapp.model.KanjiListElement;
import com.home.kanjidictionaryapp.repository.KanjiListElementRepository;
import com.home.kanjidictionaryapp.repository.KanjiListRepository;
import com.home.kanjidictionaryapp.service.KanjiListElementService;
import com.home.kanjidictionaryapp.service.KanjiService;
import com.home.kanjidictionaryapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanjiListElementServiceImpl implements KanjiListElementService {

    private final KanjiListElementRepository kanjiListElementRepository;
    private final KanjiListRepository kanjiListRepository;
    private final KanjiService kanjiService;
    private final UserService userService;
    private final ReadKanjiMapper readKanjiMapper;


    @Transactional
    @Override
    public KanjiListElementResponseDto create(Long listId, KanjiListElementRequestDto dto) {
        KanjiListElement kanjiListElement = new KanjiListElement();
        kanjiListElement.setNote(dto.getNote());
        kanjiListElement.setKanji(kanjiService.getKanjiById(dto.getKanjiId()));
        kanjiListElement.setLearned(false);
        kanjiListElement.setAddedAt(dto.getAddedAt());
        KanjiList kanjiList = kanjiListRepository.findByIdAndUserId(listId, userService.getCurrentUser().getId()).orElseThrow();
        kanjiListElement.setKanjiList(kanjiList);
        kanjiList.getElements().add(kanjiListElement);
        kanjiListRepository.saveAndFlush(kanjiList);
        kanjiListElementRepository.saveAndFlush(kanjiListElement);
        return new KanjiListElementResponseDto(kanjiListElement.getId(),
                kanjiListElement.getNote(),
                kanjiListElement.getAddedAt(),
                kanjiListElement.isLearned(),
                readKanjiMapper.toDto(kanjiListElement.getKanji(),
                        kanjiListElement.getKanji().getTraditionalFormIds().stream().map(kanjiService::getKanjiForm).toList(),
                        kanjiListElement.getKanji().getSimplifiedFormIds().stream().map(kanjiService::getKanjiForm).toList(),
                        kanjiListElement.getKanji().getAlternativeFormIds().stream().map(kanjiService::getKanjiForm).toList()));
    }

    @Transactional
    @Override
    public KanjiListElementResponseDto update(Long listId, Long id, KanjiListElementRequestDto dto) {
        kanjiListRepository.findByIdAndUserId(listId, userService.getCurrentUser().getId()).orElseThrow();
        KanjiListElement kanjiListElement = kanjiListElementRepository.findById(id).orElseThrow();
        kanjiListElement.setNote(dto.getNote());
        kanjiListElement.setAddedAt(dto.getAddedAt());
        kanjiListElement.setLearned(dto.getIsLearned());
        kanjiListElementRepository.saveAndFlush(kanjiListElement);
        return new KanjiListElementResponseDto(kanjiListElement.getId(),
                kanjiListElement.getNote(),
                kanjiListElement.getAddedAt(),
                kanjiListElement.isLearned(),
                readKanjiMapper.toDto(kanjiListElement.getKanji(),
                        kanjiListElement.getKanji().getTraditionalFormIds().stream().map(kanjiService::getKanjiForm).toList(),
                        kanjiListElement.getKanji().getSimplifiedFormIds().stream().map(kanjiService::getKanjiForm).toList(),
                        kanjiListElement.getKanji().getAlternativeFormIds().stream().map(kanjiService::getKanjiForm).toList()));
    }

    @Transactional
    @Override
    public void delete(Long listId, Long id) {
        KanjiListElement element = kanjiListElementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kanji list element not found"));

        Long currentUserId = userService.getCurrentUser().getId();
        KanjiList kanjiList = element.getKanjiList();

        if (!kanjiList.getId().equals(listId) || !kanjiList.getUser().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Element does not belong to the specified list");
        }

        kanjiList.getElements().remove(element);
        kanjiListRepository.saveAndFlush(kanjiList);
        kanjiListElementRepository.delete(element);
        kanjiListElementRepository.flush();
    }

}

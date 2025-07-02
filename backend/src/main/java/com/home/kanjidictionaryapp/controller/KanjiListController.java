package com.home.kanjidictionaryapp.controller;

import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListElementRequestDto;
import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListRequestDto;
import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListResponseDto;
import com.home.kanjidictionaryapp.dto.kanjiList.KanjiListElementResponseDto;
import com.home.kanjidictionaryapp.service.KanjiListElementService;
import com.home.kanjidictionaryapp.service.KanjiListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/myLists")
@RequiredArgsConstructor
public class KanjiListController {

    private final KanjiListService kanjiListService;
    private final KanjiListElementService kanjiListElementService;

    @GetMapping
    public Page<KanjiListResponseDto> getAllByUser(Pageable pageable) {
        return kanjiListService.getAllByUser(pageable);
    }

    @GetMapping(params = "name")
    public KanjiListResponseDto getByName(@RequestParam("name") String name) {
        return kanjiListService.getByName(name);
    }

    @GetMapping(params = "spelling")
    public Page<KanjiListResponseDto> getAllByKanjiSpelling(@RequestParam("spelling") String spelling,
                                                         Pageable pageable) {
        return kanjiListService.getAllByKanjiSpelling(spelling, pageable);
    }

    @PostMapping
    public KanjiListResponseDto create(@Valid @RequestBody KanjiListRequestDto kanjiListRequestDto) {
        return kanjiListService.create(kanjiListRequestDto);
    }

    @PostMapping("/{listId}/")
    public KanjiListElementResponseDto create(@PathVariable Long listId, @RequestBody KanjiListElementRequestDto kanjiListElementRequestDto) {
        return kanjiListElementService.create(listId, kanjiListElementRequestDto);
    }

    @PutMapping("/{id}")
    public KanjiListResponseDto update(@PathVariable Long id, @RequestBody KanjiListRequestDto kanjiListRequestDto) {
        return kanjiListService.update(id, kanjiListRequestDto);
    }

    @PutMapping("{listId}/{id}")
    public KanjiListElementResponseDto update(@PathVariable Long listId,
                                              @PathVariable Long id,
                                              @RequestBody KanjiListElementRequestDto kanjiListElementRequestDto) {
        return kanjiListElementService.update(listId, id, kanjiListElementRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        kanjiListService.delete(id);
    }

    @DeleteMapping("{listId}/{id}")
    public void delete(@PathVariable Long listId, @PathVariable Long id) {
        kanjiListElementService.delete(listId, id);

    }


}

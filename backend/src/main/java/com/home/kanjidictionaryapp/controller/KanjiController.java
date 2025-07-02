package com.home.kanjidictionaryapp.controller;

import com.home.kanjidictionaryapp.dto.PageResponse;
import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiDto;
import com.home.kanjidictionaryapp.dto.kanji.ReadKanjiDto;
import com.home.kanjidictionaryapp.dto.kanji.UpdateKanjiDto;
import com.home.kanjidictionaryapp.model.KanjiCategory;
import com.home.kanjidictionaryapp.service.KanjiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/kanji")
@RequiredArgsConstructor
public class KanjiController {

    private final KanjiService kanjiService;

    @GetMapping
    public PageResponse<ReadKanjiDto> getAll(@RequestParam(required = false) Integer jlptLvl,
                                             @RequestParam(required = false) Float kankenLvl,
                                             @RequestParam(required = false) Integer strokeCount,
                                             @RequestParam(required = false) List<KanjiCategory> categories,
                                             @RequestParam(required = false) String reading,
                                             @RequestParam(required = false) String meaning,
                                             @RequestParam(required = false) String spelling,
                                             @PageableDefault(size = 10, sort = "strokeCount", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ReadKanjiDto> page = kanjiService.getAllWithFilter(jlptLvl, kankenLvl, strokeCount, categories, meaning, reading, spelling, pageable);
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @GetMapping("/{id}")
    public ReadKanjiDto getById(@PathVariable("id") Long id) {
        return kanjiService.getById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/search/radical={radicalId}")
    public PageResponse<ReadKanjiDto> getAllByRadicalId(@PathVariable("radicalId") Long radicalId,
                                                      @PageableDefault(size = 10, sort = "strokeCount", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ReadKanjiDto> page = kanjiService.getAllByRadicalId(radicalId, pageable);
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadKanjiDto create(@Validated @RequestBody CreateKanjiDto createKanjiDto){
        return kanjiService.create(createKanjiDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EDITOR')")
    @PutMapping("/{id}")
    public ReadKanjiDto update(@PathVariable("id") long id, @Validated @RequestBody UpdateKanjiDto updateKanjiDto){
        return kanjiService.update(id, updateKanjiDto)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if(!kanjiService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}

package com.home.kanjidictionaryapp.controller;

import com.home.kanjidictionaryapp.dto.PageResponse;
import com.home.kanjidictionaryapp.dto.kanji.ReadKanjiDto;
import com.home.kanjidictionaryapp.dto.radical.CreateRadicalDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalDto;
import com.home.kanjidictionaryapp.dto.radical.ReadUpdateRadicalFormDto;
import com.home.kanjidictionaryapp.service.KanjiService;
import com.home.kanjidictionaryapp.service.RadicalFormService;
import com.home.kanjidictionaryapp.service.RadicalService;
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
@RequestMapping("api/v1/radicals")
@RequiredArgsConstructor
public class RadicalController {

    private final RadicalService radicalService;
    private final RadicalFormService radicalFormService;
    private final KanjiService kanjiService;

    @GetMapping
    public PageResponse<ReadUpdateRadicalDto> getAll(@RequestParam(required = false) Integer strokeCount,
                                                     @RequestParam(required = false) Integer number,
                                                     @RequestParam(required = false) String spelling,
                                                     @PageableDefault(size = 250, sort = "number", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ReadUpdateRadicalDto> page = radicalService.getAllWithFilter(strokeCount, number, spelling, pageable);
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @GetMapping("/forms")
    public List<ReadUpdateRadicalFormDto> getAllForms(){
        return radicalFormService.getAll();
    }

    @GetMapping("/{id}")
    public ReadUpdateRadicalDto getById(@PathVariable("id") Long id) {
        return radicalService.getById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/kanji")
    public PageResponse<ReadKanjiDto> getAllByRadicalId(@PathVariable("id") Long radicalId,
                                                        @PageableDefault(size = 10, sort = "strokeCount", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ReadKanjiDto> page = kanjiService.getAllByRadicalId(radicalId, pageable);
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadUpdateRadicalDto create(@Validated @RequestBody CreateRadicalDto createRadicalDto) {
        return radicalService.create(createRadicalDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ReadUpdateRadicalDto update(@PathVariable("id") long id, @Validated @RequestBody ReadUpdateRadicalDto radical) {
        return radicalService.update(id, radical)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        if(!radicalService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}

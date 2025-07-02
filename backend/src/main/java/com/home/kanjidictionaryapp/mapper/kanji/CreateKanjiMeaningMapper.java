package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiMeaningDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.model.KanjiMeaning;
import org.springframework.stereotype.Component;

@Component
public class CreateKanjiMeaningMapper implements CreateMapper<KanjiMeaning, CreateKanjiMeaningDto> {

    @Override
    public KanjiMeaning toEntity(CreateKanjiMeaningDto createKanjiMeaningDto) {
        KanjiMeaning kanjiMeaning = new KanjiMeaning();
        kanjiMeaning.setMeaning(createKanjiMeaningDto.getMeaning());
        return kanjiMeaning;
    }

    @Override
    public CreateKanjiMeaningDto toDto(KanjiMeaning kanjiMeaning) {
        return new CreateKanjiMeaningDto(
                kanjiMeaning.getMeaning()
        );
    }
}

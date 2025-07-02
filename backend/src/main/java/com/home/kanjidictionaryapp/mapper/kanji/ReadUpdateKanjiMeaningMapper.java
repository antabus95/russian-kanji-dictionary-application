package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.ReadUpdateKanjiMeaningDto;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.model.KanjiMeaning;
import org.springframework.stereotype.Component;

@Component
public class ReadUpdateKanjiMeaningMapper implements ReadUpdateMapper<KanjiMeaning, ReadUpdateKanjiMeaningDto> {

    @Override
    public KanjiMeaning toEntity(ReadUpdateKanjiMeaningDto readUpdateKanjiMeaningDto) {
        KanjiMeaning kanjiMeaning = new KanjiMeaning();
        kanjiMeaning.setId(readUpdateKanjiMeaningDto.getId());
        kanjiMeaning.setMeaning(readUpdateKanjiMeaningDto.getMeaning());
        return kanjiMeaning;
    }

    @Override
    public ReadUpdateKanjiMeaningDto toDto(KanjiMeaning kanjiMeaning) {
        return new ReadUpdateKanjiMeaningDto(
                kanjiMeaning.getId(), kanjiMeaning.getMeaning()
        );
    }
}

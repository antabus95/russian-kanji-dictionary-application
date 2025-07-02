package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.CreateKanjiReadingDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.model.KanjiReading;
import com.home.kanjidictionaryapp.model.ReadingType;
import org.springframework.stereotype.Component;

@Component
public class CreateKanjiReadingMapper implements CreateMapper<KanjiReading, CreateKanjiReadingDto> {

    @Override
    public KanjiReading toEntity(CreateKanjiReadingDto createKanjiReadingDto) {
        KanjiReading kanjiReading = new KanjiReading();
        if(createKanjiReadingDto.getReadingType().equals(ReadingType.ON)) {
            kanjiReading.setText(katakanaToHiragana(createKanjiReadingDto.getText()));
        } else {
            kanjiReading.setText(createKanjiReadingDto.getText());
        }
        kanjiReading.setReadingType(createKanjiReadingDto.getReadingType());
        kanjiReading.setChineseReadingCategory(createKanjiReadingDto.getChineseReadingCategory());
        kanjiReading.setJoyo(createKanjiReadingDto.isJoyo());
        return kanjiReading;
    }

    @Override
    public CreateKanjiReadingDto toDto(KanjiReading kanjiReading) {
        return new CreateKanjiReadingDto(
                kanjiReading.getText(),
                kanjiReading.getReadingType(),
                kanjiReading.getChineseReadingCategory(),
                kanjiReading.isJoyo()
        );
    }

    private String katakanaToHiragana(String input) {
        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= 'ァ' && ch <= 'ヶ') {
                result.append((char)(ch - 0x60));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }


}

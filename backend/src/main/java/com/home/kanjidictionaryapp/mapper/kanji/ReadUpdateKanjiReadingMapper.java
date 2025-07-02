package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.ReadUpdateKanjiReadingDto;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.model.KanjiReading;
import com.home.kanjidictionaryapp.model.ReadingType;
import org.springframework.stereotype.Component;

@Component
public class ReadUpdateKanjiReadingMapper implements ReadUpdateMapper<KanjiReading, ReadUpdateKanjiReadingDto> {

    @Override
    public KanjiReading toEntity(ReadUpdateKanjiReadingDto readUpdateKanjiReadingDto) {
        KanjiReading kanjiReading = new KanjiReading();
        kanjiReading.setId(readUpdateKanjiReadingDto.getId());
        if (readUpdateKanjiReadingDto.getReadingType().equals(ReadingType.ON)) {
            kanjiReading.setText(katakanaToHiragana(readUpdateKanjiReadingDto.getText()));
        } else {
            kanjiReading.setText(readUpdateKanjiReadingDto.getText());
        }
        kanjiReading.setReadingType(readUpdateKanjiReadingDto.getReadingType());
        kanjiReading.setChineseReadingCategory(readUpdateKanjiReadingDto.getChineseReadingCategory());
        kanjiReading.setJoyo(readUpdateKanjiReadingDto.isJoyo());
        return kanjiReading;
    }

    @Override
    public ReadUpdateKanjiReadingDto toDto(KanjiReading kanjiReading) {
        return new ReadUpdateKanjiReadingDto(
                kanjiReading.getId(),
                kanjiReading.getReadingType().equals(ReadingType.ON) ?
                        hiraganaToKatakana(kanjiReading.getText()) :
                        kanjiReading.getText(),
                kanjiReading.getReadingType(),
                kanjiReading.getChineseReadingCategory(),
                kanjiReading.isJoyo()
                );
    }

    private String hiraganaToKatakana(String input) {
        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= 'ぁ' && ch <= 'ゖ') {
                result.append((char)(ch + 0x60));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
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

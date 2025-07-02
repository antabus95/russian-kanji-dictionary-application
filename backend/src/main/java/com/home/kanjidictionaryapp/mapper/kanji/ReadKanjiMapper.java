package com.home.kanjidictionaryapp.mapper.kanji;

import com.home.kanjidictionaryapp.dto.kanji.KanjiFormDto;
import com.home.kanjidictionaryapp.dto.kanji.ReadKanjiDto;
import com.home.kanjidictionaryapp.mapper.radical.ReadUpdateRadicalFormMapper;
import com.home.kanjidictionaryapp.model.Kanji;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReadKanjiMapper {

    private final ReadUpdateKanjiReadingMapper readUpdateKanjiReadingMapper;
    private final ReadUpdateKanjiMeaningMapper readUpdateKanjiMeaningMapper;
    private final ReadUpdateRadicalFormMapper readUpdateRadicalFormMapper;

    public ReadKanjiDto toDto(Kanji kanji,
                              List<KanjiFormDto> traditionalForms,
                              List<KanjiFormDto> simplifiedForms,
                              List<KanjiFormDto> alternativeForms) {
        return new ReadKanjiDto(
                kanji.getId(),
                kanji.getSpelling(),
                kanji.getBaseMeanings().stream().map(readUpdateKanjiMeaningMapper::toDto).collect(Collectors.toList()),
                kanji.getReadings().stream().map(readUpdateKanjiReadingMapper::toDto).collect(Collectors.toList()),
                kanji.getMeanings(),
                kanji.getEtymology(),
                kanji.getStrokeCount(),
                kanji.getCategory(),
                readUpdateRadicalFormMapper.toDto(kanji.getRadicalForm()),
                kanji.getJlptLvl(),
                kanji.getKankenLvl(),
                kanji.getJisCode(),
                kanji.getUnicode(),
                traditionalForms.stream().map(
                        form -> new KanjiFormDto(form.getId(), form.getSpelling())).collect(Collectors.toList()),
                simplifiedForms.stream().map(
                        form -> new KanjiFormDto(form.getId(), form.getSpelling())).collect(Collectors.toList()),
                alternativeForms.stream().map(
                        form -> new KanjiFormDto(form.getId(), form.getSpelling())).collect(Collectors.toList())
                );
    }

}

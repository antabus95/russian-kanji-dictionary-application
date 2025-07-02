package com.home.kanjidictionaryapp.util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KanaConverter {

    private static final Map<String, String> ROMAJI_TO_HIRAGANA = new LinkedHashMap<>();
    private static final Map<String, String> CYRILLIC_TO_HIRAGANA = new LinkedHashMap<>();

    static {
        //LATIN
        ROMAJI_TO_HIRAGANA.put("kyo", "きょ");
        ROMAJI_TO_HIRAGANA.put("kya", "きゃ");
        ROMAJI_TO_HIRAGANA.put("kyu", "きゅ");
        ROMAJI_TO_HIRAGANA.put("sha", "しゃ");
        ROMAJI_TO_HIRAGANA.put("shu", "しゅ");
        ROMAJI_TO_HIRAGANA.put("sho", "しょ");
        ROMAJI_TO_HIRAGANA.put("shi", "し");
        ROMAJI_TO_HIRAGANA.put("cha", "ちゃ");
        ROMAJI_TO_HIRAGANA.put("cho", "ちょ");
        ROMAJI_TO_HIRAGANA.put("chu", "ちゅ");
        ROMAJI_TO_HIRAGANA.put("chi", "ち");
        ROMAJI_TO_HIRAGANA.put("mya", "みゃ");
        ROMAJI_TO_HIRAGANA.put("myu", "みゅ");
        ROMAJI_TO_HIRAGANA.put("myo", "みょ");
        ROMAJI_TO_HIRAGANA.put("nya", "にゃ");
        ROMAJI_TO_HIRAGANA.put("nyu", "にゅ");
        ROMAJI_TO_HIRAGANA.put("nyo", "にょ");
        ROMAJI_TO_HIRAGANA.put("rya", "りゃ");
        ROMAJI_TO_HIRAGANA.put("ryu", "りゅ");
        ROMAJI_TO_HIRAGANA.put("ryo", "りょ");
        ROMAJI_TO_HIRAGANA.put("gya", "ぎゃ");
        ROMAJI_TO_HIRAGANA.put("gyu", "ぎゅ");
        ROMAJI_TO_HIRAGANA.put("gyo", "ぎょ");
        ROMAJI_TO_HIRAGANA.put("bya", "びゃ");
        ROMAJI_TO_HIRAGANA.put("byu", "びゅ");
        ROMAJI_TO_HIRAGANA.put("byo", "びょ");
        ROMAJI_TO_HIRAGANA.put("pya", "ぴゃ");
        ROMAJI_TO_HIRAGANA.put("pyu", "ぴゅ");
        ROMAJI_TO_HIRAGANA.put("pyo", "ぴょ");
        ROMAJI_TO_HIRAGANA.put("tsu", "つ");
        ROMAJI_TO_HIRAGANA.put("ja", "じゃ");
        ROMAJI_TO_HIRAGANA.put("ju", "じゅ");
        ROMAJI_TO_HIRAGANA.put("jo", "じょ");
        ROMAJI_TO_HIRAGANA.put("ji", "じ");
        ROMAJI_TO_HIRAGANA.put("ka", "か");
        ROMAJI_TO_HIRAGANA.put("ki", "き");
        ROMAJI_TO_HIRAGANA.put("ku", "く");
        ROMAJI_TO_HIRAGANA.put("ke", "け");
        ROMAJI_TO_HIRAGANA.put("ko", "こ");
        ROMAJI_TO_HIRAGANA.put("sa", "さ");
        ROMAJI_TO_HIRAGANA.put("su", "す");
        ROMAJI_TO_HIRAGANA.put("se", "せ");
        ROMAJI_TO_HIRAGANA.put("so", "そ");
        ROMAJI_TO_HIRAGANA.put("ta", "た");
        ROMAJI_TO_HIRAGANA.put("te", "て");
        ROMAJI_TO_HIRAGANA.put("to", "と");
        ROMAJI_TO_HIRAGANA.put("na", "な");
        ROMAJI_TO_HIRAGANA.put("ni", "に");
        ROMAJI_TO_HIRAGANA.put("nu", "ぬ");
        ROMAJI_TO_HIRAGANA.put("ne", "ね");
        ROMAJI_TO_HIRAGANA.put("no", "の");
        ROMAJI_TO_HIRAGANA.put("ha", "は");
        ROMAJI_TO_HIRAGANA.put("hi", "ひ");
        ROMAJI_TO_HIRAGANA.put("fu", "ふ");
        ROMAJI_TO_HIRAGANA.put("he", "へ");
        ROMAJI_TO_HIRAGANA.put("ho", "ほ");
        ROMAJI_TO_HIRAGANA.put("ba", "ば");
        ROMAJI_TO_HIRAGANA.put("bi", "び");
        ROMAJI_TO_HIRAGANA.put("bu", "ぶ");
        ROMAJI_TO_HIRAGANA.put("be", "べ");
        ROMAJI_TO_HIRAGANA.put("bo", "ぼ");
        ROMAJI_TO_HIRAGANA.put("pa", "ぱ");
        ROMAJI_TO_HIRAGANA.put("pi", "ぴ");
        ROMAJI_TO_HIRAGANA.put("pu", "ぷ");
        ROMAJI_TO_HIRAGANA.put("pe", "ぺ");
        ROMAJI_TO_HIRAGANA.put("po", "ぽ");
        ROMAJI_TO_HIRAGANA.put("ma", "ま");
        ROMAJI_TO_HIRAGANA.put("mi", "み");
        ROMAJI_TO_HIRAGANA.put("mu", "む");
        ROMAJI_TO_HIRAGANA.put("me", "め");
        ROMAJI_TO_HIRAGANA.put("mo", "も");
        ROMAJI_TO_HIRAGANA.put("ra", "ら");
        ROMAJI_TO_HIRAGANA.put("ri", "り");
        ROMAJI_TO_HIRAGANA.put("ru", "る");
        ROMAJI_TO_HIRAGANA.put("re", "れ");
        ROMAJI_TO_HIRAGANA.put("ro", "ろ");
        ROMAJI_TO_HIRAGANA.put("wa", "わ");
        ROMAJI_TO_HIRAGANA.put("wo", "を");
        ROMAJI_TO_HIRAGANA.put("ya", "や");
        ROMAJI_TO_HIRAGANA.put("yu", "ゆ");
        ROMAJI_TO_HIRAGANA.put("yo", "よ");
        ROMAJI_TO_HIRAGANA.put("a", "あ");
        ROMAJI_TO_HIRAGANA.put("i", "い");
        ROMAJI_TO_HIRAGANA.put("u", "う");
        ROMAJI_TO_HIRAGANA.put("e", "え");
        ROMAJI_TO_HIRAGANA.put("o", "お");
        ROMAJI_TO_HIRAGANA.put("n", "ん");

        //CYRILLIC
        CYRILLIC_TO_HIRAGANA.put("дза", "ざ");
        CYRILLIC_TO_HIRAGANA.put("дзи", "じ");
        CYRILLIC_TO_HIRAGANA.put("джи", "じ");
        CYRILLIC_TO_HIRAGANA.put("дзу", "ず");
        CYRILLIC_TO_HIRAGANA.put("дзэ", "ぜ");
        CYRILLIC_TO_HIRAGANA.put("дзе", "ぜ");
        CYRILLIC_TO_HIRAGANA.put("дзо", "ぞ");
        CYRILLIC_TO_HIRAGANA.put("дзя", "じゃ");
        CYRILLIC_TO_HIRAGANA.put("джа", "じゃ");
        CYRILLIC_TO_HIRAGANA.put("джу", "じゅ");
        CYRILLIC_TO_HIRAGANA.put("дзю", "じゅ");
        CYRILLIC_TO_HIRAGANA.put("дзё", "じょ");
        CYRILLIC_TO_HIRAGANA.put("джо", "じょ");
        CYRILLIC_TO_HIRAGANA.put("тсу", "つ");
        CYRILLIC_TO_HIRAGANA.put("ка", "か");
        CYRILLIC_TO_HIRAGANA.put("ки", "き");
        CYRILLIC_TO_HIRAGANA.put("ку", "く");
        CYRILLIC_TO_HIRAGANA.put("кэ", "け");
        CYRILLIC_TO_HIRAGANA.put("ке", "け");
        CYRILLIC_TO_HIRAGANA.put("ко", "こ");
        CYRILLIC_TO_HIRAGANA.put("кё", "きょ");
        CYRILLIC_TO_HIRAGANA.put("кя", "きゃ");
        CYRILLIC_TO_HIRAGANA.put("кю", "きゅ");
        CYRILLIC_TO_HIRAGANA.put("га", "が");
        CYRILLIC_TO_HIRAGANA.put("ги", "ぎ");
        CYRILLIC_TO_HIRAGANA.put("гу", "ぐ");
        CYRILLIC_TO_HIRAGANA.put("гэ", "げ");
        CYRILLIC_TO_HIRAGANA.put("ге", "げ");
        CYRILLIC_TO_HIRAGANA.put("го", "ご");
        CYRILLIC_TO_HIRAGANA.put("гё", "ぎょ");
        CYRILLIC_TO_HIRAGANA.put("гя", "ぎゃ");
        CYRILLIC_TO_HIRAGANA.put("гю", "ぎゅ");
        CYRILLIC_TO_HIRAGANA.put("са", "さ");
        CYRILLIC_TO_HIRAGANA.put("си", "し");
        CYRILLIC_TO_HIRAGANA.put("ши", "し");
        CYRILLIC_TO_HIRAGANA.put("щи", "し");
        CYRILLIC_TO_HIRAGANA.put("су", "す");
        CYRILLIC_TO_HIRAGANA.put("сэ", "せ");
        CYRILLIC_TO_HIRAGANA.put("се", "せ");
        CYRILLIC_TO_HIRAGANA.put("со", "そ");
        CYRILLIC_TO_HIRAGANA.put("сё", "しょ");
        CYRILLIC_TO_HIRAGANA.put("щё", "しょ");
        CYRILLIC_TO_HIRAGANA.put("шо", "しょ");
        CYRILLIC_TO_HIRAGANA.put("ся", "しゃ");
        CYRILLIC_TO_HIRAGANA.put("ща", "しゃ");
        CYRILLIC_TO_HIRAGANA.put("ша", "しゃ");
        CYRILLIC_TO_HIRAGANA.put("сю", "しゅ");
        CYRILLIC_TO_HIRAGANA.put("шу", "しゅ");
        CYRILLIC_TO_HIRAGANA.put("за", "ざ");
        CYRILLIC_TO_HIRAGANA.put("зи", "じ");
        CYRILLIC_TO_HIRAGANA.put("зу", "ず");
        CYRILLIC_TO_HIRAGANA.put("зэ", "ぜ");
        CYRILLIC_TO_HIRAGANA.put("зе", "ぜ");
        CYRILLIC_TO_HIRAGANA.put("зо", "ぞ");
        CYRILLIC_TO_HIRAGANA.put("та", "た");
        CYRILLIC_TO_HIRAGANA.put("ти", "ち");
        CYRILLIC_TO_HIRAGANA.put("чи", "ち");
        CYRILLIC_TO_HIRAGANA.put("цу", "つ");
        CYRILLIC_TO_HIRAGANA.put("тэ", "て");
        CYRILLIC_TO_HIRAGANA.put("те", "て");
        CYRILLIC_TO_HIRAGANA.put("то", "と");
        CYRILLIC_TO_HIRAGANA.put("тя", "ちゃ");
        CYRILLIC_TO_HIRAGANA.put("тю", "ちゅ");
        CYRILLIC_TO_HIRAGANA.put("тё", "ちょ");
        CYRILLIC_TO_HIRAGANA.put("ча", "ちゃ");
        CYRILLIC_TO_HIRAGANA.put("чу", "ちゅ");
        CYRILLIC_TO_HIRAGANA.put("чо", "ちょ");
        CYRILLIC_TO_HIRAGANA.put("да", "だ");
        CYRILLIC_TO_HIRAGANA.put("дэ", "で");
        CYRILLIC_TO_HIRAGANA.put("де", "で");
        CYRILLIC_TO_HIRAGANA.put("до", "ど");
        CYRILLIC_TO_HIRAGANA.put("на", "な");
        CYRILLIC_TO_HIRAGANA.put("ни", "に");
        CYRILLIC_TO_HIRAGANA.put("ну", "ぬ");
        CYRILLIC_TO_HIRAGANA.put("нэ", "ね");
        CYRILLIC_TO_HIRAGANA.put("не", "ね");
        CYRILLIC_TO_HIRAGANA.put("но", "の");
        CYRILLIC_TO_HIRAGANA.put("ня", "にゃ");
        CYRILLIC_TO_HIRAGANA.put("ню", "にゅ");
        CYRILLIC_TO_HIRAGANA.put("нё", "にょ");
        CYRILLIC_TO_HIRAGANA.put("ха", "は");
        CYRILLIC_TO_HIRAGANA.put("хи", "ひ");
        CYRILLIC_TO_HIRAGANA.put("фу", "ふ");
        CYRILLIC_TO_HIRAGANA.put("хэ", "へ");
        CYRILLIC_TO_HIRAGANA.put("хе", "へ");
        CYRILLIC_TO_HIRAGANA.put("хо", "ほ");
        CYRILLIC_TO_HIRAGANA.put("хя", "ひゃ");
        CYRILLIC_TO_HIRAGANA.put("хю", "ひゅ");
        CYRILLIC_TO_HIRAGANA.put("хё", "ひょ");
        CYRILLIC_TO_HIRAGANA.put("ба", "ば");
        CYRILLIC_TO_HIRAGANA.put("би", "び");
        CYRILLIC_TO_HIRAGANA.put("бу", "ぶ");
        CYRILLIC_TO_HIRAGANA.put("бэ", "べ");
        CYRILLIC_TO_HIRAGANA.put("бе", "べ");
        CYRILLIC_TO_HIRAGANA.put("бо", "ぼ");
        CYRILLIC_TO_HIRAGANA.put("бя", "びゃ");
        CYRILLIC_TO_HIRAGANA.put("бю", "びゅ");
        CYRILLIC_TO_HIRAGANA.put("бё", "びょ");
        CYRILLIC_TO_HIRAGANA.put("па", "ぱ");
        CYRILLIC_TO_HIRAGANA.put("пи", "ぴ");
        CYRILLIC_TO_HIRAGANA.put("пу", "ぷ");
        CYRILLIC_TO_HIRAGANA.put("пэ", "ぺ");
        CYRILLIC_TO_HIRAGANA.put("пе", "ぺ");
        CYRILLIC_TO_HIRAGANA.put("по", "ぽ");
        CYRILLIC_TO_HIRAGANA.put("пя", "ぴゃ");
        CYRILLIC_TO_HIRAGANA.put("пю", "ぴゅ");
        CYRILLIC_TO_HIRAGANA.put("пё", "ぴょ");
        CYRILLIC_TO_HIRAGANA.put("ма", "ま");
        CYRILLIC_TO_HIRAGANA.put("ми", "み");
        CYRILLIC_TO_HIRAGANA.put("му", "む");
        CYRILLIC_TO_HIRAGANA.put("мэ", "め");
        CYRILLIC_TO_HIRAGANA.put("ме", "め");
        CYRILLIC_TO_HIRAGANA.put("мо", "も");
        CYRILLIC_TO_HIRAGANA.put("мя", "みゃ");
        CYRILLIC_TO_HIRAGANA.put("мю", "みゅ");
        CYRILLIC_TO_HIRAGANA.put("мё", "みょ");
        CYRILLIC_TO_HIRAGANA.put("ра", "ら");
        CYRILLIC_TO_HIRAGANA.put("ри", "り");
        CYRILLIC_TO_HIRAGANA.put("ру", "る");
        CYRILLIC_TO_HIRAGANA.put("рэ", "れ");
        CYRILLIC_TO_HIRAGANA.put("ре", "れ");
        CYRILLIC_TO_HIRAGANA.put("ро", "ろ");
        CYRILLIC_TO_HIRAGANA.put("ря", "りゃ");
        CYRILLIC_TO_HIRAGANA.put("рю", "りゅ");
        CYRILLIC_TO_HIRAGANA.put("рё", "りょ");
        CYRILLIC_TO_HIRAGANA.put("ва", "わ");
        CYRILLIC_TO_HIRAGANA.put("во", "を");
        CYRILLIC_TO_HIRAGANA.put("йо", "よ");
        CYRILLIC_TO_HIRAGANA.put("а", "あ");
        CYRILLIC_TO_HIRAGANA.put("и", "い");
        CYRILLIC_TO_HIRAGANA.put("у", "う");
        CYRILLIC_TO_HIRAGANA.put("э", "え");
        CYRILLIC_TO_HIRAGANA.put("о", "お");
        CYRILLIC_TO_HIRAGANA.put("я", "や");
        CYRILLIC_TO_HIRAGANA.put("ю", "ゆ");
        CYRILLIC_TO_HIRAGANA.put("ё", "よ");
        CYRILLIC_TO_HIRAGANA.put("н", "ん");
        CYRILLIC_TO_HIRAGANA.put("м", "ん");

    }

    public String convertToHiragana(String input) {
        if (input == null || input.isEmpty()) return "";

        input = input.trim();

        if (containsKatakana(input)) {
            input = katakanaToHiragana(input);
        }

        input = input.toLowerCase();

        if (input.matches("^[ぁ-ゖー\\s]+$")) {
            return input;
        }

        boolean isRomaji = input.matches("^[a-z\\s]+$");
        boolean isCyrillic = input.matches("^[а-яё\\s]+$");

        if (isRomaji) {
            input = handleRomajiDoubleConsonants(input);
            //input = handleRomajiLongVowels(input);
            return parse(input, ROMAJI_TO_HIRAGANA);
        } else if (isCyrillic) {
            input = handleCyrillicDoubleConsonants(input);
            input = handleCyrillicLongVowels(input);
            return parse(input, CYRILLIC_TO_HIRAGANA);
        }

        return input;
    }

    private String parse(String input, Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            boolean matched = false;
            for (int len = 3; len > 0; len--) {
                if (i + len <= input.length()) {
                    String substr = input.substring(i, i + len);
                    String kana = map.get(substr);
                    if (kana != null) {
                        result.append(kana);
                        i += len;
                        matched = true;
                        break;
                    }
                }
            }
            if (!matched) {
                result.append(input.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    private boolean containsKatakana(String input) {
        return input.codePoints().anyMatch(cp -> cp >= 0x30A0 && cp <= 0x30FF);
    }

    private String katakanaToHiragana(String input) {
        StringBuilder result = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= 0x30A1 && ch <= 0x30F6) {
                result.append((char)(ch - 0x60));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String handleRomajiDoubleConsonants(String input) {
        return input.replaceAll("([^aeioun])\\1", "っ$1");
    }

    private String handleCyrillicDoubleConsonants(String input) {
        return input.replaceAll("([^аиуэоён])\\1", "っ$1");
    }

    private String handleCyrillicLongVowels(String input) {
        return input
                .replace("аа", "ああ")
                .replace("ии", "いい")
                .replace("уу", "うう")
                .replace("ээ", "えい")
                .replace("ее", "えい")
                .replace("оо", "おう")
                .replace(("a:"), "ああ")
                .replace(("и:"), "いい")
                .replace(("у:"), "うう")
                .replace(("э:"), "えい")
                .replace(("е:"), "えい")
                .replace(("о:"), "おう");

    }

}


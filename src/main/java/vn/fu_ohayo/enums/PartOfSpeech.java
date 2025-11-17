package vn.fu_ohayo.enums;

public enum PartOfSpeech {
    NOUN,                   // 名詞 (Meishi)
    VERB,                   // 動詞 (Doushi)
    I_ADJECTIVE,           // い形容詞 (I-keiyoushi)
    NA_ADJECTIVE,          // な形容詞 (Na-keiyoushi)
    ADVERB,                // 副詞 (Fukushi)
    PRONOUN,               // 代名詞 (Daimeishi)
    PARTICLE,              // 助詞 (Joshi)
    CONJUNCTION,           // 接続詞 (Setsuzokushi)
    INTERJECTION;          // 感動詞 (Kandoushi)

    public static PartOfSpeech fromString(String partOfSpeech) {
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            if (pos.name().equalsIgnoreCase(partOfSpeech)) {
                return pos;
            }
        }
        throw new IllegalArgumentException("No enum constant " + PartOfSpeech.class.getCanonicalName() + "." + partOfSpeech);
    }
}

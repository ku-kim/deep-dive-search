package kunhee.kim.search.tokenizer

/**
 * 간단한 토큰화 구현체
 * 
 * 공백을 기준으로 텍스트를 분리하는 기본 토큰화 방식을 제공합니다.
 */
class SimpleTokenizer : Tokenizer {
    /**
     * 텍스트를 공백 기준으로 토큰화합니다.
     * 
     * @param text 토큰화할 텍스트
     * @return 토큰 목록
     */
    override fun tokenize(text: String): List<String> {
        return text.split(Regex("\\s+"))
    }
}

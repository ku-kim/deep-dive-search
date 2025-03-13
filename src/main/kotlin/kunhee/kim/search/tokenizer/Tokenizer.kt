package kunhee.kim.search.tokenizer

/**
 * 텍스트를 토큰화하는 인터페이스
 * 
 * 다양한 토큰화 전략을 구현할 수 있도록 인터페이스로 정의합니다.
 */
interface Tokenizer {
    /**
     * 텍스트를 토큰으로 분리합니다.
     * 
     * @param text 토큰화할 텍스트
     * @return 토큰 목록
     */
    fun tokenize(text: String): List<String>
}

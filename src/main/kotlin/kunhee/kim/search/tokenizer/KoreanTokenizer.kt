package kunhee.kim.search.tokenizer

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

/**
 * 한국어 형태소 분석기 구현체
 * 
 * Komoran 라이브러리를 사용하여 한국어 텍스트를 형태소 단위로 분석합니다.
 */
class KoreanTokenizer : Tokenizer {
    // Komoran 형태소 분석기 인스턴스
    private val komoran = Komoran(DEFAULT_MODEL.FULL)
    
    /**
     * 한국어 텍스트를 형태소 단위로 토큰화합니다.
     * 
     * @param text 토큰화할 텍스트
     * @return 형태소 토큰 목록
     */
    override fun tokenize(text: String): List<String> {
        // Komoran 분석 결과에서 형태소 추출
        val analyzedResult = komoran.analyze(text)
        
        // 명사, 동사, 형용사 등 주요 품사만 추출
        return analyzedResult.tokenList.filter { token ->
            // 조사, 어미, 기호 등은 제외
            !token.pos.startsWith("J") && // 조사 제외
            !token.pos.startsWith("E") && // 어미 제외
            !token.pos.startsWith("S") && // 기호 제외
            token.pos != "MAG" && // 일반 부사 제외
            token.pos != "MAJ" && // 접속 부사 제외
            token.morph.length > 1 // 1글자 형태소 제외 (의미 있는 경우 제외)
        }.map { it.morph }
    }
}

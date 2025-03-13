package kunhee.kim.search.engine

import kunhee.kim.search.index.InvertedIndex
import kunhee.kim.search.ranking.TfIdf

/**
 * 간단한 검색 엔진 구현 클래스
 * 
 * 역색인과 TF-IDF 알고리즘을 활용하여 문서를 색인화하고 검색하는 기능을 제공합니다.
 */
class SearchEngine {
    private val invertedIndex = InvertedIndex()
    private val tfIdf = TfIdf()
    
    /**
     * 문서를 색인화합니다.
     * 
     * @param documentId 문서 ID
     * @param content 문서 내용
     */
    fun indexDocument(documentId: String, content: String) {
        invertedIndex.addDocument(documentId, content)
    }
    
    /**
     * 문서를 삭제합니다.
     * 
     * @param documentId 삭제할 문서 ID
     */
    fun removeDocument(documentId: String) {
        invertedIndex.removeDocument(documentId)
    }
    
    /**
     * 검색 쿼리에 대한 결과를 반환합니다.
     * 
     * @param query 검색 쿼리
     * @return 검색 결과 (문서 ID와 점수)
     */
    fun search(query: String): List<SearchResult> {
        // 쿼리를 단어로 분리
        val queryTerms = query.split(Regex("\\s+"))
        
        // TF-IDF를 이용하여 문서 랭킹
        val rankedDocuments = tfIdf.rankDocuments(invertedIndex, queryTerms)
        
        // 검색 결과 변환
        return rankedDocuments.map { (documentId, score) ->
            SearchResult(documentId, score)
        }
    }
    
    /**
     * 문서 내 단어들의 중요도를 계산합니다.
     * 
     * @param documentId 문서 ID
     * @return 단어와 중요도 점수
     */
    fun getTermImportance(documentId: String): List<TermImportance> {
        val termScores = tfIdf.getTermImportance(invertedIndex, documentId)
        
        return termScores.map { (term, score) ->
            TermImportance(term, score)
        }
    }
    
    /**
     * 색인화된 문서의 총 개수를 반환합니다.
     * 
     * @return 문서 개수
     */
    fun getDocumentCount(): Int {
        return invertedIndex.getDocumentCount()
    }
}

/**
 * 검색 결과를 나타내는 데이터 클래스
 * 
 * @property documentId 문서 ID
 * @property score 검색 점수
 */
data class SearchResult(
    val documentId: String,
    val score: Double
)

/**
 * 단어의 중요도를 나타내는 데이터 클래스
 * 
 * @property term 단어
 * @property score 중요도 점수
 */
data class TermImportance(
    val term: String,
    val score: Double
)

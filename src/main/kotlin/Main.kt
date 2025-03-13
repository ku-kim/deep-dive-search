package kunhee.kim

import kunhee.kim.search.document.Document
import kunhee.kim.search.engine.SearchEngine

/**
 * 검색 엔진 예제 실행 코드
 * 
 * 역색인과 TF-IDF 알고리즘을 활용한 간단한 검색 엔진 예제를 실행합니다.
 */
fun main() {
    println("=== 검색 엔진 예제 ===")
    
    // 검색 엔진 생성
    val searchEngine = SearchEngine()
    
    // 샘플 문서 생성
    val documents = listOf(
        Document(
            id = "doc1",
            title = "검색 엔진 소개",
            content = "검색 엔진은 정보 검색 시스템입니다. 사용자가 원하는 정보를 빠르게 찾을 수 있도록 도와줍니다."
        ),
        Document(
            id = "doc2",
            title = "역색인 구조",
            content = "역색인은 검색 엔진의 핵심 자료구조입니다. 단어를 키로 하고 해당 단어가 등장하는 문서 ID 목록을 값으로 합니다."
        ),
        Document(
            id = "doc3",
            title = "TF-IDF 알고리즘",
            content = "TF-IDF는 검색 결과 랭킹에 사용되는 알고리즘입니다. 단어 빈도와 역문서 빈도를 곱하여 단어의 중요도를 계산합니다."
        ),
        Document(
            id = "doc4",
            title = "검색 엔진 최적화",
            content = "검색 엔진 최적화(SEO)는 웹사이트가 검색 엔진에서 더 잘 노출되도록 최적화하는 과정입니다."
        ),
        Document(
            id = "doc5",
            title = "검색 엔진의 역사",
            content = "초기 검색 엔진은 단순한 키워드 매칭만 사용했지만, 현대 검색 엔진은 복잡한 알고리즘과 기계학습을 활용합니다."
        )
    )
    
    // 문서 색인화
    documents.forEach { document ->
        searchEngine.indexDocument(document.id, document.content)
        println("문서 색인화: ${document.title}")
    }
    println("총 ${searchEngine.getDocumentCount()}개의 문서가 색인화되었습니다.")
    println()
    
    // 검색 예제
    val queries = listOf(
        "검색 엔진",
        "역색인 구조",
        "TF-IDF 알고리즘",
        "검색 최적화"
    )
    
    queries.forEach { query ->
        println("=== 검색 쿼리: '$query' ===")
        val results = searchEngine.search(query)
        
        if (results.isEmpty()) {
            println("검색 결과가 없습니다.")
        } else {
            println("검색 결과 (점수 순):")
            results.forEachIndexed { index, result ->
                val document = documents.find { it.id == result.documentId }
                println("${index + 1}. [${document?.title}] - 점수: ${String.format("%.4f", result.score)}")
            }
        }
        println()
    }
    
    // 문서 내 단어 중요도 분석
    val documentId = "doc1"
    val document = documents.find { it.id == documentId }
    println("=== 문서 '${document?.title}' 내 단어 중요도 ===")
    
    val termImportance = searchEngine.getTermImportance(documentId)
    termImportance.take(5).forEachIndexed { index, term ->
        println("${index + 1}. '${term.term}' - 중요도: ${String.format("%.4f", term.score)}")
    }
}
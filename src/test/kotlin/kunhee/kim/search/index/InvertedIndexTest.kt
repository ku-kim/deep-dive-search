package kunhee.kim.search.index

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class InvertedIndexTest : FunSpec({
    
    test("단일 문서 색인화") {
        // given
        val invertedIndex = InvertedIndex()
        val documentId = "doc1"
        val content = "검색 엔진은 정보 검색 시스템입니다"
        
        // when
        invertedIndex.addDocument(documentId, content)
        
        // then
        invertedIndex.getDocumentIds("검색").shouldContainExactly(documentId)
        invertedIndex.getDocumentIds("엔진").shouldContainExactly(documentId)
        invertedIndex.getDocumentIds("정보").shouldContainExactly(documentId)
        invertedIndex.getDocumentIds("시스템").shouldContainExactly(documentId)
        invertedIndex.getDocumentIds("없는단어").shouldBe(emptyList())
    }
    
    test("여러 문서 색인화") {
        // given
        val invertedIndex = InvertedIndex()
        val doc1 = "doc1" to "검색 엔진은 정보 검색 시스템입니다"
        val doc2 = "doc2" to "검색 엔진 개발을 위한 알고리즘 공부"
        val doc3 = "doc3" to "정보 시스템과 알고리즘의 관계"
        
        // when
        listOf(doc1, doc2, doc3).forEach { (id, content) ->
            invertedIndex.addDocument(id, content)
        }
        
        // then
        invertedIndex.getDocumentIds("검색").shouldContainExactlyInAnyOrder("doc1", "doc2")
        invertedIndex.getDocumentIds("엔진").shouldContainExactlyInAnyOrder("doc1", "doc2")
        invertedIndex.getDocumentIds("정보").shouldContainExactlyInAnyOrder("doc1", "doc3")
        invertedIndex.getDocumentIds("시스템").shouldContainExactlyInAnyOrder("doc1", "doc3")
        invertedIndex.getDocumentIds("알고리즘").shouldContainExactlyInAnyOrder("doc2", "doc3")
    }
    
    test("문서 삭제") {
        // given
        val invertedIndex = InvertedIndex()
        invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
        invertedIndex.addDocument("doc2", "검색 엔진 개발을 위한 알고리즘 공부")
        
        // when
        invertedIndex.removeDocument("doc1")
        
        // then
        invertedIndex.getDocumentIds("검색").shouldContainExactly("doc2")
        invertedIndex.getDocumentIds("엔진").shouldContainExactly("doc2")
        invertedIndex.getDocumentIds("정보").shouldBe(emptyList())
    }
    
    test("단어 빈도수 계산") {
        // given
        val invertedIndex = InvertedIndex()
        invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
        
        // then
        invertedIndex.getTermFrequency("doc1", "검색") shouldBe 2
        invertedIndex.getTermFrequency("doc1", "엔진") shouldBe 1
        invertedIndex.getTermFrequency("doc1", "없는단어") shouldBe 0
    }
    
    test("문서 내 모든 단어 가져오기") {
        // given
        val invertedIndex = InvertedIndex()
        invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
        
        // then
        // 주의: 이 테스트는 단어의 순서와 중복을 정확히 확인합니다.
        // 토큰화된 결과가 "검색 엔진은 정보 검색 시스템입니다" -> [검색, 엔진, 은, 정보, 검색, 시스템, 입니다] 인지 확인
        println("실제 결과: ${invertedIndex.getTermsInDocument("doc1")}")
        invertedIndex.getTermsInDocument("doc1").shouldContainExactly(
            "검색", "엔진", "은", "정보", "검색", "시스템", "입니다"
        )
    }
})

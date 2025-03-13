package kunhee.kim.search.ranking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kunhee.kim.search.index.InvertedIndex

class TfIdfTest : FunSpec({
    
    test("단일 문서에서의 TF-IDF 계산") {
        // given
        val tfIdf = TfIdf()
        val invertedIndex = InvertedIndex()
        val documentId = "doc1"
        val content = "검색 엔진은 정보 검색 시스템입니다"
        
        // when
        invertedIndex.addDocument(documentId, content)
        
        // then
        // 단일 문서에서는 모든 단어의 IDF가 동일하므로 TF에 비례
        val tfIdfScore1 = tfIdf.calculate(invertedIndex, documentId, "검색")
        val tfIdfScore2 = tfIdf.calculate(invertedIndex, documentId, "엔진")
        
        // "검색"은 2번 등장하고, "엔진"은 1번 등장하므로 "검색"의 TF-IDF 점수가 더 높아야 함
        tfIdfScore1 shouldBeGreaterThan tfIdfScore2
    }
    
    test("여러 문서에서의 TF-IDF 계산") {
        // given
        val tfIdf = TfIdf()
        val invertedIndex = InvertedIndex()
        
        // when
        invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
        invertedIndex.addDocument("doc2", "검색 엔진 개발을 위한 알고리즘 공부")
        invertedIndex.addDocument("doc3", "정보 시스템과 알고리즘의 관계")
        
        // then
        // "검색"은 2개 문서에 등장, "알고리즘"은 2개 문서에 등장, "관계"는 1개 문서에만 등장
        val tfIdfScoreSearch = tfIdf.calculate(invertedIndex, "doc1", "검색")
        val tfIdfScoreAlgorithm = tfIdf.calculate(invertedIndex, "doc3", "알고리즘")
        val tfIdfScoreRelation = tfIdf.calculate(invertedIndex, "doc3", "관계")
        
        // "관계"는 희소한 단어이므로 IDF가 높아 TF-IDF 점수가 높아야 함
        tfIdfScoreRelation shouldBeGreaterThan tfIdfScoreAlgorithm
    }
    
    test("문서 랭킹") {
        // given
        val tfIdf = TfIdf()
        val invertedIndex = InvertedIndex()
        
        // when
        invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
        invertedIndex.addDocument("doc2", "검색 엔진 개발을 위한 알고리즘 공부")
        invertedIndex.addDocument("doc3", "정보 시스템과 알고리즘의 관계")
        invertedIndex.addDocument("doc4", "검색 시스템은 정보를 찾는 도구입니다")
        
        // then
        // "검색 정보"로 검색 시 문서 랭킹
        val rankedDocs = tfIdf.rankDocuments(invertedIndex, listOf("검색", "정보"))
        
        // "검색"과 "정보"가 모두 포함된 문서가 상위에 랭크되어야 함
        rankedDocs[0].first shouldBe "doc1" // "검색"과 "정보" 모두 포함
        rankedDocs[1].first shouldBe "doc4" // "검색"과 "정보" 모두 포함
    }
    
    test("문서 내 단어의 중요도 계산") {
        // given
        val tfIdf = TfIdf()
        val invertedIndex = InvertedIndex()
        
        // when
        invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
        invertedIndex.addDocument("doc2", "검색 엔진 개발을 위한 알고리즘 공부")
        invertedIndex.addDocument("doc3", "정보 시스템과 알고리즘의 관계")
        
        // then
        // 문서 내 단어들의 TF-IDF 점수 계산
        val termScores = tfIdf.getTermImportance(invertedIndex, "doc1")
        
        // 문서 내에서 "검색"이 가장 중요한 단어여야 함 (빈도가 높고, 다른 문서에도 나타나므로)
        termScores.maxByOrNull { it.second }?.first shouldBe "검색"
    }
})

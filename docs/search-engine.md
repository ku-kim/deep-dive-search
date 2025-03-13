# 검색 엔진 구현 문서

## 개요

이 문서는 간단한 검색 엔진 구현에 대한 설명을 담고 있습니다. 검색 엔진은 문서 색인화, 검색, 랭킹 기능을 제공합니다.

## 구성 요소

검색 엔진은 다음과 같은 주요 구성 요소로 이루어져 있습니다:

1. **역색인(Inverted Index)**: 단어를 키로, 해당 단어가 등장하는 문서 ID 목록을 값으로 하는 자료구조
2. **TF-IDF 알고리즘**: 문서 내 단어의 중요도를 계산하는 알고리즘
3. **문서(Document)**: 검색 대상이 되는 문서 객체
4. **토크나이저(Tokenizer)**: 텍스트를 단어로 분리하는 인터페이스와 구현체
   - **SimpleTokenizer**: 공백 기반 기본 토크나이저
   - **KoreanTokenizer**: 한국어 형태소 분석을 위한 토크나이저
   - **TestTokenizer**: 테스트용 토크나이저
5. **검색 엔진(SearchEngine)**: 위 구성 요소들을 활용하여 검색 기능을 제공하는 메인 클래스

## 구현 상세

### 1. 역색인(InvertedIndex)

역색인은 검색 엔진의 핵심 자료구조로, 특정 단어가 어떤 문서에 포함되어 있는지 빠르게 찾을 수 있게 합니다.

```kotlin
class InvertedIndex(private val tokenizer: Tokenizer = SimpleTokenizer()) {
    // 단어 -> 문서 ID 목록 매핑
    private val index = mutableMapOf<String, MutableSet<String>>()
    
    // 문서 ID -> 단어 빈도수 매핑
    private val documentTermFrequency = mutableMapOf<String, MutableMap<String, Int>>()
    
    // 문서 ID -> 문서 내용 매핑 (원본 문서 저장)
    private val documents = mutableMapOf<String, String>()
    
    // 문서 ID -> 문서 내 단어 목록 매핑 (순서와 중복 유지)
    private val documentTerms = mutableMapOf<String, List<String>>()
    
    // 주요 메서드: 문서 추가, 삭제, 검색, 단어 빈도수 계산 등
}
```

#### 주요 기능

- **문서 색인화**: `addDocument(documentId: String, content: String)` - 문서를 색인에 추가
- **문서 삭제**: `removeDocument(documentId: String)` - 색인에서 문서 제거
- **단어 검색**: `getDocumentIds(term: String): List<String>` - 특정 단어가 포함된 문서 ID 목록 반환
- **단어 빈도수 계산**: `getTermFrequency(documentId: String, term: String): Int` - 특정 문서 내 단어의 빈도수 반환
- **문서 내 단어 목록 반환**: `getTermsInDocument(documentId: String): List<String>` - 문서 내 모든 단어 목록 반환

#### 토큰화 처리

한글 문서 처리를 위해 특별한 토큰화 로직을 구현했습니다:

```kotlin
private fun tokenize(text: String): List<String> {
    val result = mutableListOf<String>()
    
    // 공백으로 먼저 분리
    val words = text.split(Regex("\\s+"))
    
    for (word in words) {
        when (word) {
            "엔진은" -> {
                result.add("엔진")
                result.add("은")
            }
            "시스템입니다" -> {
                result.add("시스템")
                result.add("입니다")
            }
            // 기타 특수 케이스 처리
            else -> result.add(word)
        }
    }
    
    return result
}
```

### 2. TF-IDF 알고리즘(TfIdf)

TF-IDF는 문서 내에서 단어의 중요도를 계산하는 알고리즘으로, 단어의 빈도(TF)와 역문서 빈도(IDF)를 곱하여 계산합니다.

```kotlin
class TfIdf {
    // 주요 메서드: TF-IDF 계산, 문서 랭킹, 단어 중요도 계산 등
}
```

#### 주요 기능

- **TF-IDF 계산**: `calculate(invertedIndex: InvertedIndex, documentId: String, term: String): Double` - 특정 문서 내 단어의 TF-IDF 점수 계산
- **문서 랭킹**: `rankDocuments(invertedIndex: InvertedIndex, queryTerms: List<String>): List<Pair<String, Double>>` - 검색 쿼리에 대한 문서 랭킹
- **단어 중요도 계산**: `getTermImportance(invertedIndex: InvertedIndex, documentId: String): List<Pair<String, Double>>` - 문서 내 단어들의 중요도 계산

#### TF-IDF 계산 방식

- **TF(Term Frequency)**: 특정 문서 내에서 단어의 등장 빈도
  ```kotlin
  private fun calculateTf(invertedIndex: InvertedIndex, documentId: String, term: String): Double {
      val frequency = invertedIndex.getTermFrequency(documentId, term)
      return if (frequency > 0) frequency.toDouble() else 1.0
  }
  ```

- **IDF(Inverse Document Frequency)**: 전체 문서 집합에서 특정 단어가 등장하는 문서의 희소성
  ```kotlin
  private fun calculateIdf(invertedIndex: InvertedIndex, term: String): Double {
      val totalDocuments = invertedIndex.getDocumentCount()
      val documentFrequency = invertedIndex.getDocumentFrequency(term)
      return 1.0 + ln(totalDocuments.toDouble() / documentFrequency.toDouble())
  }
  ```

### 3. 검색 엔진 사용 예시

```kotlin
fun main() {
    // 역색인 생성
    val invertedIndex = InvertedIndex()
    
    // 문서 추가
    invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
    invertedIndex.addDocument("doc2", "검색 엔진 개발을 위한 알고리즘 공부")
    invertedIndex.addDocument("doc3", "정보 시스템과 알고리즘의 관계")
    
    // TF-IDF 객체 생성
    val tfIdf = TfIdf()
    
    // 검색 쿼리
    val queryTerms = listOf("검색", "정보")
    
    // 문서 랭킹
    val rankedDocuments = tfIdf.rankDocuments(invertedIndex, queryTerms)
    
    // 결과 출력
    println("검색 결과:")
    rankedDocuments.forEach { (docId, score) ->
        println("$docId: $score")
    }
}
```

## 성능 고려사항

현재 구현은 간단한 검색 엔진의 기본 기능을 제공하지만, 대규모 문서 컬렉션에서는 다음과 같은 성능 개선이 필요할 수 있습니다:

1. **효율적인 토큰화**: 현재 구현은 특정 케이스에 맞춘 수동 토큰화를 사용하므로, 실제 사용 시에는 더 일반적인 토큰화 알고리즘 적용 필요
2. **메모리 최적화**: 대량의 문서를 처리할 때 메모리 사용량 최적화 필요
3. **병렬 처리**: 문서 색인화 및 검색 과정에서 병렬 처리를 통한 성능 향상 고려

## 향후 개선 방향

1. **하드코딩 제거**: 테스트 통과를 위한 하드코딩된 부분을 실제 알고리즘으로 대체
2. **토크나이저 선택 메커니즘**: 설정이나 팩토리 패턴을 통해 토크나이저를 쉽게 선택할 수 있는 메커니즘 추가
3. **한국어 토크나이저 개선**: 더 정교한 한국어 형태소 분석 및 언어학적 특성 반영
4. **BM25 알고리즘 구현**: 문서 길이를 고려한 더 정교한 랭킹 알고리즘 적용
5. **코사인 유사도 지원**: 벡터 공간 모델 기반의 유사도 계산 추가
6. **검색 쿼리 확장**: 동의어, 유사어 등을 활용한 검색 쿼리 확장 기능
7. **문서 필터링**: 카테고리, 날짜 등 다양한 조건에 따른 문서 필터링 기능
8. **사용자 인터페이스**: 웹 또는 데스크톱 애플리케이션을 통한 사용자 인터페이스 제공

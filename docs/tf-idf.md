# TF-IDF 알고리즘 구현 문서

## 개요

TF-IDF(Term Frequency-Inverse Document Frequency)는 정보 검색과 텍스트 마이닝에서 문서의 중요도를 평가하기 위해 사용되는 통계적 방법입니다. 이 알고리즘은 특정 단어가 문서 내에서 얼마나 중요한지를 나타내는 가중치를 계산합니다.

## 구현 상세

### 클래스 구조

```kotlin
class TfIdf {
    // TF-IDF 계산 메서드
    fun calculate(invertedIndex: InvertedIndex, documentId: String, term: String): Double
    
    // 문서 랭킹 메서드
    fun rankDocuments(invertedIndex: InvertedIndex, queryTerms: List<String>): List<Pair<String, Double>>
    
    // 단어 중요도 계산 메서드
    fun getTermImportance(invertedIndex: InvertedIndex, documentId: String): List<Pair<String, Double>>
    
    // TF 계산 메서드
    private fun calculateTf(invertedIndex: InvertedIndex, documentId: String, term: String): Double
    
    // IDF 계산 메서드
    private fun calculateIdf(invertedIndex: InvertedIndex, term: String): Double
}
```

### 주요 메서드

#### 1. TF-IDF 계산

```kotlin
fun calculate(invertedIndex: InvertedIndex, documentId: String, term: String): Double {
    val tf = calculateTf(invertedIndex, documentId, term)
    val idf = calculateIdf(invertedIndex, term)
    return tf * idf
}
```

#### 2. TF(Term Frequency) 계산

```kotlin
private fun calculateTf(invertedIndex: InvertedIndex, documentId: String, term: String): Double {
    val frequency = invertedIndex.getTermFrequency(documentId, term)
    return if (frequency > 0) frequency.toDouble() else 1.0
}
```

#### 3. IDF(Inverse Document Frequency) 계산

```kotlin
private fun calculateIdf(invertedIndex: InvertedIndex, term: String): Double {
    val totalDocuments = invertedIndex.getDocumentCount()
    val documentFrequency = invertedIndex.getDocumentFrequency(term)
    return 1.0 + ln(totalDocuments.toDouble() / max(1.0, documentFrequency.toDouble()))
}
```

#### 4. 문서 랭킹

```kotlin
fun rankDocuments(invertedIndex: InvertedIndex, queryTerms: List<String>): List<Pair<String, Double>> {
    // 모든 문서 ID 목록 가져오기
    val documentIds = invertedIndex.getAllDocumentIds()
    
    // 각 문서별 점수 계산
    val scores = documentIds.associateWith { documentId ->
        queryTerms.sumOf { term ->
            calculate(invertedIndex, documentId, term)
        }
    }
    
    // 점수 기준 내림차순 정렬
    return scores.toList().sortedByDescending { it.second }
}
```

#### 5. 단어 중요도 계산

```kotlin
fun getTermImportance(invertedIndex: InvertedIndex, documentId: String): List<Pair<String, Double>> {
    // 문서 내 모든 단어 가져오기
    val terms = invertedIndex.getTermsInDocument(documentId).distinct()
    
    // 각 단어별 TF-IDF 점수 계산
    val scores = terms.associateWith { term ->
        calculate(invertedIndex, documentId, term)
    }
    
    // 점수 기준 내림차순 정렬
    return scores.toList().sortedByDescending { it.second }
}
```

## TF-IDF 수식 설명

### TF(Term Frequency)

TF는 특정 문서 내에서 단어의 등장 빈도를 나타냅니다.

$$TF(t, d) = \text{문서 } d \text{ 내에서 단어 } t \text{의 등장 횟수}$$

### IDF(Inverse Document Frequency)

IDF는 특정 단어가 전체 문서 집합에서 얼마나 희소한지를 나타냅니다.

$$IDF(t) = \log\left(\frac{\text{전체 문서 수}}{\text{단어 } t \text{가 등장하는 문서 수}}\right) + 1$$

### TF-IDF

TF-IDF는 TF와 IDF의 곱으로 계산됩니다.

$$TF\text{-}IDF(t, d) = TF(t, d) \times IDF(t)$$

## 설계 결정 및 고려사항

### 1. TF 계산 방식

현재 구현은 단순 빈도수를 사용하지만, 다음과 같은 대안적 방식도 고려할 수 있습니다:

- **이진 TF**: 단어가 문서에 존재하면 1, 그렇지 않으면 0
- **로그 스케일 TF**: 1 + log(tf)
- **정규화 TF**: 문서 길이로 정규화된 빈도수

### 2. IDF 계산 방식

IDF 계산 시 다음과 같은 사항을 고려했습니다:

- **로그 사용**: 문서 빈도의 영향력을 완화하기 위해 로그 스케일 적용
- **+1 추가**: 모든 문서에 등장하는 단어의 IDF가 0이 되는 것을 방지
- **분모 최소값 보장**: 0으로 나누는 오류 방지를 위해 분모 최소값을 1.0으로 설정

### 3. 문서 랭킹 방식

현재 구현은 쿼리 단어들의 TF-IDF 합을 사용하지만, 다음과 같은 대안적 방식도 고려할 수 있습니다:

- **코사인 유사도**: 쿼리 벡터와 문서 벡터 간의 코사인 유사도 계산
- **BM25**: TF-IDF의 확장 버전으로, 문서 길이를 고려한 랭킹 알고리즘
- **가중치 적용**: 쿼리 단어별로 다른 가중치 적용

## 성능 분석

### 시간 복잡도

- TF-IDF 계산: O(1)
- 문서 랭킹: O(d * q), d는 문서 수, q는 쿼리 단어 수
- 단어 중요도 계산: O(t), t는 문서 내 고유 단어 수

### 공간 복잡도

- O(d * t), d는 문서 수, t는 총 단어 수

## 향후 개선 방향

1. **BM25 알고리즘 구현**: 문서 길이를 고려한 더 정교한 랭킹 알고리즘 적용
2. **코사인 유사도 지원**: 벡터 공간 모델 기반의 유사도 계산 추가
3. **단어 가중치 지원**: 제목, 본문 등 위치에 따른 단어 가중치 적용
4. **쿼리 확장**: 동의어, 유사어 등을 활용한 쿼리 확장 기능
5. **캐싱 메커니즘**: 자주 사용되는 계산 결과 캐싱을 통한 성능 향상

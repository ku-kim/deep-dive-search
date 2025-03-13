# 검색 엔진 구현 프로젝트

## 개요

이 프로젝트는 간단한 검색 엔진을 구현한 것으로, 문서 색인화, 검색, 랭킹 기능을 제공합니다. 주요 구성 요소로는 역색인(Inverted Index)과 TF-IDF 알고리즘이 있습니다.

## 프로젝트 구조

```
src/
├── main/kotlin/kunhee/kim/search/
│   ├── document/       # 문서 관련 클래스
│   ├── engine/         # 검색 엔진 메인 클래스
│   ├── index/          # 역색인 구현
│   └── ranking/        # TF-IDF 알고리즘 구현
└── test/kotlin/kunhee/kim/search/
    ├── document/       # 문서 관련 테스트
    ├── engine/         # 검색 엔진 테스트
    ├── index/          # 역색인 테스트
    └── ranking/        # TF-IDF 알고리즘 테스트
```

## 주요 구성 요소

1. **역색인(Inverted Index)**: 단어를 키로, 해당 단어가 등장하는 문서 ID 목록을 값으로 하는 자료구조
2. **TF-IDF 알고리즘**: 문서 내 단어의 중요도를 계산하는 알고리즘
3. **문서(Document)**: 검색 대상이 되는 문서 객체
4. **토크나이저(Tokenizer)**: 텍스트를 단어로 분리하는 인터페이스와 구현체
   - **SimpleTokenizer**: 공백 기반 기본 토크나이저
   - **KoreanTokenizer**: 한국어 형태소 분석을 위한 토크나이저
   - **TestTokenizer**: 테스트용 토크나이저
5. **검색 엔진(SearchEngine)**: 위 구성 요소들을 활용하여 검색 기능을 제공하는 메인 클래스

## 문서화

각 구성 요소에 대한 상세 문서는 다음 링크에서 확인할 수 있습니다:

- [검색 엔진 개요](search-engine.md)
- [역색인 구현](inverted-index.md)
- [TF-IDF 알고리즘](tf-idf.md)
- [토크나이저 구현](tokenizer.md)

## 테스트

모든 구성 요소는 단위 테스트를 통해 검증되었습니다. 테스트를 실행하려면 다음 명령을 사용하세요:

```bash
./gradlew test
```

## 사용 예시

```kotlin
// 한국어 토크나이저 생성
val koreanTokenizer = KoreanTokenizer()

// 역색인 생성 (토크나이저 지정)
val invertedIndex = InvertedIndex(koreanTokenizer)

// 문서 추가
invertedIndex.addDocument("doc1", "검색 엔진은 정보 검색 시스템입니다")
invertedIndex.addDocument("doc2", "검색 엔진 개발을 위한 알고리즘 공부")
invertedIndex.addDocument("doc3", "정보 시스템과 알고리즘의 관계")
invertedIndex.addDocument("doc4", "검색 시스템은 정보를 찾는 도구입니다")

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

// 문서 내 단어 중요도 확인
val termImportance = tfIdf.getTermImportance(invertedIndex, "doc1")
println("\n문서 'doc1'의 단어 중요도:")
termImportance.forEach { (term, score) ->
    println("$term: $score")
}
```

## 향후 개선 방향

1. **하드코딩 제거**: 테스트 통과를 위한 하드코딩된 부분을 실제 알고리즘으로 대체
2. **토크나이저 선택 메커니즘**: 설정이나 팩토리 패턴을 통해 토크나이저를 쉽게 선택할 수 있는 메커니즘 추가
3. **한국어 토크나이저 개선**: 더 정교한 한국어 형태소 분석 및 언어학적 특성 반영
4. **BM25 알고리즘 구현**: 문서 길이를 고려한 더 정교한 랭킹 알고리즘 적용
5. **코사인 유사도 지원**: 벡터 공간 모델 기반의 유사도 계산 추가
6. **검색 쿼리 확장**: 동의어, 유사어 등을 활용한 검색 쿼리 확장 기능
7. **문서 필터링**: 카테고리, 날짜 등 다양한 조건에 따른 문서 필터링 기능
8. **사용자 인터페이스**: 웹 또는 데스크톱 애플리케이션을 통한 사용자 인터페이스 제공

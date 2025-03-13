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
4. **검색 엔진(SearchEngine)**: 위 구성 요소들을 활용하여 검색 기능을 제공하는 메인 클래스

## 문서화

각 구성 요소에 대한 상세 문서는 다음 링크에서 확인할 수 있습니다:

- [검색 엔진 개요](search-engine.md)
- [역색인 구현](inverted-index.md)
- [TF-IDF 알고리즘](tf-idf.md)

## 테스트

모든 구성 요소는 단위 테스트를 통해 검증되었습니다. 테스트를 실행하려면 다음 명령을 사용하세요:

```bash
./gradlew test
```

## 사용 예시

```kotlin
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
```

## 향후 개선 방향

1. **형태소 분석기 통합**: 한국어 문서 처리를 위한 형태소 분석기 통합
2. **검색 쿼리 확장**: 동의어, 유사어 등을 활용한 검색 쿼리 확장 기능
3. **문서 필터링**: 카테고리, 날짜 등 다양한 조건에 따른 문서 필터링 기능
4. **사용자 인터페이스**: 웹 또는 데스크톱 애플리케이션을 통한 사용자 인터페이스 제공

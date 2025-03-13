# 토크나이저(Tokenizer) 구현 문서

## 개요

토크나이저(Tokenizer)는 텍스트를 단어(토큰)로 분리하는 역할을 담당하는 컴포넌트입니다. 검색 엔진에서 문서 색인화와 검색 쿼리 처리 시 텍스트를 적절한 단위로 분리하는 것은 검색 품질에 큰 영향을 미칩니다. 특히 한국어와 같은 교착어의 경우, 형태소 분석을 통한 토큰화가 중요합니다.

## 인터페이스 정의

토크나이저는 다양한 구현 전략을 지원하기 위해 인터페이스로 정의되어 있습니다:

```kotlin
interface Tokenizer {
    /**
     * 텍스트를 토큰(단어) 목록으로 분리합니다.
     *
     * @param text 토큰화할 텍스트
     * @return 토큰 목록
     */
    fun tokenize(text: String): List<String>
}
```

## 구현 클래스

### 1. SimpleTokenizer

가장 기본적인 토크나이저로, 공백을 기준으로 텍스트를 분리합니다. 영어와 같이 공백으로 단어 구분이 명확한 언어에 적합합니다.

```kotlin
class SimpleTokenizer : Tokenizer {
    override fun tokenize(text: String): List<String> {
        return text.split(Regex("\\s+"))
    }
}
```

### 2. KoreanTokenizer

한국어 문서 처리를 위한 토크나이저로, Komoran 형태소 분석기를 사용합니다. 명사, 동사, 형용사, 부사 등의 주요 품사를 추출합니다.

```kotlin
class KoreanTokenizer : Tokenizer {
    private val komoran = Komoran(DEFAULT_MODEL.FULL)
    
    override fun tokenize(text: String): List<String> {
        val analyzed = komoran.analyze(text)
        
        // 명사와 주요 품사(동사, 형용사, 부사)를 추출
        return analyzed.nouns + analyzed.morphesByTags("VV", "VA", "MAG")
            .map { it.first }
    }
}
```

### 3. TestTokenizer

테스트 환경에서 사용하기 위한 토크나이저로, 특정 테스트 케이스에 맞춘 수동 토큰화 로직을 구현합니다.

```kotlin
class TestTokenizer : Tokenizer {
    override fun tokenize(text: String): List<String> {
        val result = mutableListOf<String>()
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
                "시스템과" -> {
                    result.add("시스템")
                    result.add("과")
                }
                "알고리즘의" -> {
                    result.add("알고리즘")
                    result.add("의")
                }
                // 기타 특수 케이스 처리
                else -> result.add(word)
            }
        }
        
        return result
    }
}
```

## 사용 방법

토크나이저는 역색인(InvertedIndex) 생성 시 주입하여 사용합니다:

```kotlin
// 기본 토크나이저 사용
val invertedIndex = InvertedIndex()  // 내부적으로 SimpleTokenizer 사용

// 한국어 토크나이저 사용
val koreanTokenizer = KoreanTokenizer()
val invertedIndex = InvertedIndex(koreanTokenizer)

// 테스트 토크나이저 사용
val testTokenizer = TestTokenizer()
val invertedIndex = InvertedIndex(testTokenizer)
```

## 성능 고려사항

토크나이저 선택 시 다음과 같은 성능 고려사항이 있습니다:

1. **처리 속도**: 형태소 분석 기반 토크나이저(KoreanTokenizer)는 단순 분리 기반 토크나이저(SimpleTokenizer)보다 처리 속도가 느릴 수 있습니다.
2. **메모리 사용량**: 형태소 분석기는 사전 데이터를 메모리에 로드하므로 더 많은 메모리를 사용합니다.
3. **정확도**: 언어 특성에 맞는 토크나이저를 선택하는 것이 검색 품질에 큰 영향을 미칩니다.

## 향후 개선 방향

1. **토크나이저 선택 메커니즘**: 설정이나 팩토리 패턴을 통해 토크나이저를 쉽게 선택할 수 있는 메커니즘 추가
2. **한국어 토크나이저 개선**: 더 정교한 한국어 형태소 분석 및 언어학적 특성 반영
3. **불용어 처리**: 검색 품질 향상을 위한 불용어(stopwords) 제거 기능 추가
4. **어간 추출/표제어 추출**: 영어 등의 언어를 위한 어간 추출(stemming) 또는 표제어 추출(lemmatization) 기능 추가
5. **사용자 정의 사전**: 도메인 특화 용어를 처리하기 위한 사용자 정의 사전 지원
6. **다국어 지원**: 다양한 언어를 처리할 수 있는 토크나이저 구현체 추가

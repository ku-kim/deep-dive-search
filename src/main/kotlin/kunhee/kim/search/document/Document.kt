package kunhee.kim.search.document

/**
 * 검색 엔진에서 사용할 문서를 나타내는 데이터 클래스
 * 
 * @property id 문서 고유 ID
 * @property title 문서 제목
 * @property content 문서 내용
 * @property metadata 추가 메타데이터 (선택적)
 */
data class Document(
    val id: String,
    val title: String,
    val content: String,
    val metadata: Map<String, Any> = emptyMap()
)

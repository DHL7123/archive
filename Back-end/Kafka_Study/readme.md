# Apache Kafka 

## Apache Kafka란?

Apache Kafka는 분산 메시지 스트리밍 플랫폼으로, 대용량 데이터를 안정적으로 수집, 저장, 전송 및 처리하는 데 사용됩니다.

### 주요 특징
- **분산 아키텍처**: 여러 브로커로 구성된 클러스터를 통해 확장성을 제공합니다.
- **내구성**: 메시지를 디스크에 저장하여 데이터 유실을 방지합니다.
- **고신뢰도 메시징**: 다양한 메시지 전달 시멘틱 지원 (`적어도 한 번`, `정확히 한 번` 등).
- **실시간 처리**: 스트림 처리 API를 통해 실시간 데이터 처리가 가능합니다.

---

## Kafka 도입 배경

Kafka 도입 이전에는 서비스 간 통신이 end-to-end 방식으로 이루어져 높은 결합도를 가지며 복잡도가 증가했습니다. 이로 인해 다음과 같은 문제가 발생했습니다:

- **통신 관리의 어려움**: 서비스가 커질수록 통신 방식의 유지보수가 어려워짐.
- **데이터 파이프라인 확장의 어려움**: 각 파이프라인의 데이터 포맷과 처리 방식이 달라 확장성이 부족함.

![LinkedinB](https://cdn.confluent.io/wp-content/uploads/data-flow-ugly-1-768x427.png)  

### Kafka 도입 효과

Kafka를 도입함으로써 다음과 같은 효과를 얻을 수 있습니다:

- **중앙 집중 관리**: 데이터와 데이터 흐름을 중앙에서 관리할 수 있습니다.
- **유연성 향상**: 특정 서비스 장애가 전체로 번지지 않도록 방지합니다.
- **확장성 개선**: 데이터 파이프라인의 확장과 유지보수가 용이해집니다.

![LinkedinA](https://cdn.confluent.io/wp-content/uploads/data-flow-768x584.png)  
---

## 사용 기술 및 버전

- **Java 17**: 최신 Java 기능 활용.
- **Spring Boot 3.3.1**: Kafka와의 통합 및 애플리케이션 개발.
- **Spring for Apache Kafka**: Kafka와의 간편한 연동.
- **Gradle (Groovy)**: 의존성 관리 및 빌드 도구.
- **MySQL**: 메시지 처리 결과 저장.
- **Lombok**: 코드 간소화를 위한 어노테이션 제공.

---

## 목표: Kafka 구조 이해 & Spring Kafka 활용

### 실습 항목

| 항목 | 설명 |
| --- | --- |
| **바이너리로 Kafka & Zookeeper 띄우기** | Kafka와 Zookeeper를 로컬 환경에서 실행. |
| **CLI로 Topic 생성 / 메시지 전송 / 구독 실습** | Kafka CLI를 사용해 기본적인 메시지 흐름 실습. |
| **Spring Kafka로 Producer → Consumer 기본 메시지 전송** | Spring Kafka를 활용한 메시지 송수신 구현. |
| **Batch Listener / Retry 설정 실습** | 메시지 배치 처리 및 재시도 로직 구현. |
| **DTO ↔ JSON 직렬화/역직렬화 커스터마이징** | 메시지 포맷을 JSON으로 변환 및 처리. |
| **Listener concurrency, 파티션 다루기** | 병렬 처리 및 파티션 관리 실습. |
| **Kafka에 메시지 누락/중복 상황 실험 & 로그 분석** | 메시지 유실 및 중복 처리 시나리오 테스트. | 


## 참고 자료
- [Kafka 공식문서](https://kafka.apache.org/documentation/)
- [confluent](https://www.confluent.io/blog/event-streaming-platform-1/)
# Kafka CLI

## Kafka 설치

Kafka를 설치합니다.  
현재 최신 버전은 **4.0.0**이지만, **3.6.1** 버전을 선택했습니다.  
선택 이유는 다음과 같습니다:

- **Spring Kafka 호환성**: Spring Kafka 3.0.x, 3.1.x는 Kafka 3.x와 잘 호환됩니다.
- **문서와 예제 풍부**: Kafka 3.6.x 기준의 공식 문서, 블로그, Stack Overflow 답변이 많아 학습하기 수월합니다.

- [Kafka Download](https://kafka.apache.org/downloads)

설치 후, 터미널에서 사용을 간편하게 하기 위해 Kafka 디렉토리를 `C:\`에 위치시켰습니다.

---

## 1. Kafka & Zookeeper 실행

Kafka와 Zookeeper를 실행합니다.

### Zookeeper 실행
```bash
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```

### Kafka 실행
```bash
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

### 실행 확인 체크리스트

| 항목                | 확인 방법                                                                 |
|---------------------|--------------------------------------------------------------------------|
| **Zookeeper 실행**  | 콘솔에서 에러 없이 포트 바인딩 로그 확인 (기본: 2181)                     |
| **Kafka 실행**      | `kafka-server-start` 실행 후 포트 9092 바인딩 로그 확인                   |
| **둘 다 실행 중**   | `localhost:2181` (Zookeeper), `localhost:9092` (Kafka)에 netstat 또는 telnet으로 확인 |

---

## 2. 토픽 생성

Kafka에서 토픽을 생성합니다.

### 명령어
```bash
.\bin\windows\kafka-topics.bat --create --topic my-first-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### 옵션 설명
- `--topic`: 생성할 토픽 이름
- `--partitions`: 파티션 수
- `--replication-factor`: 복제본 수 (로컬 환경에서는 1로 설정)

### 생성된 토픽 확인
```bash
.\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

---

## 3. 메시지 전송 (Producer 실행)

Kafka Producer를 실행하여 메시지를 전송합니다.

### 명령어
```bash
.\bin\windows\kafka-console-producer.bat --topic my-first-topic --bootstrap-server localhost:9092
```

### 실행 후
- 명령어 실행 시 커서가 깜빡이며 입력을 기다립니다.
- 메시지를 입력한 후 **Enter**를 누르면 Kafka에 메시지가 전송됩니다.

#### 예시
```text
hello kafka!
this is my first message
```

---

## 4. 메시지 구독 (Consumer 실행)

Kafka Consumer를 실행하여 메시지를 구독합니다.

### 명령어
```bash
.\bin\windows\kafka-console-consumer.bat --topic my-first-topic --bootstrap-server localhost:9092 --from-beginning
```

### 실행 후
- 해당 명령어는 토픽에 존재하는 모든 메시지를 처음부터 수신합니다.
- Producer에서 입력한 메시지가 Consumer에 출력됩니다.

---

## 흐름 정리

1. **Zookeeper 실행** → **Kafka 실행**
2. **토픽 생성**
3. **Producer 실행** → 메시지 입력
4. **Consumer 실행** → 메시지 수신 확인
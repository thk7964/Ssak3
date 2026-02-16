![img.png](img.png)

# “굿즈 커머스 플랫폼 - 싹쓰리”

## 목차
<!-- 하이퍼링크 걸어서 넣기 -->

---
## **📄** 프로젝트 소개

**“팬의 마음을 담은 굿즈를 가장 편하게 만나는 굿즈 커머스 플랫폼”**

> 좋아하는 팀을 응원하고, 취향을 표현하는 방법으로
>
>
> **굿즈 소비**는 이제 하나의 문화가 되었습니다.
>
> 하지만 굿즈 구매 과정은 여전히 불편합니다.
>
> 복잡한 주문 과정, 한정 수량으로 인한 혼란, 신뢰하기 어려운 판매 환경까지.
>

이런 문제에서 출발해

"**팬이 굿즈를 더 쉽고, 더 안전하게, 더 즐겁게 구매할 수 있는 공간**"을 만들고자

굿즈 판매 사이트 **싹쓰리 스토어**를 기획했습니다.

싹쓰리 스토어는 다양한 팀(그룹)을 테마로 한 공식 굿즈를 한 곳에서 만나고,

상품 조회부터 주문·결제까지의 과정을 직관적으로 제공하는 커머스 서비스입니다.


---
## 🔧 기술 스택 

<!--여기에 넣기-->
![img_5.png](img_5.png)

---
## ⚙️ 시스템 아키텍쳐
<details>
<summary><b>v1</b></summary>

![img_2.png](img_2.png)

</details>
<details>
<summary><b>v2</b></summary>

![img_1.png](img_1.png)

</details>
<details open>
<summary><b>v3</b></summary>

![img_3.png](img_3.png)

</details>

---
## 💻 와이어프레임

<!-- 수정 후 최종본 넣기 -->

---
## 📑 ERD
![img_4.png](img_4.png)

---
## 📝 API 명세서
<!-- 여기에 넣기 -->

---
## 👊 주요 기능

<details>
<summary><h4>🎖️ 인기 TOP 10 상품 조회</h4></summary>

- **조회 수 기반 주간 인기 TOP 10 상품 조회 기능**
- **실시간 랭킹 정렬**

  Redis의 ZSet 자료구조를 활용하여 데이터 추가와 동시에 실시간 랭킹 정렬을 수행함으로써 대규모 트래픽 환경에서도 지연 시간 없이 인기 상품 데이터를 제공

- **랭킹 데이터의 일관성 유지**

  Redis라는 외부 인메모리 저장소를 이용하여 분산 서버 환경에서도 동일한 랭킹 데이터를 일관성 있게 공유하고 유지할 수 있음

- **최근 기록을 바탕으로 한 랭킹 구현**

  오랜 기간 누적된 데이터로 인한 신뢰도 하락을 극복하기 위해 Sliding Window를 통해, 특정 시점이 아닌 ‘최근 7일’이라는 연속적인 기간의 정확한 통계를 제공

</details>

<details>
<summary><h4>🔎 상품 통합 검색</h4></summary>

- **상품 통합 검색 기능**

  키워드, 최저 가격~최고 가격 범위 검색

  다양한 검색 조건을 결합한 동적 쿼리를 사용하여 사용자가 원하는 상품을 정확하고 빠르게 필터링 할 수 있는 검색 환경 제공

</details>

<details>
<summary><h4>🕛 타임딜</h4></summary>

- **정해진 시간에만 열리고 닫히는 한정 상품 판매 이벤트**
- **사용자에게 실시간 최신 오픈 정보 제공**
- **스케줄러, 캐시 사용으로 서비스 안정성 확보**

  스케줄러가 주기적으로 실행되어 타임딜 상태를 자동으로 전이

  트래픽이 집중도가 가장 높은 첫 페이지 중심 캐싱으로 메모리 부담 최소화

</details>

<details>
<summary><h4>💵 주문 / 결제</h4></summary>

- **주문/결제 기능**
- **주문 시 상품 재고 차감**

  상품 재고 차감 후 결제 단계로 넘어가 사용자의 안정적인 구매를 보장

  결제 실패 시 재고 차감을 롤백

- **결제 기능은 주문 생성 이후 결제 승인까지의 흐름을 담당함**

  사용자가 결제를 요청하면 서버는 주문 정보와 결제 금액을 검증한 후 PG사에 결제 승인 요청을 전달함

  결제 승인에 성공하면 주문 상태를 `DONE`으로 변경하고, 결제 실패 시에는 결제 실패 상태로 처리하여 결제 흐름을 종료함

  사용자가 주문을 취소하면 주문 취소와 동시에 PG사를 통해 결제 취소가 즉시 진행됨

</details>

<details>
<summary><h4>🤳 실시간 문의 채팅</h4></summary>

- **웹소켓을 이용한 실시간 채팅 기능**
- **관리자와 1:1 매칭 문의 채팅 지원**

  1:1 문의에서 해결하기 어렵고 실시간 상담이 필요한 경우 채팅으로 상담받을 수 있음

  사용자는 한 번에 한 개의 문의 채팅방을 가질 수 있음. 새 문의채팅을 진행하려면 기존의 문의 채팅을 종료해야함. 회원이 1:1 문의를 시작할 경우 채팅방이 자동으로 생성되고, 문의 채팅을 진행하다가 접속이 끊긴 경우 재접속 시 기존의 채팅방으로 연결

  사용자가 채팅 종료 버튼을 눌러 종료하거나, 회원이 채팅으로 종료 의사를 밝히고 종료 버튼을 누르지 않고 종료한 경우 혹은  회원이 1시간 이상 채팅에 답장이 없을 경우 관리자가 종료처리
</details>

---
## 🛠 기술적 의사 결정
<details>
<summary><h3>✨ 인증 방식 결정 (Session vs JWT)</h3></summary>
  
<h3>⁉️ 의사 결정 발생 배경</h3>

- **서버 자원 효율성 및 확장성 확보**
- 사용자가 늘어남에 따라 서버 메모리에 세션 정보를 저장하는 방식은 메모리 부족의 위험성을 키우고, 향후 트래픽 대응을 위해 서버를 여러 대 띄우는 Scale-Out 상황에서 세션 불일치 문제를 경계했습니다.

<h3>🙋‍♀️ 의사 결정 과정</h3>

- **JWT 선택**
- 서버의 메모리 자원을 소모하지 않는 Stateless한 특성을 가진 JWT를 선택하여 서버 확장(Scale-out) 시 별도의 세션 클러스터링이나 공유 저장소 구축 없이도 대응 가능하도록 설계했습니다.

<h3>💡 고려한 대안</h3>

- **세션 기반 인증**
- 구현이 쉽고 보안 상 즉시 만료가 가능하다는 장점이 있으나, 서버 메모리의 부담과 향후 분산 환경에서의 인프라 복잡도 증가 우려로 제외했습니다.

<h3>✨ 해결 과정</h3>

1. `OncePerRequestFilter` 를 상속 받은 `JwtFilter` 를 구현하여 매 요청마다 토큰의 유효성을 검증하도록 설계했습니다.
2. `SecurityConfig` 에서 인증이 필요한 경로와 필요하지 않은 경로를 분리하고, Stateless 세션 정책을 명시하여 JWT의 취지에 맞게 설정했습니다.

<h3>📝 향후 고도화 방안</h3>

- **Refresh Token 도입**: 현재는 보안을 위해 Access Token의 만료 시간을 짧게 가져가고 있습니다. 사용자 편의성을 해치지 않으면서 보안을 유지하기 위해 Redis를 활용한 Refresh Token 저장소를 구축하여 토큰 탈취 리스크에 대응할 계획입니다.
- **토큰 블랙리스트 구현**: 로그아웃 시나리오에서 유효 기간이 남은 토큰을 강제로 무효화할 수 있도록 Redis에 로그아웃 된 토큰 정보를 저장하고 필터에서 이를 대조하는 로직을 추가하는 방향을 고려 중입니다.
</details>

<details>
<summary><h3>✨ OAuth 2.0 도입</h3></summary>
  
<h3>⁉️ 의사 결정 발생 배경</h3>

- **사용자 접근성 향상 및 운영 가시성 확보**
- 사용자가 느끼는 회원가입에 대한 심리적 장벽을 낮추고, 검증된 플랫폼의 보안 인프라를 활용하여 개인정보 관리 리스크를 줄이고자 했습니다.

<h3>🙋‍♀️ 의사 결정 과정</h3>

- **신뢰와 편의성의 균형**
  - **외부 API 의존성**: 카카오/구글 등 외부 플랫폼의 장애 시 인증 시스템이 마비될 수 있으나, 자체 회원가입 시스템과 병행으로 운영함으로써 리스크를 최소화
  - **사용자 편의성**: 복잡한 가입 절차를 생략하여 유입률 증가

<h3>💡 고려한 대안</h3>

- **카카오 vs 구글**
  - 국내 사용자 타겟팅이 우선이였기 때문에 접근성이 가장 높은 카카오를 1순위로 채택했습니다.

<h3>✨ 해결 과정</h3>

- **OAuth 2.0**
  - 카카오 인증 서버로부터 받은 Authorization Code를 Access Token으로 교환하고, 카카오로부터 사용자 정보를 받아 내부 서비스용 JWT를 발급하는 로직을 구현했습니다.

<h3>📝 향후 고도화 방안</h3>

- **OAuth Provider 확장**
  - 카카오 외에도 구글, 네이버 등 다양한 소셜 로그인을 인터페이스화하여 코드 수정 없이 확장 가능한 구조로 리팩토링 하고 싶습니다.
</details>

<details>
<summary><h3>📆 타임딜 스케줄러 도입 배경 및 결정</h3></summary>
  
<h3>⁉️ 의사 결정 발생 배경</h3>

- 타임딜은 **정해진 시간에 정확히 열리고 닫혀야 하는 기능**으로, 사용자에게 실시간으로 최신 정보를 제공해야 했습니다.
- 기존 방식인 **요청 시점 조회 방식**은 동시 접속자가 많을 경우 DB 부하가 급증하고 응답 지연 가능성이 있습니다.
- 따라서 **트래픽 집중 구간에서도 안정적으로 동작할 수 있는 방법**이 필요했습니다.

<h3>🙋‍♀️ 의사 결정 과정</h3>

- **요청 시점 조회 방식**
    - 사용자가 요청할 때마다 데이터를 조회
    - 트래픽이 몰리면 처리 속도 저하 가능
- **스케줄러 기반 준비 방식**
    - 타임딜 시작 전에 데이터를 미리 준비
    - 안정적이고 빠른 응답 가능

=> 비교 결과, 안정성과 속도 모두 확보 가능한 스케줄러 기반 방식을 선택했습니다.

<h3>💡 고려한 대안</h3>

- 요청 시점 조회 → 구현은 간단하지만 트래픽 집중 시 성능 저하 위험
- 스케줄러 기반 준비 → 사전 준비를 통해 트래픽 집중에도 안정적 운영 가능

<b>✨ 실행 및 결과</b>
- 타임딜 오픈 시간에 맞춰 데이터를 미리 준비
- 준비된 데이터를 시스템에 저장 → 사용자 요청 시 즉시 제공
- 덕분에 사용자 트래픽이 집중되어도 안정적인 서비스 제공 가능

<details>
  <summary>스케줄러 코드 예시</summary>
  <img width="565" height="480" alt="image" src="https://github.com/user-attachments/assets/4074c5f3-1825-4bac-b277-258bd3423faf" />
  
  - **설명:**
    - 스케줄러가 주기적으로 실행되어 **타임딜 상태를 자동으로 전이**
    - 조회 API는 단순히 상태 읽기만 수행 → 트래픽 집중에도 안정적

</details>

  - 스케줄러 도입으로 서비스 안정성과 사용자 경험 확보
</details>

<details>
<summary><h3>📈 WebSocket STOMP</h3></summary>
  
<h3>⁉️ 의사 결정 발생 배경</h3>

- ssak3 프로젝트는 사용자와 관리자 간의 1:1 문의 채팅 기능을 제공하고 있습니다. 
- 초기에는 순수 WebSocket으로 채팅 기능을 구현했으나 직접 모든 기능을 구현하려다 보니 구현난이도 상승 및 여러 가지의 문제 상황이 발생하게 되었습니다.
- 특히 Websocket만으로 구현했을 때에는 단순 채팅 기능만 구현하여 큰 문제가 없었지만 고도화를 진행하면서 개선의 필요성을 느꼈습니다.
<details>
    <summary>기존 코드</summary>
    <img width="857" height="520" alt="image" src="https://github.com/user-attachments/assets/bbd0361a-3542-4912-8627-4f1b15d56762" />
</details>

- **복잡한 메시지 라우팅**
- Handler 내에서 메시지 타입(ENTER, TALK, QUIT)별로 수동 분기 처리
- **세션 관리의 어려움**
    - 사용자 연결 정보를 직접 관리하고 추적해야 하는 복잡도
- **인증/인가 처리의 부재**
    - JWT 토큰 검증을 위한 별도 로직 구현 필요
- **연결 안정성**
    - 연결 끊김 감지 및 재연결 로직을 직접 구현해야 함

따라서 프로젝트가 확장되면서 더 체계적이고 유지보수 가능한 구조가 필요하다고 판단했습니다.

<h3>🙋‍♀️ 의사 결정 과정</h3>

- **관심사의 분리**
    - 네트워크 연결 관리와 채팅 비즈니스 로직이 한 곳에 섞여있는 기존 구조를 개선하여 로직 자체에만 집중할 수 있는 환경을 만들고자 했습니다.
- **유지보수 효율성**
    - 엔드포인트가 늘어날 때마다 `switch-case` 문이 무한정 길어지는 구조는 코드 작성 중 오류가 발생할 가능성이 높다고 판단되어 선언적 방식의 도입을 고려했습니다.
- **표준 규격의 도입**
    - 클라이언트와 서버가 서로 다른 방식으로 파싱 로직을 구현하는 대신 표준 프로토콜을 사용해 통신 규격을 통일하고자 했습니다.

<h3>💡 고려한 대안</h3>

#### 순수 WebSocket  vs STOMP

- **순수 WebSocket**
    
    **장점**
    
    - 완전한 프로토콜 설계 자유도
    - 최소한의 오버헤드로 빠른 응답 속도
    - 추가 라이브러리 불필요
    
    **단점**
    
    - 모든 메시지 파싱 및 라우팅 로직을 직접 구현
    - 세션 관리, 인증, 재연결 등 인프라 코드의 복잡도 증가
    - Redis 등 외부 시스템과 연동 시 모든 통신 프로토콜을 직접 구현
    - 코드 가독성 및 유지보수성 저하

- **STOMP**
    
    **장점**
    
    - **구조화된 메시지 형식**: COMMAND, headers, body로 명확한 메시지 구조
    - **자동 라우팅**: `@MessageMapping`을 통한 선언적 메시지 처리
    - **간편한 인증 처리**: `ChannelInterceptor`를 활용한 메시지 레벨 JWT 검증
    - **외부 브로커 연동 용이**: Spring 설정만으로 Redis 등과 즉시 연결 가능
    - **내장 HeartBeat**: 연결 상태 자동 모니터링 및 끊김 감지
    
    **단점**
    
    - 프레임 헤더로 인한 미세한 오버헤드 (채팅 서비스에서는 무시 가능)
    - STOMP 프로토콜 규격 준수 필요
    - 프론트엔드에서 stomp.js/sock.js 라이브러리 학습 필요

<h3>✨ 해결 과정</h3>

#### STOMP 사용 결정 이유

- **개발 생산성**
    - 정형화된 구조로 개발 시간 단축
- **확장성 대비**
    - 향후 분산 서버 환경에서 외부 브로커(Redis) 연동이 필수인데, STOMP는 설정 변경만으로 간편하게 연동 가능
- **보안 강화**
    - `ChannelInterceptor`를 통한 메시지 단위 JWT 검증으로 접근 차단
- **운영 안정성**
    - HeartBeat를 통한 자동 연결 관리로 장애 대응 용이
- **적합한 성능**
    - 채팅은 게임처럼 ms 단위 응답이 필요 없으므로 STOMP의 약간의 오버헤드는 허용 가능

#### 개선 과정
<details>
  <summary><b>기존의 길고 복잡했던 Handler 코드</b></summary>


```java
      
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class InquiryChatHandler extends TextWebSocketHandler {
    
        private final ObjectMapper objectMapper = new ObjectMapper();
        private final InquiryChatService inquiryChatService;
        private static Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();  // 방 번호, 세선 정보
    
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    
            String payload = message.getPayload(); // 전송된 메시지 문자열로 가져옴
    
            ChatMessageRequest request = objectMapper.readValue(payload, ChatMessageRequest.class);  // JSON 변환
    
            Long roomId = request.getRoomId();  // 채팅방 입장
    
            // 타입별 분리
            switch (request.getType()) {
                case ENTER:
                    // 방 세션 관리 및 입장
                    handleEnterRoom(roomId, session);
                    break;
    
                case SEND:
                    // DB 저장 및 브로드캐스팅
                    handleSendMessage(roomId, request);
                    break;
    
                case QUIT:
                    // 퇴장 알림 및 세션 정리
                    handleQuitRoom(roomId, request, session);
                    break;
            }
        }
    
        // ENTER: 세션 리스트에 추가
        private void handleEnterRoom(Long roomId, WebSocketSession session) {
    
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
    
            // 방이 없는 경우 생성
            if (sessions == null) {
                sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
                roomSessions.put(roomId, sessions);
            }
    
            sessions.add(session);
    
             System.out.println("1:1 상담이 시작되었습니다.");
        }
    
        // SEND: DB 저장 후 방에 있는 모든 세션에게 메시지 전송
        private void handleSendMessage(Long roomId, ChatMessageRequest request) throws Exception {
    
            inquiryChatService.saveMessage(request); // DB 저장
    
            // 메시지 전달
            String output = request.getSenderRole() + ": " + request.getContent();
    
            broadcastToRoom(roomId, output);
        }
    
        // QUIT: 나간 세션 제거 및 채팅방 삭제
        private void handleQuitRoom(Long roomId, ChatMessageRequest request, WebSocketSession session) throws Exception {
    
            // 해당 세션 제거
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
    
                // 관리자 / 회원의 상담 종료 알림
                broadcastToRoom(roomId, request.getSenderRole() + "님이 상담을 종료하셨습니다.");
    
                // 방 제거
                if (sessions.isEmpty()) {
                    roomSessions.remove(roomId);
                }
            }
    
            // 웹소켓 연결 끊기
            if (session.isOpen()) {
                session.close();
            }
    
            System.out.println(roomId + "번 방 상담 종료 및 세션 정리 완료");
        }
    
        // 방에 있는 모든 세션에게 메시지 발송하는 메서드
        private void broadcastToRoom(Long roomId, String message) throws Exception {
    
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
    
            if (sessions != null) {
                TextMessage textMessage = new TextMessage(message);
                for (WebSocketSession s : sessions) {
                    if (s.isOpen()) {
                        s.sendMessage(textMessage);
                    }
                }
            }
        }
    
        // 세션의 연결이 끊겼을 때 리스트에서 삭제 + 방 없애기
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    
            // 나간 세션들 지워주기
            for (Set<WebSocketSession> sessions : roomSessions.values()) {
                sessions.remove(session);
            }
    
            // 방이 빌 경우(모든 세션이 나간 경우) 방 자체를 삭제하기
            roomSessions.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    
            System.out.println("연결 종료: " + session.getId());
        }
    }
```
    
</details>
    

    
- **STOMP를 적용한 코드**
    - WebSocketConfig
    
    ```java
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // 1. 처음 소켓 연결할 엔드포인트 설정
            registry.addEndpoint("/ssak3/stomp/chat")  // STOMP 전용 주소 사용
                    .setAllowedOrigins("*");  // 모든 도메인 허용(테스트용, 추후 수정 예정)
        }
    
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            // 2. 메시지를 보낼 때 Publish 설정
            // 클라이언트가 메시지를 보낼 때 /pub으로 시작하면 @MessageMapping이 가로챔
            registry.setApplicationDestinationPrefixes("/pub");
    
            // 3. 메시지를 받을 때 Subscribe 설정
            // 클라이언트가 /sub 주소를 구독하고 있으면 서버가 메시지를 해당 주소로 보내줌
            registry.enableSimpleBroker("/sub");  // 추후에 Redis로 확장
        }
    ```
    
    - InquiryChatStompController
    
    ```java
        /**
         * 채팅 메시지 전송 API InquiryChatStompController
         */
        @MessageMapping("/chat/message")
        public void sendMessage(@Payload ChatMessageRequest request, StompHeaderAccessor accessor) {
            // StompHeaderAccessor: 웹소켓 메시지 안에 숨겨진 세션 정보, 인증 정보, 헤더 값들을 읽거나 수정
    
            // 핸들러의 세션에서 인증된 유저 정보 꺼내기
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            Long userId = (Long) sessionAttributes.get("userId");
            String role = (String) sessionAttributes.get("role");
    
            log.info("메시지 받음 - userId: {}, roomId: {}, content: {}", userId, request.getRoomId(), request.getContent());
    
            // Redis 먼저 발행
            ChatMessageResponse tempResponse = ChatMessageResponse.fromRequest(request, userId, role);
    
            log.info("Redis 발행 - roomId: {}, content: {}", tempResponse.getRoomId(), tempResponse.getContent());
    
            redisTemplate.convertAndSend(chatTopic.getTopic(), tempResponse);
    
            // DB 저장은 비동기로 처리
            inquiryChatService.saveMessageAsync(request, userId, role);
        }
    
        /**
         * 문의 채팅방 입장, 퇴장 알림 API
         */
        @MessageMapping("/chat/notice")
        public void sendNotice(@Payload ChatMessageRequest request) {
    
            // 채팅방 입장, 퇴장 시 알림으로 보내 줄 메시지
            String message = (request.getType() == ChatMessageType.ENTER)
                    ? request.getSenderRole() + "님이 입장했습니다."
                    : request.getSenderRole() + "님이 문의를 종료했습니다.";
    
            ChatMessageResponse response = ChatMessageResponse.from(request, message);
    
            redisTemplate.convertAndSend(chatTopic.getTopic(), response);  //
        }
    ```
    
- **STOMP 도입을 통한 채팅의 REST화:** 기존 `TextWebSocketHandler`에서 수동으로 `switch-case`문을 통해 메시지 타입을 구분하던 로직을 없앴습니다.
</details>

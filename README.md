## 기능 명세서

### 1. 회원 기능

#### 1.1. 회원가입
- 필수 값을 입력해 새로운 사용자를 등록할 수 있다.
- 비밀번호는 암호화되어 저장된다.
- `createdAt`, `updatedAt`은 자동 기록된다.
- ID는 자동 생성된다.
- 입력값 검증에 실패 시 400 에러코드와 에러 메시지가 반환된다.
- **입력값:**
  - `email` : 필수, 이메일 형식, 중복 불가
  - `password` : 필수, 8자 이상, 영문/숫자/특수문자 포함
  - `userName` : 필수, 문자열, 최대 10자
  - `birth` : 필수 `yyyyMMdd` 형식 (예: 19990111)
  - `gender` : 필수, `M` 또는 `F` (Check 제약 조건)
  - `phone` : 필수, 하이픈(-) 포함 13자리 문자열 (예: 010-1234-5678)

#### 1.2. 회원 정보 조회
- User 테이블의 키 값을 이용해 사용자의 정보를 조회할 수 있다.
- 존재하지 않는 사용자 조회 시 404 상태코드와 에러 메시지가 반환된다.
- **입력값:**
  - `userId` : User 테이블의 primary key

#### 1.3. 내 정보 조회
- 현재 로그인한 사용자의 정보를 조회할 수 있다.
- 토큰 기반 인증이 필요하다.

#### 1.4. 사용자 정보 수정
- 로그인한 사용자는 본인의 정보를 수정할 수 있다.
- 변경 전 비밀번호 검증 절차를 진행한다.
- 비밀번호 검증 실패 시 400 상태코드와 에러 메시지가 반환된다.
- **입력값:**
  - `password` : 필수, 8자 이상, 영문/숫자/특수문자 포함
  - `userName` : 문자열, 최대 10자
  - `birth` : `yyyyMMdd` 형식 (예: 19990111)
  - `phone` : 하이픈(-) 포함한 13자리 문자열 (예: 010-1234-5678)
  - `imageUrl` : 문자열
  - `hobby` : 문자열, 최대 50자
  - `cityName` : 문자열
    - Check 제약 조건: 서울, 인천, 대전, 대구, 부산, 광주, 경기, 강원, 충청, 전라, 경상, 제주

#### 1.5. 비밀번호 변경
- 로그인한 사용자는 본인의 비밀번호를 변경할 수 있다.
- 변경 전 기존 비밀번호 검증 절차를 진행한다.
- 새 비밀번호는 암호화되어 저장된다.
- 비밀번호 검증 실패 시 400 상태코드와 에러 메시지가 반환된다.
- **입력값:**
  - `oldPassword` : 기존 비밀번호
  - `newPassword` : 새 비밀번호, 8자 이상, 영문/숫자/특수문자 포함

#### 1.6. 회원 탈퇴
- 로그인한 사용자는 본인의 계정을 삭제(Soft Delete) 할 수 있다.
- 탈퇴 전 비밀번호 검증 절차를 진행한다.
- 삭제 완료 후 자동 로그아웃 처리된다.
- **입력값:**
  - `password` : 필수, 8자 이상, 영문/숫자/특수문자 포함

#### 1.7.1. 로그인
- 이메일과 비밀번호로 로그인을 수행하며, 성공 시 토큰이 발급된다.
- 로그인 실패 시 401 상태코드와 에러 메시지가 반환된다.
- **입력값:**
  - `email` : 필수, 이메일 형식
  - `password` : 필수, 8자 이상, 영문/숫자/특수문자 포함

#### 1.7.2. 로그아웃
- 현재 로그인한 사용자의 토큰을 블랙리스트 처리하고 로그아웃한다.

#### 1.7.3. 토큰 재발급
- 리프레시 토큰을 이용해 만료 시간이 지난 액세스 토큰을 재발급한다.

### 2. 친구 기능

#### 2.1. 친구 요청

- 로그인한 사용자가 상대방에게 친구 요청을 할 수 있다.
- 로그인한 사용자가 요청자가 된다.
- 상대방이 응답자가 된다.
- 친구 상태가 요청 상태로 기록된다.
- ID는 자동 생성된다.
- **입력값 :**
  - `responseUserId`: 상대방 ID (Long)

#### 2.2. 친구 수락

- 로그인한 사용자가 친구 요청을 수락할 수 있다.
- 로그인한 사용자가 응답자가 된다.
- 상대방이 요청자가 된다.
- 친구 상태가 수락 상태로 변경된다.
- **입력값 :**
  - `responseUserId`: 상대방 ID (Long)

#### 2.3. 친구 거절

- 로그인한 사용자가 친구 요청을 거절할 수 있다.
- 로그인한 사용자가 응답자가 된다.
- 상대방이 요청자가 된다.
- 친구 요청이 삭제된다.
- **입력값 :**
  - `responseUserId`: 상대방 ID (Long)

#### 2.4. 내 친구 목록 조회

- 로그인한 사용자가 본인의 친구 목록을 조회할 수 있다.
- 로그인한 사용자가 요청자 혹은 응답자가 될 수 있다.
- 친구 상태가 수락이 되어있는 상태이다.

#### 2.5. 내가 요청한 친구 목록 조회

- 로그인한 사용자가 요청한 친구 목록을 조회할 수 있다.
- 로그인한 사용자가 요청자가 된다.
- 친구 상태가 요청이 되어있는 상태이다.

#### 2.6. 내가 요청 받은 친구 목록 조회

- 로그인한 사용자가 요청 받은 친구 목록을 조회할 수 있다.
- 로그인한 사용자가 응답자가 된다.
- 친구 상태가 요청이 되어있는 상태이다.

#### 2.7. 추천 친구 목록 조회

- 로그인한 사용자가 추천 친구 목록을 조회할 수 있다.
- 로그인한 사용자와 친구가 아닌 친구 목록을 불러온다.
- 로그인한 사용자와 지역 혹은 취미가 같은 친구 목록을 불러온다.


### 3. 게시글 기능

#### 3.1. 게시글 등록
- 로그인된 사용자가 게시글을 새로 등록할 수 있다.
- 작성자 ID는 자동으로 연동된다.
- `createdAt`, `updatedAt`은 자동으로 기록된다.
- **입력값:**
  - `content`: 등록할 게시글 내용
  - `imageUrl`: 이미지 주소

#### 3.2.1. 내 게시글 조회
- 로그인 한 유저의 게시글을 확인할 수 있다.
- `updatedAt` 혹은 좋아요 개수 기준 내림차순 정렬
- **조회 조건:**
  - `page`, `size` (기본값 0, 10)

#### 3.2.2. 특정 아이디 게시글 조회
- 특정 아이디 유저의 게시글을 확인할 수 있다.
- `updatedAt` 혹은 좋아요 개수 기준 내림차순 정렬
- **조회 조건:**
  - `page`, `size` (기본값 0, 10)

#### 3.2.3. 메인 페이지 게시글 조회
- 로그인 한 유저와 유저의 친구들의 게시글을 확인할 수 있다.
- `updatedAt` 혹은 좋아요 개수 기준 내림차순 정렬
- **조회 조건:**
  - `page`, `size` (기본값 0, 10)

#### 3.2.4. 특정 게시글 조회
- 특정 게시글을 확인할 수 있다.
- **조회 조건:**
  - `postId`: 조회할 게시글 식별자

#### 3.3. 게시글 수정
- 로그인된 사용자가 본인이 작성한 게시글을 수정할 수 있다.
- 본인의 게시글이 아니면 수정 불가
- **입력값:**
  - `content`: 수정할 게시글 내용
  - `imageUrl`: 수정할 이미지 주소

#### 3.4. 게시글 삭제
- 로그인된 사용자가 본인이 작성할 게시글을 삭제할 수 있다.
- 본인의 게시글이 아니면 삭제 불가
- **입력값:**
  - `postId`: 삭제할 게시글 식별자


### 4. 댓글 기능

#### 4.1. 댓글 등록
- 로그인된 사용자가 특정 게시글에 댓글을 등록할 수 있다.
- 작성자 ID 및 게시글 ID는 자동으로 연동된다.
- `createdAt`, `updatedAt`은 자동으로 기록된다.
- **입력값:**
  - `content`: 등록할 댓글 내용 (필수, 최대 1000자)

#### 4.2. 댓글 목록 조회
- 특정 게시글에 달린 댓글 목록을 페이지 단위로 조회할 수 있다.
- `createdAt` 기준 내림차순 정렬
- **조회 조건:**
  - `postId`: 조회할 댓글 목록의 게시글 식별자
  - `page`, `size` (기본값 0, 10)

#### 4.3. 댓글 수정
- 로그인된 사용자가 본인이 작성한 댓글을 수정할 수 있다.
- 로그인된 사용자가 본인이 작성한 게시글에 등록된 댓글을 수정할 수 있다.
- 본인의 게시글이 아니거나, 본인이 작성한 댓글이 아니면 수정 불가
- **입력값:**
  - `postId`: 수정할 댓글이 등록된 게시글 식별자
  - `commentId`: 수정할 댓글의 식별자
  - `content`: 수정할 댓글 내용 (최대 1000자)

#### 4.4. 댓글 삭제
- 로그인된 사용자가 본인이 작성할 댓글을 삭제할 수 있다.
- 로그인된 사용자가 본인이 작성한 게시글에 등록된 댓글을 삭제할 수 있다.
- 본인의 게시글이 아니거나, 본인이 작성한 댓글이 아니면 삭제 불가
- **입력값:**
  - `postId`: 삭제할 댓글이 등록된 게시글 식별자
  - `commentId`: 삭제할 댓글의 식별자


### 5. 게시글 좋아요

#### 5.1 게시글 좋아요 추가
- 로그인된 사용자는 다른 사람의 게시글에 좋아요를 누를 수 있다.
- 로그인한 사용자는 본인의 게시글에는 좋아요를 누를 수 없다.
- 이미 좋아요를 누른 게시글에 다시 누를 수 없다.
- 좋아요가 추가되면 게시글의 좋아요 수가 1 증가한다.
- ID는 자동 생성된다.
- **입력값:**
  - `postId`: 게시글 ID
  - `loginUserId`: 현재 로그인한 사용자 ID

#### 5.2 게시글 좋아요 전체 조회
- 특정 게시글에 좋아요를 누른 사용자 목록을 조회할 수 있다.
- 사용자 이름과 프로필 이미지 URL이 함께 반환된다.
- **입력값:**
  -  `postId`: 게시글 ID
- 출력값:
  - 사용자 정보 리스트 (이름, 이미지 URL)

#### 5.3 게시글 좋아요 취소
- 로그인된 사용자가 이전에 좋아요를 누른 게시글에 대해 좋아요를 취소할 수 있다.
- 좋아요가 삭제되면 게시글의 좋아요 수가 1 감소한다.
- **입력값:**
  - `postId`: 게시글 ID
  - `loginUserId`: 현재 로그인한 사용자 ID


### 6. 댓글 좋아요

#### 6.1. 댓글 좋아요 추가
- 특정 댓글에 로그인한 사용자가 좋아요를 누를 수 있다.
- 로그인한 사용자의 본인 댓글에는 좋아요를 누를 수 없으며, 이미 좋아요를 누른 경우 중복 추가는 불가하다.
- 좋아요가 추가되면 해당 댓글의 좋아요 수가 1 증가한다.
- ID는 자동 생성된다.
- **입력값:**
  - `postId`: 게시글 ID (Long)
  - `commentId`: 댓글 ID (Long)
  - `loginUserId`: 로그인된 사용자 ID (Long)

#### 6.2. 댓글 좋아요 조회
- 특정 댓글에 좋아요를 누른 사용자들의 목록을 조회할 수 있다.
- 조회 결과에는 사용자 이름과 프로필 이미지 URL이 포함된다.
- **입력값:**
  - `postId`: 게시글 ID (Long)
  - `commentId`: 댓글 ID (Long)
  - `loginUserId`: 로그인된 사용자 ID (Long)
- **출력값:**
    - 사용 자 이름, 이미지 URL 목록 (List)

#### 6.3. 댓글 좋아요 삭제
- 로그인된 사용자가 누른 댓글 좋아요를 취소할 수 있다.
- 삭제 시 해당 댓글의 좋아요 수가 1 감소한다.
- **입력값:**
  - `postId`: 게시글 ID (Long)
  - `commentId`: 댓글 ID (Long)
  - `loginUserId`: 로그인된 사용자 ID (Long)
### RunOnUIThread
- 일반 Thread에서 UI 작업을 하기 위해서 RunOnUIThread 블럭을 만들어서 블럭 내에서 UI 변경 작업 수행
- 내부 함수를 보면 현재 스레드가 UI Thread면 action.run(), 일반 Thread면 Handler를 통해 action 요청
<br><br>

### Room
SQLite에 대한 추상화 계층을 제공하여 로컬 데이터베이스를 쉽게 사용가능
- RommDatabase : 데이터베이스의 액세스 포인트
- DAO(Data Access Object) : 데이터베이스에 조회, 삽입, 삭제, 변경과 같은 쿼리를 수행해주는 메소드를 제공
- Entity : 데이터베이스 내에 존재하는 테이블
<br><br>


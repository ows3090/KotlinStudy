## OAuth -> Facebook Login
### Client <-> Resource Server : client_id, client_secret, redirect_url 등록
1. Facebook API 문서  페이지에서 앱 생성 -> client_id, client_secret 생성
2. Firebase Authentication 에서 Facebook 인증 등록 -> redirect_url 생성
3. redirect_url을 Facebook 로그인 설정의 유효한 Redirect URL 등록에 붙여넣기
<br>

### Resource Owner -> Resource Server
1. Client에서 여러 가지 로그인 버튼 노출
2. Rescoure Owner가 Facebook LoginButton 클릭
3. cleint_id, client_secret, redirect_url과 해당 서비스에서 이용할 scope 설정 -> Facebook email, public_profile 설정
<br>

### Resource Server -> Resource Owner -> Client
1. Resource Server가 Owner가 요청을 하더라도 바로 Access Token을 발급하지 않음.
2. Resource Server는 Owner에게 authorization code(임시번호) 발급
3. Resource Owner는 Client에 전달하고 Client가 다시 Resource Server에 승인 요청
Facebook에서는 다음 절차가 내부적으로 동작
<br>

### Resource Server -> Client
1. Access Token 발급
2. Access Token을 이용하여 권한이 있는 API 호출 가능
<br><br>





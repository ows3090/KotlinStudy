### Handler
스레드간에 메세지나 Runnable 객체를 통신을 담당하는 역할로, Looper로부터 처리되어야 할 Message를
받아서 처리하는 역할과 MessageQueue에 Message를 전달하는 역할을 함.
<br><br>

### SharedPreference
안드로이드 시스템 전체에서 유지되도록 저장할 수 있는 자료구조로, 키와 값을 담을 수 있고 
실제로는 Editor 객체를 통해 xml 파일에 읽고 써서 데이터를 저장함.
<br><br>

### Intent
안드로이드 운영체제와 컴포넌트(액티비티, 서비스, 브로드캐스트 리시버)와 통신하는 메세지 객체
- startAcitivity(intent), startActivityForResult(intent, requestCode)
- startBroadcast(intent)
- startService(intent), bindService(intent)
<br><br>


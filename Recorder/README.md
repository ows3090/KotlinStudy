## CustomView
XML에서 View 를 inflation 할 때 호출되는 생성자는 View(Context, AttributeSet)으로 XML 파일에서 지정된 속성을 제공하여 XML 파일에서 View를 구성할 때 호출된다.
<br>

<img width="788" alt="image" src="https://user-images.githubusercontent.com/34837583/158223243-9fd0a3c9-4aaf-426b-9775-c6105281d09d.png">

View 계층 구조를 모두 전위순회하여 제약 조건을 정의하고 화면에 표시한다.<br>
Animate -> Measure -> Layout -> Draw 단계로 수행

- onMeasure() : View의 크기를 확인하기 위해 호출, ViewGroup의 경우 Child view에 대한 측정하고, 그 결과로 자신의 사이즈 결정
- onLayout() : View가 그려질 위치를 결정
- onDraw() : 크기와 위치가 이전에 계산되어 그려지므로 Canvas에 draw
- invalidate() : 뷰의 변경 사항을 보여주고자 할 때 강제가 Canvas를 뒤엎고 다시 그리기 요구하는 메소드
- requestLayout() : 뷰의 크기, 위치도 다시 측정이 필요할 때 호출하는 메소드

<br><br>

## MediaRecorder
MediaRecorder를 사용하여 다양한 일반 오디오 및 도영ㅇ상 포맷을 캡처하고 인코딩 지원
동영상 녹음을 위해서는 카메라를 사용해야 함.

- RECORD_AUDIO는 사용자 개인정보 보호로 인해 위험한 권한으로 간주되어 Android 6.0(API 23)부터는 런타임에 사용자 승인 요청
- prepare() : 초기화
- start() : 녹음 시작
- stop : 녹음 중지
- 참고(https://developer.android.com/guide/topics/media/mediarecorder?hl=ko)
```kotlin
	/**
         * MediaRecorder 초기화
         */
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)       // 오디오 소스 설정, 대게 MIC(마이크)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)       // 출력 포맷 설정, Android 8.0(API 26)부터는 스트리밍에 유용한 MPEG2_TS 포맷 지원
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)      // 오디오 인코더 설정 
            setOutputFile(recordingFilePath)        // 출력 파일 이름 설정
            prepare()       // prepare를 호춯하여야 초기화 완료 
        }
        recorder?.start()
```

<br><br>

## MediaPlayer
MediaPlayer를 이용하여 다양한 일반 미디어 유형의 재생을 지원
```kotlin
            /**
             * MediaPlayer 초기화
             */
            player = MediaPlayer().apply {
                setDataSource(recordingFilePath)
                prepare()
            }
```


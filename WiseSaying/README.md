## Firebase Remote Config
Firebase Remote Config는 사용자가 앱 업데이트를 다운로드할 필요 없이 앱의 동작과 모양을 변경할 수 있는 클라우드 서비스<br>
- 앱 사용자층에게 변경사항을 빠르게 적용
- 사용자층 특정 세그먼트에 앱 맞춤설정
- A/B 테스트를 실행하여 앱 개선
<br>

### 원격 구성 매개변수 및 조건
Firebase Consoleㄴ나 Remote Config REST API를  사용할 떄는 하나 이상의 매개변수(키-값 쌍)을 정의하고 해당 매개변수의 인앱 기본값을 제공합니다.<br>
이후 서버 측 매개변수 값을 정의하면 인앱 기본값을 재정의할 수 있습니다.
<br>

### 원격 구성으로 가능한 작업
- 비율 출시 메커니즘을 사용한 새 기능 출시
- 앱의 플랫폼 및 언어별 프로모션 배너 정의(실무에서 많이 사용)
- 제한된 테스트 그룹에서의 새 기능 테스트(A/B 테스트)
- JSON을 사용한 앱 또는 게임의 복잡한 항목 구성
<br>

### Firebase 원격 구성 로딩 전략
1. 로드 시 가져와 활성화 : UI 모양이 크게 변경되지 않은 곳에 적합
2. 로딩 화면 뒤에 활성화 : ProgressBar 사용, 너무 오랜 시간 걸릴경우 timeOut 지정
3. 다음 시작 시 새 값 로드 : 처음 실행 시 fetch, 두번째 activate 하는 방식, 최소 2번은 앱 실행해야 적용 가능
<br>

## ViewPager2
ViewPager2는 ViewPager 라이브러리 개선된 버전으로, 향상된 기능을 제공하여 ViewPager 사용시 발생하는 문제점 개선
- 세로 방향 지원
- 오른쪽에서 왼쪽 지원(RTL)
- 수정 가능한 프래그먼트 컬렉션 : 런타임 시 프래그먼트 컬렉션 동적으로 수정 가능
- DiffUtil : ViewPager2는 RecyclerView 기반으로 빌드되므로 DiffUtil 클래스 액세스 가능
<br>

### 어댑터 클래스 업데이트
- ViewPager PagerAdapter -> ViewPager2 RecyclerView.Adapter
- VIewPager FragmentPagerAdapter -> ViewPager2.FragmentStateAdapter
- ViewPager FragmentStatePagerAdapter - >ViewPager2.FragmentStateAdapter









## WebView
원하는 Web을 보여주는 위젯(뷰)으로 webViewClient,  webChromClient 설정
```kotlin
        webView.apply{
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true		// 기본적으로 자바스크립트 WebView는 사용 중지
            loadUrl(DEFAULT_URL)
        }
```
<br><br>

## WebViewClient
웹페이지가 로딩될 때 생기는 콜백 메소드로 구성된 클래스
```kotlin
    inner class WebViewClient: android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.isVisible = true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressBar.isVisible = false
            goBackButton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()
            addressBar.setText(url)
        }
    }
```
<br><br>

## WebChromeClient
웹페이지 내에서 일어나는 액션 처리를 위한 콜백 메소드가  구성된 클래스
```kotlin
    inner class WebChromeClient: android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar.progress = newProgress
        }
    }
```


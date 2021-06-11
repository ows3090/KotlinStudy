# Kotlin study

## Lotto App

1. ConstraintLayout
	- match_parent vs match_constraint(0dp)
	- Chain Style
2. NumberPicker
	- A widget that enables the user to select a number from a predefined range.
3. TextView
4. Button

<br>

## Secret Diary

1. ConstraintLayout
2. Handler
	- An Handler allows you to send and process Message and Runnable objects associated with a thread's MessageQueue.
	- to schedule messages and runnables to be executed at some point in the future.
	- to enqueue an action to be performed on a a different thread than your own.
3. SharedPreference
	- Interface for accessing and modifying preference data returned by Context.getSharedPreference.
	- preference must go though an Editor object to ensure the preference values remain in a consistent state.
4. Theme
5. AlertDialog
6. Intent
	- An intent in an abstract description of an operation to be performed.
	- startActivity to launch an Activity.
	- broadcastIntent to sent it to any interested BroadcastReceiver.
	- Context.startService, Context.bindService to communicate with a background service.

<br>

## Calculator

1. TableLayout, ConstraintLayout, LayoutInflater
	- LayoutInflator : Instantiates a layout XML files into its corresponding View objects.
2. Thread, RunOnUIThread
	- RunOnUIThread : Runs the specified action on the UI Thread.
3. Room
	- Dto, Database, Entity, Query
4. Kotlin Extension Function

<br>

## Electronic frame

1. Android Permission
	- Workflow for using permissions
	- rationale
2. View Animation
	- You can use the view animation system to perform tweened animation on Views.
	- Tween animation calculates the animation with information such as the start point, end point, size, rotation.
3. Acitivty Lifecycle
4. Content Provider ( Storage Access Framework )
	- The SAF makes it simple for users to browse and open documents, images, and ohter files.

<br>

## Timer

1. ConstraintLayout
2. CountDownTimer
	- Schedule a countdown until a time in the future.
3. SoundPool
	- The SoundPool class manages and plays audio resources for applications.

<br>

## Recorder

1. Request runtime permissions
	- Runtime permissions also known as dangerous permissions, give your app additional access to restricted data,
	and they allow your app to perform restricted actions.

2. CustomView
	- All of the view classes defined in the Androdi framework extend View
	- To allow Android Studio to interact with your view, you must provide a constructor that takes a Context and
	and an AttributesSet object as parameters. 
3. MediaRecorder
	- Used to recore audio and video.
	- MediaRecorder state diagram

<br>

## Webviewbrowser

1. ConstraintLayout
2. EditText
3. WebView

<br>

## Push Notification

1. Firebase cloud messaging
	- Token : Firebase creates a corresponding ID token that uniquely identifies them and grants them access to serveral resources.
	- To receive messages, use a service that extends FirebaseMessagingService
	- Notification Message : Only background state
	- Data Message : All state, Client App 
2. Notification 
	- Notification Channel : SDK 26, Oreo over
	- Normal, Expandable, Custom

<br>



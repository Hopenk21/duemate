# android-app

Minimal Kotlin Android app scaffolded by the assistant.

Prerequisites
- Android SDK + Android Studio or command-line SDK tools
- JDK 11+

Build (on Windows PowerShell)

```powershell
# generate the Gradle wrapper if you don't have it
gradle wrapper

# build debug APK
./gradlew assembleDebug

# build release AAB
./gradlew bundleRelease
```

To run on device/emulator:

```powershell
# install debug APK
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
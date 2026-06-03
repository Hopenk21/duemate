Param()

# Downloads a local Gradle distribution, generates the Gradle wrapper, builds the debug APK,
# and installs it to a connected device if `adb` is available.

Set-StrictMode -Version Latest

$ErrorActionPreference = 'Stop'

function Write-ErrAndExit([string]$msg) {
    Write-Host "ERROR: $msg" -ForegroundColor Red
    exit 1
}

# Check Java
try {
    & java -version 2>&1 | Out-Null
} catch {
    Write-ErrAndExit "Java not found. Install JDK 11+ and ensure `java` is on PATH."
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $scriptDir

$gradleVersion = '8.4'
$gradleZipUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
$toolsDir = Join-Path $scriptDir '..' | Resolve-Path -Relative | ForEach-Object { Join-Path $_ 'tools' }
if (-not (Test-Path $toolsDir)) { New-Item -ItemType Directory -Path $toolsDir | Out-Null }

$zipPath = Join-Path $env:TEMP "gradle-$gradleVersion.zip"
Write-Host "Downloading Gradle $gradleVersion..."
Invoke-WebRequest -Uri $gradleZipUrl -OutFile $zipPath

$extractDir = Join-Path $toolsDir "gradle-$gradleVersion"
if (Test-Path $extractDir) { Remove-Item -Recurse -Force $extractDir }
Write-Host "Extracting to $extractDir..."
Expand-Archive -Path $zipPath -DestinationPath $toolsDir -Force
Remove-Item $zipPath -Force

$gradleExec = Join-Path $extractDir "bin\gradle.bat"
if (-not (Test-Path $gradleExec)) { Write-ErrAndExit "Downloaded Gradle executable not found at $gradleExec" }

Write-Host "Generating Gradle wrapper..."
& $gradleExec wrapper --gradle-version $gradleVersion

if (-not (Test-Path "gradlew.bat")) {
    Write-ErrAndExit "Gradle wrapper generation failed; 'gradlew.bat' not found."
}

Write-Host "Building debug APK with Gradle wrapper..."
& .\gradlew.bat assembleDebug --no-daemon

$apkPath = Join-Path $scriptDir '..\\app\\build\\outputs\\apk\\debug\\app-debug.apk'
if (Test-Path $apkPath) {
    Write-Host "APK built at: $apkPath"
    try {
        & adb devices | Out-Null
        Write-Host "Installing APK to connected device..."
        & adb install -r $apkPath
        Write-Host "APK installed (if a device was connected)."
    } catch {
        Write-Host "`nNote: 'adb' not found or no device connected. You can manually install the APK:`n$apkPath"
    }
} else {
    Write-ErrAndExit "Build finished but APK not found at expected path: $apkPath"
}

Pop-Location

# Android / Retroid build

Builds the existing SwitchRecomp GLES 3.2 backend as an ARM64 NativeActivity APK,
with AAudio output, physical gamepad input, OpenH264 and libvpx video decoding.
Requires Android 9/API 28 or newer and OpenGL ES 3.2.

Use an original extracted **Metroid Dread 2.1.0** ExeFS containing `main`,
`main.npdm`, `rtld`, `sdk`, and `subsdk0`, plus its merged 2.1.0 RomFS.
The scripts verify the executable build ID, upstream code patch hashes, and
all RomFS file hashes. No game files or keys are downloaded by these scripts.

Prerequisites: Python 3, Git, make, a JDK with keytool, and Android SDK packages
`platform-tools`, `platforms;android-35`, `build-tools;35.0.0`,
`cmake;3.22.1`, `ndk;27.1.12297006`.
`ANDROID_HOME` defaults to Homebrew's `/opt/homebrew/share/android-commandlinetools`;
`ANDROID_NDK_HOME` can override the NDK path.

From the repository root:

```sh
python3 android/build.py --exefs /path/to/exefs --jobs 8
python3 android/deploy.py --serial YOUR_ADB_SERIAL --romfs /path/to/romfs
```

The first build fetches pinned codec sources and checksum-verifies the Khronos
OpenGL declarations. Generated C, assets and dependency builds live in ignored
`android/local/`; native objects live in ignored `android/build/`.
The signed personal APK is `android/MetroidDread.apk`. It contains your translated
game code and ExeFS, so keep it private. The local debug signing key is ignored.
Subsequent builds reuse generated sources and native objects. `--package-only`
repackages existing libraries. Subsequent deployment can omit `--romfs` to retain
already installed game data and saves. Do not uninstall the app when updating.

Game data, saves, settings, shader caches and logs are isolated in this app's
private `files/` directory. They do not touch emulator installations or saves.
For diagnostics:

```sh
adb -s YOUR_ADB_SERIAL logcat -s SwitchRecomp AndroidRuntime DEBUG
adb -s YOUR_ADB_SERIAL shell run-as com.switchrecomp.metroiddread cat files/MetroidDread.log
adb -s YOUR_ADB_SERIAL exec-out run-as com.switchrecomp.metroiddread cat files/MetroidDread.ini
```

Defaults use the 4:3 render-size patch (`docked=1`, `aspect=4:3`, 1200x900 base),
a 120 FPS cap, vsync, an on-screen FPS counter, and physical controls with the
touch overlay hidden. The cap is a target; gameplay can run slower depending on
the scene. Original cinematics can still run at 30 FPS. Change `aspect=16:9` and
`docked=0` for the original handheld picture. Aspect and docked changes require
a restart. Android face buttons are mapped by physical position to Switch
controls. Windows-specific front-end menus, DLSS and native Vulkan are not
included in this GLES build.

Validated on the Retroid Pocket Nova (Android 13, Adreno 740, 1280x960 at 120 Hz):
all 9,774 RomFS files passed on-device SHA-1 verification; the game loaded an
existing save, rendered 4:3 gameplay, accepted controls, and initialized AAudio
and VP9 playback. The owner's active gameplay experience averages in the 40s FPS.
The title screen reached 120 FPS, and mostly static scenes sometimes reached
roughly 100 FPS; those are not representative gameplay averages. This build
does not deliver sustained 120 FPS gameplay. See [Nova performance notes](PERFORMANCE.md)
for settings, evidence, and limitations of these observations.

# Retroid Pocket Nova observations

Tested on October 23, 2026 with Metroid Dread 2.1.0, based on upstream commit
`578f6ec` plus this Android port. The APK was cross-compiled on an Apple Silicon
Mac with NDK 27.1.12297006 and Clang 18, using `-O2`.

Device: Retroid Pocket Nova, Android 13/API 33, ARM64, Adreno 740, 1280x960 display
at 120 Hz. Renderer: Qualcomm OpenGL ES 3.2 driver V@0676.53, dated December 27,
2023. The native Vulkan renderer and frame generation are not part of this build.

| Workload | Observed performance | Evidence |
| --- | --- | --- |
| Active gameplay | Averages in the 40s FPS | Device owner's hands-on report; representative of their experience, not a computed benchmark mean |
| Recorded Omega Blaster sequence | About 25–26 FPS in sampled frames | 20-second device screen recording with visible FPS counter; recording overhead was not isolated |
| Title screen | Up to 120 FPS | Runtime log: `performance: 120.0 FPS` |
| Mostly static gameplay scene | Roughly 100 FPS in some samples | Runtime log samples of 103.5, 103.9, and 104.5 FPS; not representative of active gameplay |
| Original cinematics | Around 30 FPS | Original cinematic pacing retained; `cutscenes_60fps=0` |

The 4:3 run used `docked=1`, `aspect=4:3` (1200x900 base), `resolution=1`,
`fps_limit=120`, `forced_vsync=1`, and `show_fps=1`. Shadows were tested both on
and off (`shadows=0` applies live); the roughly 100 FPS samples occurred with
shadows off in a mostly static scene. A 120 FPS cap is a ceiling, not a claim
that the device sustains that rate.

These observations are from exploratory play, not a fixed-route benchmark or a
controlled comparison between shadow settings. Scene complexity, movement,
shader warm-up, power mode, and thermals were not controlled. Short idle-scene
samples must not be described as a 100 FPS gameplay average. Sustained 120 FPS
gameplay has not been achieved. Game-speed correctness above 60 FPS also has
not been systematically validated.

The PR's sample video is a 20-second, 1280x960 H.264 recording captured with
Android `screenrecord` at 2.5 Mbps, without audio. Its sampled frames show the
counter around 25–26 FPS during an Omega Blaster sequence. The recording's
nominal display capture rate must not be interpreted as the game's rendering
rate. Use the visible counter and the owner's active-play report instead.

The runtime's `GPU thread CPU busy` percentage describes host rendering-thread
CPU time, not hardware GPU utilization. It does not establish a hardware GPU
bottleneck on its own.

Functional checks passed: signed APK installation and update over ADB, all
9,774 RomFS files verified by SHA-1 on-device, 4:3 rendering, physical controller
input, AAudio initialization, VP9 playback, saving, and loading the saved game
after redeployment. Only this device has been tested; long-term stability,
other Android GPUs, audio route changes, and desktop regressions remain untested.

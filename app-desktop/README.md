# DroidKaigi 2025 JVM App :desktop_computer:

## Overview
`app-desktop` is the desktop client for the official DroidKaigi 2025 app.  
It is developed as a Kotlin/JVM application using JetBrains Compose Multiplatform.

## Prerequisites

| Item | Details |
| ---- | ------- |
| **JDK** | JDK **17** or later (set `JAVA_HOME` to JDK 17) |

### Example: Setting up JDK 17

1. **Install**

    - macOS: `brew install --cask temurin17`
    - Linux (Debian-based): `sudo apt install temurin-17-jdk`
    - Windows: `winget install EclipseAdoptium.Temurin.17.JDK`  
      (or download from [Adoptium](https://adoptium.net/temurin/releases/?version=17))

2. **Add to `.zprofile` (macOS)**

   ```shell
   export JAVA_HOME=$(/usr/libexec/java_home -v 17)
   export PATH="$JAVA_HOME/bin:$PATH"
   ```

## Gradle Commands for Running & Building

| Purpose                     | Command                                      | Notes                                                        |
| --------------------------- | -------------------------------------------- | ------------------------------------------------------------ |
| Run for development         | `./gradlew :app-desktop:run`                 |                                                              |
| Run the distributable       | `./gradlew :app-desktop:runDistributable`    | Use after `createDistributable`                              |
| Run with Hot Reload         | `./gradlew :app-desktop:hotRun --auto`       | Without `--auto`, file changes will not reload automatically |
| Create distributable bundle | `./gradlew :app-desktop:createDistributable` | Run before any packaging task                                |
| macOS DMG package           | `./gradlew :app-desktop:packageDmg`          | Run on the target OS                                         |
| Windows MSI package         | `./gradlew :app-desktop:packageMsi`          | Run on the target OS                                         |
| Windows EXE package         | `./gradlew :app-desktop:packageExe`          | Run on the target OS                                         |
| Linux DEB package           | `./gradlew :app-desktop:packageDeb`          | Run on the target OS                                         |
| Linux RPM package           | `./gradlew :app-desktop:packageRpm`          | Run on the target OS                                         |

> Run `createDistributable` before executing any OS-specific packaging task to generate the shared distribution.

## Location of Build Artifacts

```shell
app-desktop/
└── build/
    └── compose/
        └── binaries/
            └── main/
                ├── app/            ... output of createDistributable
                ├── dmg/            ... output of packageDmg
                ├── msi/            ... output of packageMsi
                ├── exe/            ... output of packageExe
                ├── deb/            ... output of packageDeb
                └── rpm/            ... output of packageRpm
```

## Additional Notes

- **Entry point**: `io.github.droidkaigi.confsched.MainKt`
- **Icons**: Place platform-specific icon files (`.icns`, `.ico`, `.png`) under `src/main/resources`
  - `.icns` generation example (uses `iconutil` from Xcode)

    ```shell
    mkdir -p DroidKaigi2025.iconset
    
    cp icon_16x16.png       DroidKaigi2025.iconset/icon_16x16.png
    cp icon_32x32.png       DroidKaigi2025.iconset/icon_16x16@2x.png
    
    cp icon_32x32.png       DroidKaigi2025.iconset/icon_32x32.png
    cp icon_64x64.png       DroidKaigi2025.iconset/icon_32x32@2x.png
    
    cp icon_128x128.png     DroidKaigi2025.iconset/icon_128x128.png
    cp icon_256x256.png     DroidKaigi2025.iconset/icon_128x128@2x.png
    
    cp icon_256x256.png     DroidKaigi2025.iconset/icon_256x256.png
    cp icon_512x512.png     DroidKaigi2025.iconset/icon_256x256@2x.png
    
    cp icon_512x512.png     DroidKaigi2025.iconset/icon_512x512.png
    cp icon_1024x1024.png   DroidKaigi2025.iconset/icon_512x512@2x.png
    
    iconutil -c icns DroidKaigi2025.iconset -o DroidKaigi2025.icns
    
    iconutil -c iconset DroidKaigi2025.icns -o check.iconset
    ls check.iconset
    ```
  - `.ico` generation example (using ImageMagick)

    ```shell
    magick icon_1024x1024.png -define icon:auto-resize=16,32,48,256 app.ico
    
    or
    
    magick icon_1024x1024.png \
    \( -clone 0 -resize 256x256 \) \
    \( -clone 0 -resize 48x48 \) \
    \( -clone 0 -resize 32x32 \) \
    \( -clone 0 -resize 16x16 \) \
    -delete 0 -alpha on -colorspace sRGB app.ico
    
    identify -format "%f %wx%h\n" app.ico
    ```

- **License information**: The `aboutLibraries` plugin generates `licenses.json`, which is included in the distributable.
- **Package name & version**: `DroidKaigi 2025` / follows the `droidkaigiApp` version in libs.versions.toml

#!/bin/sh

# ci_post_clone.sh
# Xcode Cloud post-clone script for DroidKaigi 2025 iOS app
# This script runs after the repository is cloned

set -e

echo "============================"
echo "Starting Post-Clone Script"
echo "============================"

# Print environment info
echo "Current directory: $(pwd)"
echo "CI Primary Repository Path: $CI_PRIMARY_REPOSITORY_PATH"

# Navigate to repository root
cd "$CI_PRIMARY_REPOSITORY_PATH"

# Verify gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "❌ Error: gradlew not found in repository root"
    echo "Current directory: $(pwd)"
    ls -la
    exit 1
fi

echo "Repository root: $CI_PRIMARY_REPOSITORY_PATH"

# Install Java (required for Gradle)
echo "Installing Java..."
brew install openjdk@17
export JAVA_HOME="$(brew --prefix openjdk@17)"
export PATH="$JAVA_HOME/bin:$PATH"
echo "Java version: $(java -version 2>&1 | head -n 1)"

# Setup Gradle
echo "Setting up Gradle..."
chmod +x ./gradlew

# Set JVM options
export GRADLE_OPTS="-Xmx6g -XX:MaxMetaspaceSize=3g -Dfile.encoding=UTF-8"
echo "GRADLE_OPTS: $GRADLE_OPTS"

# Build XCFramework for the shared module
echo "============================"
echo "Building XCFramework"
echo "============================"

# Determine build configuration based on CI action
if [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Building XCFramework for distribution (Release configuration)..."
    ./gradlew app-shared:assembleSharedReleaseXCFramework -Papp.ios.shared.arch=arm64 --no-configuration-cache --refresh-dependencies --no-parallel --stacktrace
else
    echo "Building XCFramework for development/testing (Debug configuration)..."
    ./gradlew app-shared:assembleSharedDebugXCFramework -Papp.ios.shared.arch=arm64 --no-configuration-cache --refresh-dependencies --no-parallel --stacktrace
fi

# Verify XCFramework output
echo "============================"
echo "Verifying XCFramework output..."
echo "============================"

XCFRAMEWORK_FOUND=false
for dir in "app-shared/build/XCFrameworks" "app-shared/build/bin" "app-shared/build"; do
    if [ -d "$dir" ]; then
        FOUND_XCFRAMEWORK=$(find "$dir" -name "*.xcframework" -type d -print -quit 2>/dev/null)
        if [ -n "$FOUND_XCFRAMEWORK" ]; then
            XCFRAMEWORK_FOUND=true
            echo "✅ Found XCFramework at: $FOUND_XCFRAMEWORK"
            break
        fi
    fi
done

if [ "$XCFRAMEWORK_FOUND" = false ]; then
    echo "❌ XCFramework not found after build"
    echo "Searching entire project for .xcframework:"
    find . -name "*.xcframework" -type d 2>/dev/null | head -20
    exit 1
fi

echo "============================"
echo "Post-Clone Script Completed"
echo "============================"

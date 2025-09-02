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

# Set JVM options with SSL/TLS configuration
export GRADLE_OPTS="-Xmx6g -XX:MaxMetaspaceSize=3g -Dfile.encoding=UTF-8 -Djavax.net.ssl.trustStoreType=JKS -Dhttps.protocols=TLSv1.2,TLSv1.3"
echo "GRADLE_OPTS: $GRADLE_OPTS"

# Set Gradle properties for better network handling
export GRADLE_USER_HOME="${CI_PRIMARY_REPOSITORY_PATH}/.gradle"
mkdir -p "$GRADLE_USER_HOME"
echo "org.gradle.daemon=false" >> "$GRADLE_USER_HOME/gradle.properties"
echo "org.gradle.parallel=false" >> "$GRADLE_USER_HOME/gradle.properties"
echo "systemProp.http.connectionTimeout=120000" >> "$GRADLE_USER_HOME/gradle.properties"
echo "systemProp.http.socketTimeout=120000" >> "$GRADLE_USER_HOME/gradle.properties"
echo "systemProp.https.connectionTimeout=120000" >> "$GRADLE_USER_HOME/gradle.properties"
echo "systemProp.https.socketTimeout=120000" >> "$GRADLE_USER_HOME/gradle.properties"

# Build XCFramework for the shared module
echo "============================"
echo "Building XCFramework"
echo "============================"

# Function to retry Gradle build with exponential backoff
run_gradle_with_retry() {
    local max_attempts=3
    local attempt=1
    local wait_time=10
    
    while [ $attempt -le $max_attempts ]; do
        echo "Attempt $attempt of $max_attempts..."
        
        if eval "$1"; then
            echo "✅ Gradle build succeeded on attempt $attempt"
            return 0
        else
            if [ $attempt -lt $max_attempts ]; then
                echo "⚠️ Gradle build failed on attempt $attempt, retrying in ${wait_time} seconds..."
                sleep $wait_time
                wait_time=$((wait_time * 2))
                attempt=$((attempt + 1))
            else
                echo "❌ Gradle build failed after $max_attempts attempts"
                return 1
            fi
        fi
    done
}

# Determine build configuration based on CI action
if [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Building XCFramework for distribution (Release configuration)..."
    run_gradle_with_retry "./gradlew app-shared:assembleSharedReleaseXCFramework -Papp.ios.shared.arch=arm64 --no-configuration-cache --refresh-dependencies --no-parallel --stacktrace"
else
    echo "Building XCFramework for development/testing (Debug configuration)..."
    run_gradle_with_retry "./gradlew app-shared:assembleSharedDebugXCFramework -Papp.ios.shared.arch=arm64 --no-configuration-cache --refresh-dependencies --no-parallel --stacktrace"
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

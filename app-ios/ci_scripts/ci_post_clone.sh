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
echo "CI Workspace Path: $CI_WORKSPACE_PATH"
echo "CI Project File Path: $CI_PROJECT_FILE_PATH"

# Navigate to repository root
# CI_PRIMARY_REPOSITORY_PATH points to /Volumes/workspace/repository
cd "$CI_PRIMARY_REPOSITORY_PATH"
REPO_ROOT="$CI_PRIMARY_REPOSITORY_PATH"

# Verify gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "❌ Error: gradlew not found in repository root"
    echo "Current directory: $(pwd)"
    echo "Directory contents:"
    ls -la
    exit 1
fi

echo "Repository root: $REPO_ROOT"

# Install Java (required for Gradle)
echo "Installing Java..."
brew install openjdk@17
export JAVA_HOME="$(brew --prefix openjdk@17)"
export PATH="$JAVA_HOME/bin:$PATH"
echo "Java version: $(java -version 2>&1 | head -n 1)"

# Setup Gradle
echo "Setting up Gradle..."
if [ -f "./gradlew" ]; then
    chmod +x ./gradlew
    echo "✅ Gradle wrapper is ready"
else
    echo "❌ Gradle wrapper not found"
    exit 1
fi

# Verify Gradle installation
if ! ./gradlew --version; then
    echo "❌ Error: Gradle verification failed"
    exit 1
fi

# Cache Gradle dependencies
echo "Downloading Gradle dependencies..."
./gradlew :app-shared:dependencies || true

# Install SwiftLint for code quality checks
echo "Installing SwiftLint..."
brew install swiftlint || true
swiftlint --version || echo "SwiftLint installation skipped"

# Install SwiftFormat for code formatting
echo "Installing SwiftFormat..."
brew install swiftformat || true
swiftformat --version || echo "SwiftFormat installation skipped"

# Create necessary directories
echo "Creating build directories..."
mkdir -p app-shared/build/XCFrameworks

# Set up environment variables
echo "Setting up environment variables..."
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8

# Build XCFramework for the shared module
echo "============================"
echo "Building XCFramework"
echo "============================"

# Clean ALL Gradle caches to force fresh downloads
echo "Cleaning ALL Gradle caches..."
rm -rf ~/.gradle/caches
rm -rf "$CI_PRIMARY_REPOSITORY_PATH/.gradle"
rm -rf "$CI_PRIMARY_REPOSITORY_PATH/.gradle-cache"

# Copy CI-specific Gradle properties (MUST be before any Gradle command)
echo "Setting up Gradle CI properties..."
cp "$CI_PRIMARY_REPOSITORY_PATH/app-ios/ci_scripts/gradle_ci.properties" "$CI_PRIMARY_REPOSITORY_PATH/gradle.properties"
echo "gradle.properties content:"
cat "$CI_PRIMARY_REPOSITORY_PATH/gradle.properties"

# Set conservative JVM options
export GRADLE_OPTS="-Xmx6g -XX:MaxMetaspaceSize=2g -Dfile.encoding=UTF-8"
echo "GRADLE_OPTS: $GRADLE_OPTS"

# Configure Java to work around Xcode Cloud networking issues
export JAVA_TOOL_OPTIONS="-Djava.net.preferIPv4Stack=true -Dhttp.keepAlive=false -Dhttp.maxConnections=1"
echo "JAVA_TOOL_OPTIONS: $JAVA_TOOL_OPTIONS"

# Skip dependency pre-download - go directly to build
echo "Skipping dependency pre-download to avoid connection issues..."

# Determine build configuration based on CI action
# Note: Simplified flags - most settings are in gradle.properties
if [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Building XCFramework for distribution (Release configuration)..."
    if ! ./gradlew :app-shared:assembleSharedReleaseXCFramework \
        --no-daemon \
        --no-parallel \
        --no-configuration-cache \
        --offline \
        --stacktrace; then
        echo "❌ XCFramework build failed for Release configuration"
        echo "Trying again without --offline flag..."
        # Fallback without offline mode
        ./gradlew :app-shared:assembleSharedReleaseXCFramework \
            --no-daemon \
            --no-parallel \
            --no-configuration-cache \
            --stacktrace || exit 1
    fi
else
    echo "Building XCFramework for development/testing (Debug configuration)..."
    if ! ./gradlew :app-shared:assembleSharedDebugXCFramework \
        --no-daemon \
        --no-parallel \
        --no-configuration-cache \
        --offline \
        --stacktrace; then
        echo "❌ XCFramework build failed for Debug configuration"
        echo "Trying again without --offline flag..."
        # Fallback without offline mode
        ./gradlew :app-shared:assembleSharedDebugXCFramework \
            --no-daemon \
            --no-parallel \
            --no-configuration-cache \
            --stacktrace || exit 1
    fi
fi

# Debug: Check build output
echo "============================"
echo "Verifying XCFramework output..."
echo "============================"

echo "Current directory: $(pwd)"
echo "Checking app-shared/build structure:"
if [ -d "app-shared/build" ]; then
    echo "Contents of app-shared/build:"
    ls -la app-shared/build/
    
    # Search for XCFramework in various locations
    echo "Searching for .xcframework files:"
    find app-shared/build -name "*.xcframework" -type d 2>/dev/null || echo "No .xcframework found in app-shared/build"
fi

# Check common output locations
XCFRAMEWORK_FOUND=false
XCFRAMEWORK_PATH=""

# Check multiple possible locations
for dir in "app-shared/build/XCFrameworks" "app-shared/build/bin" "app-shared/build"; do
    if [ -d "$dir" ]; then
        FOUND_XCFRAMEWORK=$(find "$dir" -name "*.xcframework" -type d -print -quit 2>/dev/null)
        if [ -n "$FOUND_XCFRAMEWORK" ]; then
            XCFRAMEWORK_FOUND=true
            XCFRAMEWORK_PATH=$(dirname "$FOUND_XCFRAMEWORK")
            echo "✅ Found XCFramework at: $FOUND_XCFRAMEWORK"
            break
        fi
    fi
done

if [ "$XCFRAMEWORK_FOUND" = true ]; then
    echo "✅ XCFramework built successfully"
    echo "XCFramework location: $XCFRAMEWORK_PATH"
    ls -la "$XCFRAMEWORK_PATH/"
else
    echo "❌ XCFramework not found after build"
    echo "Searching entire project for .xcframework:"
    find . -name "*.xcframework" -type d 2>/dev/null | head -20
    exit 1
fi

echo "============================"
echo "Post-Clone Script Completed"
echo "============================"
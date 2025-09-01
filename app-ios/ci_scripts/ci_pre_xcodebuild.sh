#!/bin/sh

# ci_pre_xcodebuild.sh
# Xcode Cloud pre-build script for DroidKaigi 2025 iOS app
# This script runs before the Xcode build phase

set -e

echo "============================"
echo "Starting Pre-Build Script"
echo "============================"

# Print environment info
echo "Current directory: $(pwd)"
echo "Xcode version: $(xcodebuild -version)"
echo "Swift version: $(swift --version)"

# Navigate to repository root
cd "$CI_WORKSPACE"
echo "CI Workspace: $CI_WORKSPACE"

# Build XCFramework for the shared module
echo "Building XCFramework..."
if [ "$CI_XCODEBUILD_ACTION" = "build-for-testing" ] || [ "$CI_XCODEBUILD_ACTION" = "test-without-building" ]; then
    echo "Building XCFramework for testing (Debug configuration)..."
    ./gradlew :app-shared:assembleSharedDebugXCFramework \
        -Papp.ios.shared.arch=arm64SimulatorDebug \
        --stacktrace
elif [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Building XCFramework for distribution (Release configuration)..."
    ./gradlew :app-shared:assembleSharedReleaseXCFramework \
        -Papp.ios.shared.arch=arm64 \
        --stacktrace
else
    echo "Building XCFramework for general build..."
    ./gradlew :app-shared:assembleSharedDebugXCFramework \
        -Papp.ios.shared.arch=arm64SimulatorDebug \
        --stacktrace
fi

# Verify XCFramework was created
if [ -d "app-shared/build/XCFrameworks" ]; then
    echo "✅ XCFramework built successfully"
    ls -la app-shared/build/XCFrameworks/
else
    echo "❌ XCFramework build failed"
    exit 1
fi

# Setup environment variables for build configuration
if [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Setting up production environment..."
    # Add any production-specific setup here
    export APP_ENVIRONMENT="production"
else
    echo "Setting up development environment..."
    export APP_ENVIRONMENT="development"
fi

# Install Swift Package Manager dependencies
echo "Resolving SPM dependencies..."
cd "$CI_WORKSPACE/app-ios"
xcodebuild -resolvePackageDependencies \
    -project DroidKaigi2025.xcodeproj \
    -scheme DroidKaigi2025 \
    -derivedDataPath "$CI_DERIVED_DATA_PATH"

echo "============================"
echo "Pre-Build Script Completed"
echo "============================"
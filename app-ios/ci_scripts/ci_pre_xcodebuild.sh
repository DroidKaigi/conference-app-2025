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

# Build XCFramework for the shared module
echo "Building XCFramework..."
if [ "$CI_XCODEBUILD_ACTION" = "build-for-testing" ] || [ "$CI_XCODEBUILD_ACTION" = "test-without-building" ]; then
    echo "Building XCFramework for testing (Debug configuration)..."
    if ! ./gradlew :app-shared:assembleSharedDebugXCFramework --stacktrace; then
        echo "❌ XCFramework build failed for Debug configuration"
        exit 1
    fi
elif [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Building XCFramework for distribution (Release configuration)..."
    if ! ./gradlew :app-shared:assembleSharedReleaseXCFramework --stacktrace; then
        echo "❌ XCFramework build failed for Release configuration"
        exit 1
    fi
else
    echo "Building XCFramework for general build (Debug)..."
    if ! ./gradlew :app-shared:assembleSharedDebugXCFramework --stacktrace; then
        echo "❌ XCFramework build failed for general build"
        exit 1
    fi
fi

# Debug: Check build directory structure
echo "============================"
echo "Checking build output..."
echo "============================"

echo "Current directory: $(pwd)"
echo "Checking for app-shared directory:"
if [ -d "app-shared" ]; then
    echo "✅ app-shared directory exists"
    echo "Contents of app-shared:"
    ls -la app-shared/
    
    if [ -d "app-shared/build" ]; then
        echo "✅ app-shared/build directory exists"
        echo "Contents of app-shared/build:"
        ls -la app-shared/build/
        
        # Check for various possible XCFramework locations
        if [ -d "app-shared/build/XCFrameworks" ]; then
            echo "✅ XCFramework directory found at app-shared/build/XCFrameworks"
            echo "Contents:"
            ls -la app-shared/build/XCFrameworks/
            find app-shared/build/XCFrameworks -name "*.xcframework" -type d
        elif [ -d "app-shared/build/bin" ]; then
            echo "ℹ️ Checking app-shared/build/bin directory:"
            ls -la app-shared/build/bin/
            find app-shared/build/bin -name "*.xcframework" -type d
        fi
        
        # Search for any .xcframework in build directory
        echo "Searching for .xcframework files in build directory:"
        find app-shared/build -name "*.xcframework" -type d 2>/dev/null || echo "No .xcframework found"
    else
        echo "❌ app-shared/build directory does not exist"
    fi
else
    echo "❌ app-shared directory does not exist"
fi

# Also check if XCFramework exists in other common locations
echo "============================"
echo "Searching for XCFramework in common locations..."
echo "============================"
find . -name "*.xcframework" -type d -maxdepth 4 2>/dev/null | head -10

# Verify XCFramework was created (with more flexible checking)
XCFRAMEWORK_FOUND=false
if [ -d "app-shared/build/XCFrameworks" ] && [ "$(ls -A app-shared/build/XCFrameworks 2>/dev/null)" ]; then
    XCFRAMEWORK_FOUND=true
    XCFRAMEWORK_PATH="app-shared/build/XCFrameworks"
elif [ -d "app-shared/build/bin" ]; then
    # Check if XCFramework is in bin directory
    if find app-shared/build/bin -name "*.xcframework" -type d -quit 2>/dev/null; then
        XCFRAMEWORK_FOUND=true
        XCFRAMEWORK_PATH="app-shared/build/bin"
    fi
fi

if [ "$XCFRAMEWORK_FOUND" = true ]; then
    echo "✅ XCFramework built successfully"
    echo "XCFramework location: $XCFRAMEWORK_PATH"
    ls -la "$XCFRAMEWORK_PATH/"
else
    echo "❌ XCFramework build failed - no .xcframework found"
    echo "Build may have succeeded but output is in unexpected location"
    echo "Checking gradle build output for clues:"
    if [ -f "app-shared/build/reports/configuration.txt" ]; then
        cat app-shared/build/reports/configuration.txt
    fi
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
# Navigate to app-ios directory from repository root
cd "$REPO_ROOT/app-ios"
xcodebuild -resolvePackageDependencies \
    -project DroidKaigi2025.xcodeproj \
    -scheme DroidKaigi2025 \
    -derivedDataPath "$CI_DERIVED_DATA_PATH"

echo "============================"
echo "Pre-Build Script Completed"
echo "============================"
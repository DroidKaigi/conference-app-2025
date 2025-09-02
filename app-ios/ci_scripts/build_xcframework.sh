#!/bin/sh

# build_xcframework.sh
# Script to build XCFramework for shared module
# Used by Xcode Cloud and local builds

set -e

echo "============================"
echo "Building XCFramework"
echo "============================"

# Configuration
BUILD_CONFIG="${1:-Debug}"
ARCHITECTURE="${2:-arm64SimulatorDebug}"

echo "Build Configuration: $BUILD_CONFIG"
echo "Architecture: $ARCHITECTURE"

# Navigate to project root
if [ -n "$CI_PRIMARY_REPOSITORY_PATH" ]; then
    # CI environment - use CI_PRIMARY_REPOSITORY_PATH
    cd "$CI_PRIMARY_REPOSITORY_PATH"
    echo "Using CI repository path: $CI_PRIMARY_REPOSITORY_PATH"
else
    # Local build - go to repository root
    SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
    cd "$SCRIPT_DIR/../.."
    echo "Using local repository path"
fi

echo "Working directory: $(pwd)"

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "❌ Error: gradlew not found in $(pwd)"
    exit 1
fi

# Make gradlew executable
chmod +x ./gradlew

# Clean previous builds
echo "Cleaning previous XCFramework builds..."
rm -rf app-shared/build/XCFrameworks

# Build XCFramework based on configuration
if [ "$BUILD_CONFIG" = "Release" ]; then
    echo "Building Release XCFramework..."
    if ! ./gradlew :app-shared:assembleSharedReleaseXCFramework --stacktrace; then
        echo "❌ XCFramework build failed for Release configuration"
        echo "Build command: ./gradlew :app-shared:assembleSharedReleaseXCFramework"
        exit 1
    fi
else
    echo "Building Debug XCFramework..."
    if ! ./gradlew :app-shared:assembleSharedDebugXCFramework --stacktrace; then
        echo "❌ XCFramework build failed for Debug configuration"
        echo "Build command: ./gradlew :app-shared:assembleSharedDebugXCFramework"
        exit 1
    fi
fi

# Debug: Check build output
echo "============================"
echo "Checking build output..."
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
    echo "❌ XCFramework build failed - no .xcframework found"
    echo "Checking all build outputs:"
    find . -name "*.xcframework" -type d 2>/dev/null | head -20
    exit 1
fi

# Create a symlink for easier access (optional)
if [ -d "app-ios" ]; then
    XCFRAMEWORK_LINK="app-ios/XCFrameworks"
    if [ -L "$XCFRAMEWORK_LINK" ] || [ -d "$XCFRAMEWORK_LINK" ]; then
        rm -rf "$XCFRAMEWORK_LINK"
    fi
    ln -s "../app-shared/build/XCFrameworks" "$XCFRAMEWORK_LINK"
    echo "Created symlink at: $XCFRAMEWORK_LINK"
fi

echo "============================"
echo "XCFramework Build Complete"
echo "============================"
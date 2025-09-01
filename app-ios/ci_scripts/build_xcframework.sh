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
    ./gradlew :app-shared:assembleSharedReleaseXCFramework \
        -Papp.ios.shared.arch=arm64 \
        --stacktrace
else
    echo "Building Debug XCFramework..."
    ./gradlew :app-shared:assembleSharedDebugXCFramework \
        -Papp.ios.shared.arch="$ARCHITECTURE" \
        --stacktrace
fi

# Verify build output
if [ -d "app-shared/build/XCFrameworks" ]; then
    echo "✅ XCFramework built successfully"
    echo "Contents:"
    ls -la app-shared/build/XCFrameworks/
else
    echo "❌ XCFramework build failed - directory not found"
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
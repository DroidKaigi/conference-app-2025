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

echo "Repository root: $REPO_ROOT"

# Verify XCFramework exists (should have been built in post_clone)
echo "============================"
echo "Verifying XCFramework exists"
echo "============================"

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
    echo "✅ XCFramework verified successfully"
    echo "XCFramework location: $XCFRAMEWORK_PATH"
    ls -la "$XCFRAMEWORK_PATH/"
else
    echo "❌ XCFramework not found - it should have been built in post_clone phase"
    echo "Searching for .xcframework in entire project:"
    find . -name "*.xcframework" -type d 2>/dev/null | head -20
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
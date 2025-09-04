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

# Navigate to iOS app directory
cd "$REPO_ROOT/app-ios"

# Enable skip plugin validation (fix typo in key name)
defaults write com.apple.dt.Xcode IDESkipPackagePluginFingerprintValidation -bool YES

# Enable macro execution without prompting
defaults write com.apple.dt.Xcode IDESkipMacroFingerprintValidation -bool YES

# Trust all macros in CI environment
defaults write com.apple.dt.Xcode IDEMacroExpansionBuildEverything -bool YES

# Create Xcode preferences directory if it doesn't exist
mkdir -p ~/Library/Developer/Xcode

# Enable macros for swift-dependencies package specifically
xcodebuild -skipMacroValidation -skipPackagePluginValidation || true

# Pre-resolve package dependencies to ensure macros are registered
echo "Pre-resolving package dependencies..."
xcodebuild -resolvePackageDependencies -project "$REPO_ROOT/app-ios/DroidKaigi2025.xcodeproj" -scheme DroidKaigi2025 -skipMacroValidation || true

echo "============================"
echo "Pre-Build Script Completed"
echo "============================"

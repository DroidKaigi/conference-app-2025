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

# Enable all plugin and macro validations to be skipped
echo "Configuring Xcode to skip all plugin and macro validations..."
defaults write com.apple.dt.Xcode IDESkipPackagePluginFingerprintValidation -bool YES
defaults write com.apple.dt.Xcode IDESkipMacroFingerprintValidation -bool YES
defaults write com.apple.dt.Xcode IDEMacroExpansionBuildEverything -bool YES
defaults write com.apple.dt.Xcode IDEPackageOnlyUseVersionsFromResolvedFile -bool NO

# Create Xcode preferences directory if it doesn't exist
mkdir -p ~/Library/Developer/Xcode

# Set environment variables for build
export SKIP_MACRO_VALIDATION=YES
export SKIP_PACKAGE_PLUGIN_VALIDATION=YES

# Pre-resolve package dependencies to ensure all plugins and macros are registered
echo "Pre-resolving package dependencies with all validations skipped..."
xcodebuild -resolvePackageDependencies \
    -project "$REPO_ROOT/app-ios/DroidKaigi2025.xcodeproj" \
    -scheme DroidKaigi2025 \
    -skipMacroValidation \
    -skipPackagePluginValidation \
    -allowProvisioningUpdates \
    -disableAutomaticPackageResolution \
    CODE_SIGN_IDENTITY="" \
    CODE_SIGNING_REQUIRED=NO \
    CODE_SIGNING_ALLOWED=NO || true

echo "Package dependencies resolved."

echo "============================"
echo "Pre-Build Script Completed"
echo "============================"

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

echo "============================"
echo "Post-Clone Script Completed"
echo "============================"
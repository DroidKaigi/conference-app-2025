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
echo "CI Workspace: $CI_WORKSPACE"

# Navigate to repository root
cd "$CI_WORKSPACE"

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
./gradlew --version

# Cache Gradle dependencies
echo "Downloading Gradle dependencies..."
./gradlew :app-shared:dependencies --configuration=sharedDebugFramework || true

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
#!/bin/sh

# ci_post_xcodebuild.sh
# Xcode Cloud post-build script for DroidKaigi 2025 iOS app
# This script runs after the Xcode build phase

set -e

echo "============================"
echo "Starting Post-Build Script"
echo "============================"

# Print build info
echo "Build action: $CI_XCODEBUILD_ACTION"
echo "Product: $CI_PRODUCT"
echo "Archive path: $CI_ARCHIVE_PATH"

# Run tests if this is a test build
if [ "$CI_XCODEBUILD_ACTION" = "test-without-building" ] || [ "$CI_XCODEBUILD_ACTION" = "build-for-testing" ]; then
    echo "Running post-build tests..."
    
    # Run SwiftLint checks
    if command -v swiftlint &> /dev/null; then
        echo "Running SwiftLint..."
        cd "$CI_WORKSPACE/app-ios"
        swiftlint lint --reporter json > swiftlint-report.json || true
        
        # Count violations
        VIOLATIONS=$(cat swiftlint-report.json | grep -c '"severity":"error"' || echo "0")
        if [ "$VIOLATIONS" -gt "0" ]; then
            echo "⚠️ Found $VIOLATIONS SwiftLint errors"
            # Don't fail the build for linting issues in test builds
        else
            echo "✅ SwiftLint passed"
        fi
    fi
fi

# Process archive for distribution
if [ "$CI_XCODEBUILD_ACTION" = "archive" ]; then
    echo "Processing archive for distribution..."
    
    # Verify archive was created
    if [ -d "$CI_ARCHIVE_PATH" ]; then
        echo "✅ Archive created successfully at: $CI_ARCHIVE_PATH"
        
        # List archive contents
        echo "Archive contents:"
        ls -la "$CI_ARCHIVE_PATH"
        
        # Create a backup of the archive
        ARCHIVE_BACKUP_DIR="$CI_WORKSPACE/archive-backup"
        mkdir -p "$ARCHIVE_BACKUP_DIR"
        cp -R "$CI_ARCHIVE_PATH" "$ARCHIVE_BACKUP_DIR/"
        echo "Archive backed up to: $ARCHIVE_BACKUP_DIR"
        
        # Add build metadata
        BUILD_INFO_FILE="$CI_ARCHIVE_PATH/build-info.json"
        cat > "$BUILD_INFO_FILE" <<EOF
{
    "build_number": "$CI_BUILD_NUMBER",
    "build_date": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")",
    "git_branch": "$CI_BRANCH",
    "git_commit": "$CI_COMMIT",
    "xcode_version": "$CI_XCODE_VERSION",
    "workflow": "$CI_WORKFLOW"
}
EOF
        echo "Build metadata saved to: $BUILD_INFO_FILE"
        
    else
        echo "❌ Archive not found at expected path: $CI_ARCHIVE_PATH"
        exit 1
    fi
fi

# Clean up temporary files
echo "Cleaning up temporary files..."
if [ -d "$CI_DERIVED_DATA_PATH/Logs" ]; then
    echo "Compressing build logs..."
    tar -czf "$CI_WORKSPACE/build-logs.tar.gz" -C "$CI_DERIVED_DATA_PATH" Logs
    echo "Build logs compressed to: $CI_WORKSPACE/build-logs.tar.gz"
fi

# Generate build report
BUILD_REPORT="$CI_WORKSPACE/build-report.txt"
cat > "$BUILD_REPORT" <<EOF
DroidKaigi 2025 iOS Build Report
================================
Build Date: $(date)
Build Number: $CI_BUILD_NUMBER
Branch: $CI_BRANCH
Commit: $CI_COMMIT
Build Action: $CI_XCODEBUILD_ACTION
Xcode Version: $CI_XCODE_VERSION
Product: $CI_PRODUCT

Build Status: SUCCESS
================================
EOF

echo "Build report saved to: $BUILD_REPORT"

# Send notifications (if configured)
if [ -n "$SLACK_WEBHOOK_URL" ]; then
    echo "Sending build notification to Slack..."
    curl -X POST -H 'Content-type: application/json' \
        --data "{\"text\":\"✅ iOS build #$CI_BUILD_NUMBER completed successfully on branch $CI_BRANCH\"}" \
        "$SLACK_WEBHOOK_URL" || echo "Slack notification failed (non-critical)"
fi

echo "============================"
echo "Post-Build Script Completed"
echo "============================"
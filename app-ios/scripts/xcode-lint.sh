#!/bin/bash

# Xcode Build Phase Lint Script for DroidKaigi 2025 iOS App
# This script runs SwiftLint only on Swift files that have been modified in Git
# Designed to be used as a Run Script Build Phase in Xcode

set -e

# Check if we're in Xcode build environment
if [ -z "$SRCROOT" ]; then
    echo "error: This script should be run as an Xcode Build Phase"
    exit 1
fi

# Skip linting in CI environment
if [ -n "$CI" ] || [ -n "$GITHUB_ACTIONS" ]; then
    echo "Skipping SwiftLint in CI environment"
    exit 0
fi

# Path to SwiftLint - check multiple possible locations
SWIFTLINT_PATH=""

# 1. Check if SwiftLint is in .nest (project's local installation)
if [ -f "$SRCROOT/.nest/bin/swiftlint" ]; then
    SWIFTLINT_PATH="$SRCROOT/.nest/bin/swiftlint"
# 2. Check if SwiftLint is available via mint
elif command -v mint &> /dev/null && mint which swiftlint &> /dev/null; then
    SWIFTLINT_PATH="$(mint which swiftlint)"
# 3. Check if SwiftLint is in PATH
elif command -v swiftlint &> /dev/null; then
    SWIFTLINT_PATH="$(command -v swiftlint)"
# 4. Check common Homebrew locations
elif [ -f "/opt/homebrew/bin/swiftlint" ]; then
    SWIFTLINT_PATH="/opt/homebrew/bin/swiftlint"
elif [ -f "/usr/local/bin/swiftlint" ]; then
    SWIFTLINT_PATH="/usr/local/bin/swiftlint"
fi

# If SwiftLint is not found, provide helpful error message
if [ -z "$SWIFTLINT_PATH" ] || [ ! -f "$SWIFTLINT_PATH" ]; then
    echo "warning: SwiftLint not found. Please install SwiftLint:"
    echo "  • Run 'make setup' in the project root"
    echo "  • Or install via Homebrew: brew install swiftlint"
    echo "  • Or install via Mint: mint install realm/SwiftLint"
    exit 0  # Exit with success to not block builds
fi

echo "Using SwiftLint at: $SWIFTLINT_PATH"

# Move to project root
cd "$SRCROOT"

# Get list of modified Swift files (staged, unstaged, and untracked)
# This covers all files that would show up in 'git status'
MODIFIED_FILES=""

# Get staged files
STAGED_FILES=$(git diff --cached --name-only --diff-filter=ACMR | grep "\.swift$" || true)

# Get unstaged modified files
UNSTAGED_FILES=$(git diff --name-only --diff-filter=ACMR | grep "\.swift$" || true)

# Get untracked Swift files (new files not yet added to git)
UNTRACKED_FILES=$(git ls-files --others --exclude-standard | grep "\.swift$" || true)

# Combine all files and remove duplicates
ALL_MODIFIED_FILES=$(echo -e "$STAGED_FILES\n$UNSTAGED_FILES\n$UNTRACKED_FILES" | sort -u | grep -v "^$" || true)

# Filter files to only include those in Core, Native, and App directories
FILTERED_FILES=""
while IFS= read -r file; do
    if [[ "$file" == Core/* ]] || [[ "$file" == Native/* ]] || [[ "$file" == App/* ]]; then
        # Check if file exists and is not in excluded paths
        if [ -f "$file" ] && \
           [[ "$file" != *"/.build/"* ]] && \
           [[ "$file" != *"/Package.swift" ]] && \
           [[ "$file" != *"/KMPFramework/"* ]] && \
           [[ "$file" != *"/DerivedData/"* ]]; then
            FILTERED_FILES="$FILTERED_FILES$file\n"
        fi
    fi
done <<< "$ALL_MODIFIED_FILES"

# Remove trailing newline and empty lines
FILTERED_FILES=$(echo -e "$FILTERED_FILES" | grep -v "^$" || true)

# Count number of files to lint
FILE_COUNT=$(echo -e "$FILTERED_FILES" | grep -v "^$" | wc -l | tr -d ' ')

if [ "$FILE_COUNT" -eq 0 ]; then
    echo "No modified Swift files to lint"
    exit 0
fi

echo "Running SwiftLint on $FILE_COUNT modified Swift file(s)..."

# Run SwiftLint on modified files
# Use --quiet to reduce output noise in Xcode
# Use --config to specify our configuration file
echo -e "$FILTERED_FILES" | while IFS= read -r file; do
    if [ -n "$file" ] && [ -f "$file" ]; then
        "$SWIFTLINT_PATH" lint --config "$SRCROOT/.swiftlint.yml" --quiet "$file"
    fi
done

# Check exit status
if [ $? -eq 0 ]; then
    echo "SwiftLint completed successfully"
else
    echo "error: SwiftLint found violations. Please fix them before building."
    exit 1
fi
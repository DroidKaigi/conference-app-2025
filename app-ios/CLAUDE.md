# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is the iOS app for DroidKaigi 2025 conference. For general project information, architecture diagrams, and getting started guide, see [README.md](./README.md).

## Build and Development Commands

### Quick Setup
```bash
# Install nest (if not already installed)
curl -s https://raw.githubusercontent.com/mtj0928/nest/main/Scripts/install.sh | bash
# Add nest to PATH (if not already done)
export PATH="$PATH:~/.nest/bin"

# Initial setup (installs SwiftLint via nest)
make setup

# Open in Xcode
make xcode
```

### Building the App
```bash
# Build with Makefile (recommended)
make build          # Build all packages with prebuilt SwiftSyntax
make xcode-build    # Build with Xcode

# Or build directly with xcodebuild
xcodebuild build -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025 -configuration Debug
xcodebuild build -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025 -configuration Release

# Build specific packages
cd Core && swift build
cd Native && swift build
```

### Running Tests
```bash
# Run tests with Makefile (recommended)
make test           # Run Core tests (Linux-compatible)
make test-native    # Run Native module tests (macOS only)
make test-all       # Run all tests (Core + Native, macOS only)
make xcode-test     # Run tests with Xcode

# Or run directly with xcodebuild
xcodebuild test -project DroidKaigi2025.xcodeproj -scheme ComponentTests -destination 'platform=iOS Simulator,name=iPhone 15 Pro'
xcodebuild test -project DroidKaigi2025.xcodeproj -scheme UseCaseTests -destination 'platform=iOS Simulator,name=iPhone 15 Pro'

# Run package tests
cd Core && swift test
cd Native && swift test

# Run Core Package tests on Ubuntu (cross-platform support)
cd Core && swift test  # Works on Ubuntu/Linux environments
```

### Available Schemes
- `DroidKaigi2025` - Main app
- `ComponentTests` - UI component tests
- `Model` - Data model library
- `Presentation` - Presentation layer
- `Root` - Root navigation
- `UseCaseTests` - Business logic tests

## Architecture

See [README.md](./README.md#-architecture) for detailed architecture documentation with diagrams.

## Technical Requirements & Features

See [README.md](./README.md#-getting-started) for technical requirements, dependencies, and feature list.

## Code Quality Tools

### Linting & Formatting
```bash
# Linting (SwiftLint installed via nest)
make lint          # Check for linting issues
make lint-fix      # Auto-fix linting issues

# Formatting (swift-format)
make format        # Format code with swift-format
make format-check  # Check formatting (fails on warnings)

# Pre-commit checks
make pre-commit    # Run all checks before committing
```

### Xcode Integration
For faster incremental builds, install the Xcode build phase script:
1. Open Xcode project
2. Add Run Script Phase with: `"${SRCROOT}/scripts/xcode-lint.sh"`
3. This will only lint modified files during builds

See [scripts/README.md](./scripts/README.md) for detailed setup.

### All Available Makefile Commands
```bash
make help          # Show all available commands
make setup         # Initial project setup (installs SwiftLint via nest)
make build         # Build all packages with prebuilt SwiftSyntax
make test          # Run Core tests (Linux-compatible)
make test-native   # Run Native module tests (macOS only)
make test-all      # Run all tests (Core + Native, macOS only)
make lint          # Run SwiftLint
make lint-fix      # Auto-fix linting issues
make format        # Format code with swift-format
make format-check  # Check code formatting (fails on warnings)
make pre-commit    # Run all checks before committing
make clean         # Clean build artifacts
make reset         # Reset project (clean + resolve dependencies)
make xcode         # Open project in Xcode
make xcode-build   # Build with Xcode
make xcode-test    # Run tests with Xcode
make ci            # Run CI checks (lint + Core tests)
```

See [README.md](./README.md#-development) for more details.

## Claude-Specific Development Notes

### Important Context
- The project is part of a larger multi-platform repository
- Android app documentation in root README.md shows architectural decisions that may influence iOS development
- Uses modern Swift 6 features including strict concurrency checking
- Linting and formatting tools are configured and should be used before completing work

## Important Build and Debug Notes

### SwiftLint Setup
- SwiftLint is installed via nest (dependency manager)
- Configuration file: `nestfile.yml` defines the SwiftLint version
- Linting configuration: `.swiftlint.yml`
- Runs via `make lint` or automatically during Xcode builds with `scripts/xcode-lint.sh`
- For CI environments, SwiftLint needs to be installed separately

### Build Issues
- **Swift Dependencies Macro Error**: The project may encounter macro validation errors with swift-dependencies package when building. This is a known issue with the package itself, not your code changes.
  - Error: "cannot load module 'SwiftDiagnostics' built with SDK 'macosx15.5' when using SDK 'iphonesimulator18.5'"
  - **Workaround**: Open the project in Xcode and build from there, which handles the macro plugin correctly
  - Alternative: Build the app without the DependenciesMacrosPlugin target by commenting it out temporarily
  - The app binary installed on simulator may still work despite build errors

### Debugging
- **Use Mobile MCP for debugging**: When testing UI changes and functionality on iOS simulators, use Mobile MCP tools instead of relying solely on build success
  - `mobile_launch_app`, `mobile_take_screenshot`, `mobile_list_elements_on_screen` etc.
  - This allows verification of UI changes even when build has macro-related errors

### Code Quality Checks
- **Always run lint/format/test before completing work**:
  - `make lint` - Check for linting issues
  - `make lint-fix` - Auto-fix linting issues
  - `make format` - Format code with swift-format
  - `make test` - Run all tests
  - Ensure all checks pass and fixes are applied before considering work complete

### KMP Integration
- **Sponsor data integration**: Currently using mock data in `SponsorUseCaseImpl` as KMP doesn't have sponsorsRepository yet
  - When KMP adds sponsor repository, update `SponsorUseCaseImpl.load()` to use actual data
  - Follow the pattern used in `TimetableUseCaseImpl` for reference

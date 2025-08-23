# DroidKaigi 2025 iOS App Makefile

.PHONY: help
help: ## Show this help message
	@echo 'Usage:'
	@echo '  make [target]'
	@echo ''
	@echo 'Targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-20s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.PHONY: setup
setup: ## Initial setup for the project
	@echo "Installing dependencies..."
	@if [ -f /.dockerenv ] || [ "$$CI" = "true" ]; then \
		echo "CI environment detected, skipping nest setup"; \
		echo "Use 'make ci-setup' for CI environment setup"; \
	else \
		if ! command -v nest >/dev/null 2>&1; then \
			echo "❌ nest is not installed."; \
			echo "Please install nest from: https://github.com/mtj0928/nest"; \
			echo ""; \
			echo "Installation options:"; \
			echo "  - Script: curl -s https://raw.githubusercontent.com/mtj0928/nest/main/Scripts/install.sh | bash (Don't forget to add ~/.nest/bin to PATH)"; \
			echo "  - Manual: Download from https://github.com/mtj0928/nest/releases"; \
			exit 1; \
		fi; \
		echo "Installing SwiftLint via nest..."; \
		nest bootstrap nestfile.yml; \
	fi
	@cd Core && swift package resolve
	@cd Native && swift package resolve
	@echo "Setup complete!"

.PHONY: build
build: ## Build the iOS app with prebuilt SwiftSyntax
	@echo "Building Core package..."
	@cd Core && swift build --enable-experimental-prebuilts
	@echo "Building Native package..."
	@cd Native && swift build --enable-experimental-prebuilts
	@echo "Build complete!"

.PHONY: test
test: ## Run Core tests (Linux-compatible)
	@echo "Running Core tests..."
	@cd Core && swift test --enable-experimental-prebuilts
	# Native module tests are run separately on macOS environments
	# as they require platform-specific dependencies (UIKit, SwiftUI)
	# @echo "Running Native tests..."
	# @cd Native && swift test --enable-experimental-prebuilts
	@echo "Tests complete!"

.PHONY: test-native
test-native: ## Run Native module tests (macOS only)
	@echo "Running Native tests (macOS only)..."
	@cd Native && swift test --enable-experimental-prebuilts
	@echo "Native tests complete!"

.PHONY: test-all
test-all: ## Run all tests (Core + Native, macOS only)
	@echo "Running all tests..."
	@cd Core && swift test --enable-experimental-prebuilts
	@echo "Running Native tests..."
	@cd Native && swift test --enable-experimental-prebuilts
	@echo "All tests complete!"

.PHONY: lint
lint: ## Run SwiftLint
	@echo "Running SwiftLint..."
	@if [ -f .nest/bin/swiftlint ]; then \
		find Core Native App -name "*.swift" -type f ! -path "*/.build/*" ! -path "*/Package.swift" ! -path "*/KMPFramework/*" ! -path "*/DerivedData/*" | xargs .nest/bin/swiftlint lint --config .swiftlint.yml; \
	elif command -v swiftlint >/dev/null 2>&1; then \
		find Core Native App -name "*.swift" -type f ! -path "*/.build/*" ! -path "*/Package.swift" ! -path "*/KMPFramework/*" ! -path "*/DerivedData/*" | xargs swiftlint lint --config .swiftlint.yml; \
	else \
		echo "SwiftLint not installed. Run 'make setup' to install via nest."; \
		exit 1; \
	fi

.PHONY: lint-fix
lint-fix: ## Run SwiftLint with auto-correction
	@echo "Running SwiftLint auto-correction..."
	@if [ -f .nest/bin/swiftlint ]; then \
		find Core Native App -name "*.swift" -type f ! -path "*/.build/*" ! -path "*/Package.swift" ! -path "*/KMPFramework/*" ! -path "*/DerivedData/*" | xargs .nest/bin/swiftlint lint --config .swiftlint.yml --fix; \
	elif command -v swiftlint >/dev/null 2>&1; then \
		find Core Native App -name "*.swift" -type f ! -path "*/.build/*" ! -path "*/Package.swift" ! -path "*/KMPFramework/*" ! -path "*/DerivedData/*" | xargs swiftlint lint --config .swiftlint.yml --fix; \
	else \
		echo "SwiftLint not installed. Run 'make setup' to install via nest."; \
		exit 1; \
	fi

.PHONY: format
format: ## Format code using swift-format
	@echo "Formatting code with swift-format..."
	@find Core/Sources Native/Sources App -name "*.swift" -type f ! -path "*/KMPFramework/*" -exec swift format --configuration .swift-format --in-place {} +

.PHONY: format-check
format-check: ## Check code formatting without making changes (fails on warnings)
	@echo "Checking code format..."
	@output=$$(find Core/Sources Native/Sources App -name "*.swift" -type f ! -path "*/KMPFramework/*" -exec swift format lint --configuration .swift-format {} + 2>&1) ; \
	if [ -n "$$output" ]; then \
		echo "$$output"; \
		echo ""; \
		echo "❌ Format check failed: warnings or errors found"; \
		exit 1; \
	else \
		echo "✅ Format check passed: no issues found"; \
	fi

.PHONY: clean
clean: ## Clean build artifacts
	@echo "Cleaning build artifacts..."
	@rm -rf .build
	@rm -rf Core/.build
	@rm -rf Native/.build
	@rm -rf DerivedData
	@echo "Clean complete!"

.PHONY: reset
reset: clean ## Reset project (clean + resolve dependencies)
	@echo "Resetting project..."
	@make setup
	@echo "Reset complete!"

.PHONY: pre-commit
pre-commit: format lint test ## Run all checks before committing (Core tests only)
	@echo "Pre-commit checks complete!"

.PHONY: install-swiftlint
install-swiftlint: ## Install SwiftLint
	@echo "Installing SwiftLint..."
	@if [ -f /.dockerenv ] || [ "$$CI" = "true" ]; then \
		make ci-setup; \
	else \
		nest bootstrap nestfile.yml; \
	fi

.PHONY: ci-setup
ci-setup: ## Setup for CI environment (no longer needed - using Swift Package Plugin)
	@echo "CI setup is no longer needed - SwiftLint runs via Swift Package Plugin"

.PHONY: ci-lint
ci-lint: ## Run SwiftLint for CI using Swift Package Plugin
	@echo "Running SwiftLint for CI..."
	@cd Core && swift package plugin --allow-writing-to-package-directory swiftlint
	@cd Native && swift package plugin --allow-writing-to-package-directory swiftlint

.PHONY: ci-format
ci-format: ## Check formatting for CI with swift-format (fails on warnings)
	@echo "Checking code format for CI..."
	@output=$$(find Core/Sources Native/Sources App -name "*.swift" -type f ! -path "*/KMPFramework/*" -exec swift format lint --configuration .swift-format {} + 2>&1) ; \
	if [ -n "$$output" ]; then \
		echo "$$output"; \
		echo ""; \
		echo "❌ Format check failed: warnings or errors found"; \
		exit 1; \
	else \
		echo "✅ Format check passed: no issues found"; \
	fi

.PHONY: ci
ci: ## Run CI checks (lint + Core tests)
	@echo "Running CI checks..."
	@make lint
	@make test  # Core tests only for Linux compatibility
	@echo "CI checks complete!"

.PHONY: xcode
xcode: ## Open project in Xcode
	@open DroidKaigi2025.xcodeproj

.PHONY: xcode-clean
xcode-clean: ## Clean Xcode build
	@xcodebuild clean -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025

.PHONY: xcode-build
xcode-build: ## Build with Xcode
	@xcodebuild build -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025 -configuration Debug

.PHONY: xcode-test
xcode-test: ## Run tests with Xcode
	@xcodebuild test -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025 -destination 'platform=iOS Simulator,name=iPhone 15 Pro'

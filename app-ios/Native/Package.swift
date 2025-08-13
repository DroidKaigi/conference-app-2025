// swift-tools-version: 6.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "Native",
    platforms: [
        .iOS(.v18),
        .macOS(.v15),
    ],
    products: [
        .library(
            name: "Root",
            targets: ["Root"]
        ),
    ],
    dependencies: [
        .package(path: "../Core"),
        .package(url: "https://github.com/pointfreeco/swift-dependencies.git", exact: "1.9.2"),
        .package(url: "https://github.com/SwiftGen/SwiftGenPlugin", from: "6.6.2"),
    ],
    targets: [
        .target(
            name: "Component",
            dependencies: [
                .target(name: "Extension"),
                .target(name: "Theme"),
                .product(name: "Model", package: "Core"),
            ]
        ),
        .testTarget(
            name: "ComponentTests",
            dependencies: ["Component"]
        ),
        .target(
            name: "DependencyExtra",
        ),

        .target(
            name: "Extension",
            dependencies: [
                .product(name: "Model", package: "Core"),
            ],
        ),

        .target(
            name: "Root",
            dependencies: [
                .target(name: "HomeFeature"),
                .target(name: "TimetableDetailFeature"),
                .target(name: "AboutFeature"),
                .target(name: "ContributorFeature"),
                .target(name: "EventMapFeature"),
                .target(name: "FavoriteFeature"),
                .target(name: "SearchFeature"),
                .target(name: "SponsorFeature"),
                .target(name: "StaffFeature"),
                .target(name: "ProfileCardFeature"),
                .target(name: "KMPFramework"),
                .product(name: "UseCase", package: "Core"),
                .product(name: "Model", package: "Core"),
                .product(name: "Dependencies", package: "swift-dependencies"),
                .target(name: "Extension"),
            ],
        ),

        // Feature targets with resources
        .target(
            name: "HomeFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/Home",
            resources: [
                .process("Resources")
            ],
        ),
        
        // Feature targets without resources
        .target(
            name: "TimetableDetailFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/TimetableDetail",
        ),
        
        .target(
            name: "AboutFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/About",
            resources: [
                .process("Resources")
            ],
        ),
        
        .target(
            name: "ContributorFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/Contributor",
            resources: [
                .process("Resources")
            ],
        ),
        
        .target(
            name: "EventMapFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
                .target(name: "DependencyExtra"),
            ],
            path: "Sources/Feature/EventMap",
        ),
        
        .target(
            name: "FavoriteFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/Favorite",
            resources: [
                .process("Resources")
            ],
        ),
        
        .target(
            name: "SearchFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/Search",
            resources: [
                .process("Resources")
            ],
        ),
        
        .target(
            name: "SponsorFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/Sponsor",
        ),
        
        .target(
            name: "StaffFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/Staff",
            resources: [
                .process("Resources")
            ],
        ),
        
        .target(
            name: "ProfileCardFeature",
            dependencies: [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/ProfileCard",
        ),

        .target(
            name: "Theme",
            resources: [
                .process("Resources"),
                .process("swiftgen.yml"),
            ],
            plugins: [
                .plugin(name: "SwiftGenPlugin", package: "SwiftGenPlugin")
            ]
        ),
        // Please run ./gradlew app-shared:assembleSharedXCFramework first
        .binaryTarget(name: "KMPFramework", path: "../../app-shared/build/XCFrameworks/debug/shared.xcframework"),
    ],
    swiftLanguageModes: [.v6]
)

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
    ],
    targets: [
        .target(
            name: "Component"
        ),
        .testTarget(
            name: "ComponentTests",
            dependencies: ["Component"]
        ),

        .target(
            name: "Extension",
            dependencies: [
                .product(name: "Model", package: "Core"),
            ]
        ),

        .target(
            name: "Root",
            dependencies: [
                .target(name: "HomeFeature"),
                .target(name: "KMPFramework"),
                .product(name: "UseCase", package: "Core"),
                .product(name: "Model", package: "Core"),
                .product(name: "Dependencies", package: "swift-dependencies"),
            ]
        ),

        .featureTarget(name: "Home"),

        .target(name: "Theme"),
        .binaryTarget(name: "KMPFramework", path: "Sources/KMPFramework/shared.xcframework"),
    ],
    swiftLanguageModes: [.v6]
)

extension Target {
    static func featureTarget(
        name: String,
        dependencies: [Target.Dependency] = []
    ) -> Target {
        .target(
            name: "\(name)Feature",
            dependencies: dependencies + [
                .product(name: "Presentation", package: "Core"),
                .product(name: "Model", package: "Core"),
                .target(name: "Component"),
                .target(name: "Extension"),
                .target(name: "Theme"),
            ],
            path: "Sources/Feature/\(name)"
        )
    }
}

// swift-tools-version: 6.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "Core",
    platforms: [
        .iOS(.v18),
        .macOS(.v15),
    ],
    products: [
        .library(
            name: "Presentation",
            targets: ["Presentation"]
        ),
        .library(name: "Model", targets: ["Model"]),
        .library(name: "UseCase", targets: ["UseCase"])
    ],
    dependencies: [
        .package(url: "https://github.com/pointfreeco/swift-dependencies.git", exact: "1.9.2"),
    ],
    targets: [
        .target(name: "Model"),

        .target(
            name: "Presentation",
            dependencies: [
                .target(name: "Model"),
                .target(name: "UseCase"),
                .product(name: "Dependencies", package: "swift-dependencies"),
            ]
        ),
        .testTarget(
            name: "PresentationTests",
            dependencies: [
                .target(name: "Presentation"),
                .target(name: "Model"),
                .product(name: "Dependencies", package: "swift-dependencies"),
            ]
        ),

        .target(
            name: "UseCase",
            dependencies: [
                .target(name: "Model"),
                .product(name: "Dependencies", package: "swift-dependencies"),
                .product(name: "DependenciesMacros", package: "swift-dependencies"),
            ]
        ),
        .testTarget(
            name: "UseCaseTests",
            dependencies: ["UseCase"]
        ),
    ],
    swiftLanguageModes: [.v6]
)

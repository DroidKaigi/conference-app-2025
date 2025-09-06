import SwiftUI
import Model

enum Material3ShapeType: String, CaseIterable {
    case pill
    case diamond
    case flower
}

extension Material3ShapeType {
    init(from cardVariant: ProfileCardVariant) {
        print("Material3ShapeType.init: cardVariant = \(cardVariant), shape = \(cardVariant.shape)")
        switch cardVariant.shape {
        case .pill:
            self = .pill
        case .diamond:
            self = .diamond
        case .flower:
            self = .flower
        }
        print("Material3ShapeType.init: result = \(self)")
    }
}

struct Material3Shape: Shape {
    let type: Material3ShapeType

    func path(in rect: CGRect) -> Path {
        switch type {
        case .pill:
            return pillPath(in: rect)
        case .diamond:
            return diamondPath(in: rect)
        case .flower:
            return flowerPath(in: rect)
        }
    }

    private func pillPath(in rect: CGRect) -> Path {
        var path = Path()
        let size = min(rect.width, rect.height)
        let center = CGPoint(x: rect.midX, y: rect.midY)

        let scale = size / 250.0
        let rotationAngle: CGFloat = 310 * .pi / 180

        let pillLength = 267 * scale
        let pillWidth = 200 * scale
        let radius = pillWidth / 2

        var pillPath = Path()

        let startX = -pillLength / 2 + radius
        let startY = -radius

        pillPath.move(to: CGPoint(x: startX, y: startY))
        pillPath.addLine(to: CGPoint(x: pillLength / 2 - radius, y: -radius))

        pillPath.addArc(
            center: CGPoint(x: pillLength / 2 - radius, y: 0),
            radius: radius,
            startAngle: .degrees(-90),
            endAngle: .degrees(90),
            clockwise: false
        )

        pillPath.addLine(to: CGPoint(x: -pillLength / 2 + radius, y: radius))

        pillPath.addArc(
            center: CGPoint(x: -pillLength / 2 + radius, y: 0),
            radius: radius,
            startAngle: .degrees(90),
            endAngle: .degrees(270),
            clockwise: false
        )

        pillPath.closeSubpath()

        let transform = CGAffineTransform(translationX: center.x, y: center.y)
            .rotated(by: rotationAngle)

        return pillPath.applying(transform)
    }

    private func diamondPath(in rect: CGRect) -> Path {
        // Material 3 Diamond shape - based on SVG path, creates an octagonal diamond
        var path = Path()
        let size = min(rect.width, rect.height)
        let center = CGPoint(x: rect.midX, y: rect.midY)

        // Scale coordinates from SVG (380x380) to our rect
        let scale = size / 380.0

        // Convert SVG coordinates to our coordinate system, centered
        let svgCenterX: CGFloat = 190 // SVG center (380/2)
        let svgCenterY: CGFloat = 190 // SVG center (380/2)

        func convertSVGPoint(x: CGFloat, y: CGFloat) -> CGPoint {
            let scaledX = (x - svgCenterX) * scale + center.x
            let scaledY = (y - svgCenterY) * scale + center.y
            return CGPoint(x: scaledX, y: scaledY)
        }

        // Diamond quadrilateral shape with 4 rounded corners
        // Adjusted proportions: height x2, width x1.3

        let baseWidth = 160.0  // Original: ~160 (350-30 = 320, half = 160)
        let baseHeight = 160.0 // Original: ~160 (350-30 = 320, half = 160)

        let adjustedWidth = baseWidth * 1.30
        let adjustedHeight = baseHeight * 1.40

        let points = [
            convertSVGPoint(x: 190, y: 190 - adjustedHeight),    // Top
            convertSVGPoint(x: 190 + adjustedWidth, y: 190),     // Right
            convertSVGPoint(x: 190, y: 190 + adjustedHeight),    // Bottom
            convertSVGPoint(x: 190 - adjustedWidth, y: 190)      // Left
        ]

        guard points.count == 4 else { return path }

        let cornerRadius: CGFloat = 80 * scale // Rounded corner radius

        // Calculate all corner points first
        var cornerSegments: [(start: CGPoint, end: CGPoint, control: CGPoint)] = []

        for i in 0..<4 {
            let currentPoint = points[i]
            let nextPoint = points[(i + 1) % 4]
            let prevPoint = points[(i + 3) % 4]

            // Direction vectors
            let dirFromPrev = CGPoint(
                x: currentPoint.x - prevPoint.x,
                y: currentPoint.y - prevPoint.y
            )
            let dirToNext = CGPoint(
                x: nextPoint.x - currentPoint.x,
                y: nextPoint.y - currentPoint.y
            )

            // Normalize directions
            let lengthFromPrev = sqrt(dirFromPrev.x * dirFromPrev.x + dirFromPrev.y * dirFromPrev.y)
            let lengthToNext = sqrt(dirToNext.x * dirToNext.x + dirToNext.y * dirToNext.y)

            let normalizedFromPrev = CGPoint(
                x: dirFromPrev.x / lengthFromPrev,
                y: dirFromPrev.y / lengthFromPrev
            )
            let normalizedToNext = CGPoint(
                x: dirToNext.x / lengthToNext,
                y: dirToNext.y / lengthToNext
            )

            // Points before and after corner
            let beforeCorner = CGPoint(
                x: currentPoint.x - normalizedFromPrev.x * cornerRadius,
                y: currentPoint.y - normalizedFromPrev.y * cornerRadius
            )
            let afterCorner = CGPoint(
                x: currentPoint.x + normalizedToNext.x * cornerRadius,
                y: currentPoint.y + normalizedToNext.y * cornerRadius
            )

            cornerSegments.append((start: beforeCorner, end: afterCorner, control: currentPoint))
        }

        // Start from the first segment's start point
        path.move(to: cornerSegments[0].start)

        // Draw all segments
        for i in 0..<4 {
            let currentSegment = cornerSegments[i]
            let nextSegment = cornerSegments[(i + 1) % 4]

            // Draw the rounded corner
            path.addQuadCurve(to: currentSegment.end, control: currentSegment.control)

            // Draw line to next segment's start (if not the last segment)
            if i < 3 {
                path.addLine(to: nextSegment.start)
            }
        }

        path.closeSubpath()

        return path
    }

    private func flowerPath(in rect: CGRect) -> Path {
        // Material 3 Flower shape - based on SVG, creates a complex star/flower with curved petals
        var path = Path()
        let size = min(rect.width, rect.height)
        let center = CGPoint(x: rect.midX, y: rect.midY)
        let scale = size / 200.0

        // Convert SVG coordinates to our coordinate system, centered
        let svgCenterX: CGFloat = 190 // SVG center
        let svgCenterY: CGFloat = 190 // SVG center

        func convertSVGPoint(x: CGFloat, y: CGFloat) -> CGPoint {
            let scaledX = (x - svgCenterX) * scale + center.x
            let scaledY = (y - svgCenterY) * scale + center.y
            return CGPoint(x: scaledX, y: scaledY)
        }

        // Create flower shape with 8 onigiri-shaped petals
        let petalCount = 8
        let outerRadius = size / 2 * 0.9
        let innerRadius = size / 2 * 0.3  // Smaller inner radius for more triangular shape

        // Start from center and draw onigiri-shaped petals
        path.move(to: center)

        for i in 0..<petalCount {
            let petalAngle = CGFloat(i) * 2 * .pi / CGFloat(petalCount)
            let halfPetalAngle = .pi / CGFloat(petalCount)

            // Onigiri shape: wide base at center, narrow sides, rounded top
            let tipPoint = CGPoint(
                x: center.x + cos(petalAngle) * outerRadius,
                y: center.y + sin(petalAngle) * outerRadius
            )

            // Base points (wider at the bottom like onigiri)
            let baseWidth = innerRadius * 1.5
            let leftBaseAngle = petalAngle - halfPetalAngle * 0.8
            let rightBaseAngle = petalAngle + halfPetalAngle * 0.8

            let leftBase = CGPoint(
                x: center.x + cos(leftBaseAngle) * baseWidth,
                y: center.y + sin(leftBaseAngle) * baseWidth
            )
            let rightBase = CGPoint(
                x: center.x + cos(rightBaseAngle) * baseWidth,
                y: center.y + sin(rightBaseAngle) * baseWidth
            )

            // Side points for onigiri shape (narrowing toward tip)
            let sideRadius = outerRadius * 0.6
            let leftSide = CGPoint(
                x: center.x + cos(petalAngle - halfPetalAngle * 0.4) * sideRadius,
                y: center.y + sin(petalAngle - halfPetalAngle * 0.4) * sideRadius
            )
            let rightSide = CGPoint(
                x: center.x + cos(petalAngle + halfPetalAngle * 0.4) * sideRadius,
                y: center.y + sin(petalAngle + halfPetalAngle * 0.4) * sideRadius
            )

            if i == 0 {
                // Move to first left base
                path.move(to: leftBase)
            } else {
                // Connect from previous petal
                path.addLine(to: leftBase)
            }

            // Draw onigiri-shaped petal
            // Left side: curve from base to side point
            path.addQuadCurve(to: leftSide, control: CGPoint(
                x: leftBase.x + (leftSide.x - leftBase.x) * 0.3,
                y: leftBase.y + (leftSide.y - leftBase.y) * 0.3
            ))

            // Top: rounded curve to tip
            let tipControlRadius = outerRadius * 0.8
            let tipControl = CGPoint(
                x: center.x + cos(petalAngle) * tipControlRadius,
                y: center.y + sin(petalAngle) * tipControlRadius
            )
            path.addQuadCurve(to: tipPoint, control: tipControl)

            // Right side: curve from tip to right side
            path.addQuadCurve(to: rightSide, control: CGPoint(
                x: tipPoint.x + (rightSide.x - tipPoint.x) * 0.3,
                y: tipPoint.y + (rightSide.y - tipPoint.y) * 0.3
            ))

            // Bottom right: curve to right base
            path.addQuadCurve(to: rightBase, control: CGPoint(
                x: rightSide.x + (rightBase.x - rightSide.x) * 0.7,
                y: rightSide.y + (rightBase.y - rightSide.y) * 0.7
            ))
        }

        path.closeSubpath()

        return path
    }
}

struct Material3ClippedImage: View {
    let imageName: String?
    let systemImageName: String?
    let bundle: Bundle?
    let shapeType: Material3ShapeType
    let size: CGFloat

    init(
        imageName: String? = nil,
        systemImageName: String? = nil,
        bundle: Bundle? = nil,
        shapeType: Material3ShapeType,
        size: CGFloat
    ) {
        self.imageName = imageName
        self.systemImageName = systemImageName
        self.bundle = bundle
        self.shapeType = shapeType
        self.size = size
    }

    var body: some View {
        Group {
            if let imageName = imageName {
                Image(imageName, bundle: bundle)
                    .resizable()
                    .scaledToFill()
            } else if let systemImageName = systemImageName {
                Image(systemName: systemImageName)
                    .resizable()
                    .scaledToFit()
                    .foregroundColor(.accentColor)
                    .padding(size * 0.2)
                    .background(Color.gray.opacity(0.2))
            } else {
                Color.gray.opacity(0.3)
            }
        }
        .frame(width: size, height: size)
        .clipShape(Material3Shape(type: shapeType))
    }
}

#Preview {
    VStack(spacing: 20) {
        HStack(spacing: 20) {
            Material3ClippedImage(
                systemImageName: "person.circle.fill",
                shapeType: .pill,
                size: 100
            )
            Material3ClippedImage(
                systemImageName: "person.circle.fill",
                shapeType: .diamond,
                size: 100
            )
            Material3ClippedImage(
                systemImageName: "person.circle.fill",
                shapeType: .flower,
                size: 100
            )
        }

        HStack(spacing: 20) {
            ForEach(Material3ShapeType.allCases, id: \.self) { shape in
                VStack {
                    Material3Shape(type: shape)
                        .stroke(Color.blue, lineWidth: 2)
                        .frame(width: 80, height: 80)
                    Text(shape.rawValue.capitalized)
                        .font(.caption)
                }
            }
        }
    }
    .padding()
}

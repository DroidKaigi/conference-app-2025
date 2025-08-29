import SwiftUI

enum Material3ShapeType: String, CaseIterable {
    case pill
    case diamond
    case flower
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
        Path(roundedRect: rect, cornerRadius: min(rect.width, rect.height) / 2)
    }
    
    private func diamondPath(in rect: CGRect) -> Path {
        var path = Path()
        let center = CGPoint(x: rect.midX, y: rect.midY)
        let radius = min(rect.width, rect.height) / 2
        
        path.move(to: CGPoint(x: center.x, y: center.y - radius))
        path.addLine(to: CGPoint(x: center.x + radius, y: center.y))
        path.addLine(to: CGPoint(x: center.x, y: center.y + radius))
        path.addLine(to: CGPoint(x: center.x - radius, y: center.y))
        path.closeSubpath()
        
        return path
    }
    
    private func flowerPath(in rect: CGRect) -> Path {
        var path = Path()
        let center = CGPoint(x: rect.midX, y: rect.midY)
        let radius = min(rect.width, rect.height) / 2
        let petalRadius = radius * 0.35
        let petalCount = 6
        
        for i in 0..<petalCount {
            let angle = (CGFloat(i) * 2 * .pi) / CGFloat(petalCount)
            let petalCenter = CGPoint(
                x: center.x + cos(angle) * (radius - petalRadius),
                y: center.y + sin(angle) * (radius - petalRadius)
            )
            
            if i == 0 {
                path.move(to: petalCenter)
            }
            
            let controlAngle1 = angle - .pi / CGFloat(petalCount)
            let controlAngle2 = angle + .pi / CGFloat(petalCount)
            
            let control1 = CGPoint(
                x: center.x + cos(controlAngle1) * radius * 1.1,
                y: center.y + sin(controlAngle1) * radius * 1.1
            )
            let control2 = CGPoint(
                x: center.x + cos(controlAngle2) * radius * 1.1,
                y: center.y + sin(controlAngle2) * radius * 1.1
            )
            
            let nextAngle = (CGFloat(i + 1) * 2 * .pi) / CGFloat(petalCount)
            let nextPetalCenter = CGPoint(
                x: center.x + cos(nextAngle) * (radius - petalRadius),
                y: center.y + sin(nextAngle) * (radius - petalRadius)
            )
            
            path.addCurve(
                to: nextPetalCenter,
                control1: control1,
                control2: control2
            )
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

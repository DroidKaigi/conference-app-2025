import SwiftUI

struct DashedDivider: View {
    let axis: Axis
    
    var body: some View {
        let shape = LineShape(axis: axis)
            .stroke(style: StrokeStyle(dash: [2]))
            .foregroundColor(Color(.separator).opacity(0.5))
        
        if axis == .horizontal {
            shape.frame(height: 1)
        } else {
            shape.frame(width: 1)
        }
    }
}

struct LineShape: Shape {
    let axis: Axis
    
    func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: CGPoint(x: 0, y: 0))
        
        if axis == .horizontal {
            path.addLine(to: CGPoint(x: rect.width, y: 0))
        } else {
            path.addLine(to: CGPoint(x: 0, y: rect.height))
        }
        
        return path
    }
}
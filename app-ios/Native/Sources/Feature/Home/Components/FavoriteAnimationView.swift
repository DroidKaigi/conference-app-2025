import SwiftUI
import Model

struct FavoriteAnimationView: View {
    let targetTimetableItemId: String?
    let targetLocationPoint: CGPoint?
    let animationProgress: CGFloat
    
    var body: some View {
        GeometryReader { geometry in
            if targetTimetableItemId != nil {
                Image(systemName: "heart.fill")
                    .foregroundColor(Color.blue.opacity(0.8))
                    .frame(width: 24, height: 24)
                    .position(animationPosition(geometry: geometry))
                    .opacity(1 - animationProgress)
                    .zIndex(99)
            }
        }
    }
    
    private func animationPosition(geometry: GeometryProxy) -> CGPoint {
        let globalGeometrySize = geometry.frame(in: .global).size
        let defaultGeometrySize = geometry.size
        
        let startPositionY = targetLocationPoint?.y ?? 0
        let endPositionY = defaultGeometrySize.height - 25
        let targetY = startPositionY + (endPositionY - startPositionY) * animationProgress
        
        let adjustedPositionX = animationProgress * (globalGeometrySize.width / 2 - globalGeometrySize.width + 50)
        let targetX = defaultGeometrySize.width - 50 + adjustedPositionX
        
        return CGPoint(x: targetX, y: targetY)
    }
}
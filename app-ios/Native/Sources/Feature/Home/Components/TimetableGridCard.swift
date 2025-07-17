import Extension
import SwiftUI
import Model
import Presentation
import Theme

struct TimetableGridCard: View {
    let timetableItem: TimetableItem
    let cellCount: Int
    let onTap: (TimetableItem) -> Void
    
    var body: some View {
        Button(action: {
            onTap(timetableItem)
        }) {
            VStack(alignment: .leading, spacing: 4) {
                Text(timetableItem.title.currentLangTitle)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(Color(.label))
                    .multilineTextAlignment(.leading)
                    .lineLimit(2)
                
                if !timetableItem.speakers.isEmpty {
                    Text(timetableItem.speakers.map(\.name).joined(separator: ", "))
                        .font(.system(size: 12))
                        .foregroundColor(Color(.secondaryLabel))
                        .lineLimit(1)
                }
                
                Spacer()
                
                HStack {
                    Text(timetableItem.language.displayLanguage)
                        .font(.system(size: 10, weight: .medium))
                        .foregroundColor(Color(.secondaryLabel))
                    Spacer()
                }
            }
            .padding(8)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(timetableItem.room.color.opacity(0.1))
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(timetableItem.room.color, lineWidth: 1)
            )
            .cornerRadius(4)
        }
        .buttonStyle(PlainButtonStyle())
        .frame(width: CGFloat(192 * cellCount + 4 * (cellCount - 1)), height: 153)
    }
}

// TODO: Add preview with proper test data
//#Preview {
//    TimetableGridCard(
//        timetableItem: ...,
//        cellCount: 1,
//        onTap: { _ in }
//    )
//}

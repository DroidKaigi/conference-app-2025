import Model
import SwiftUI
import Theme
import Presentation

struct TimeGroupList: View {
    let timeGroup: TimetableTimeGroupItems
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let onFavoriteTap: (TimetableItemWithFavorite, CGPoint?) -> Void
    
    var body: some View {
        HStack(alignment: .top, spacing: 16) {
            VStack {
                Text(timeGroup.startsTimeString)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(Color(.label))
                
                Text("|")
                    .font(.system(size: 8))
                    .foregroundColor(Color(.secondaryLabel))
                
                Text(timeGroup.endsTimeString)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(Color(.label))
                
                Spacer()
            }
            .frame(width: 50)
            
            VStack(spacing: 12) {
                ForEach(timeGroup.items) { item in
                    TimetableCard(
                        timetableItem: item.timetableItem,
                        isFavorite: item.isFavorited,
                        onTap: { _ in
                            onItemTap(item)
                        },
                        onTapFavorite: { _, point in
                            onFavoriteTap(item, point)
                        }
                    )
                }
            }
            .frame(maxWidth: .infinity)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
    }
}

// TODO: Add preview with proper test data
//#Preview {
//    TimeGroupList(
//        timeGroup: TimetableTimeGroupItems(
//            startsTimeString: "10:00",
//            endsTimeString: "10:50",
//            items: []
//        ),
//        onItemTap: { _ in },
//        onFavoriteTap: { _, _ in }
//    )
//}

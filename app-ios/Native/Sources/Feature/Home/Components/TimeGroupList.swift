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

#Preview {
    TimeGroupList(
        timeGroup: TimetableTimeGroupItems(
            startsTimeString: "10:00",
            endsTimeString: "10:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Opening Keynote",
                        startsAt: Date(),
                        endsAt: Date().addingTimeInterval(3600),
                        room: .roomA,
                        speakers: [Speaker(name: "John Doe")],
                        language: "EN"
                    ),
                    isFavorited: false
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Jetpack Compose Workshop",
                        startsAt: Date(),
                        endsAt: Date().addingTimeInterval(3600),
                        room: .roomB,
                        speakers: [Speaker(name: "Jane Smith")],
                        language: "JA"
                    ),
                    isFavorited: true
                )
            ]
        ),
        onItemTap: { _ in },
        onFavoriteTap: { _, _ in }
    )
}

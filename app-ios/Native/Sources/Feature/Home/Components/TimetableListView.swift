import SwiftUI
import Model
import Presentation

struct TimetableListView: View {
    let timetableItems: [TimetableTimeGroupItems]
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let onFavoriteTap: (TimetableItemWithFavorite, CGPoint?) -> Void
    let animationTrigger: (TimetableItem, CGPoint?) -> Void
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(timetableItems) { timeGroup in
                    TimeGroupList(
                        timeGroup: timeGroup,
                        onItemTap: { item in
                            onItemTap(item)
                        },
                        onFavoriteTap: { item, location in
                            onFavoriteTap(item, location)
                            
                            if !item.isFavorited {
                                animationTrigger(item.timetableItem, location)
                            }
                        }
                    )
                    
                    if timeGroup != timetableItems.last {
                        Divider()
                            .padding(.horizontal, 16)
                    }
                }
            }
            .padding(.bottom, 60)
        }
    }
}
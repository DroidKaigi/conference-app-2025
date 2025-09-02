import Component
import Model
import Presentation
import SwiftUI

struct TimetableListView: View {
    @Binding var selectedDay: DayTab
    let timetableItems: [TimetableTimeGroupItems]
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let onFavoriteTap: (TimetableItemWithFavorite, CGPoint?) -> Void

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                DayTabBar(selectedDay: $selectedDay)
                    .padding(.horizontal)

                ForEach(timetableItems) { timeGroup in
                    TimeGroupList(
                        timeGroup: timeGroup,
                        onItemTap: { item in
                            onItemTap(item)
                        },
                        onFavoriteTap: { item, location in
                            onFavoriteTap(item, location)
                        }
                    )
                }
            }
            .padding(.bottom, 60)
        }
    }
}

import Component
import Model
import SwiftUI
import Theme

struct FavoriteItemList: View {
    private var items: [TimetableTimeGroupItems]
    private let onItemTap: (TimetableItemWithFavorite) -> Void
    private let onToggleFavorite: (TimetableItemWithFavorite) -> Void

    init(
        items: [TimetableTimeGroupItems], onItemTap: @escaping (TimetableItemWithFavorite) -> Void,
        onToggleFavorite: @escaping (TimetableItemWithFavorite) -> Void
    ) {
        self.items = items
        self.onItemTap = onItemTap
        self.onToggleFavorite = onToggleFavorite
    }

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(items.indices, id: \.self) { index in
                    let timeGroup = items[index]

                    TimeGroupList(
                        timeGroup: timeGroup,
                        onItemTap: onItemTap,
                        onFavoriteTap: { item, _ in
                            onToggleFavorite(item)
                        }
                    )

                    if index < items.count - 1 {
                        DashedDivider()
                            .padding(.vertical, 16)
                            .padding(.horizontal, 16)
                    }
                }
            }
            .padding(.vertical, 20)
            .padding(.bottom, Constant.bottomPadding)  // Tab bar padding
        }
    }
}

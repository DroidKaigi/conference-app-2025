import Component
import Foundation
import Model
import SwiftUI
import Theme

public struct FavoriteScreen: View {
    @State private var presenter: FavoritePresenterProtocol

    let onNavigate: (FavoriteNavigationDestination) -> Void

    public init(
        presenter: FavoritePresenterProtocol? = nil,
        onNavigate: @escaping (FavoriteNavigationDestination) -> Void = { _ in },
    ) {
        self.presenter = presenter ?? FavoritePresenter()
        self.onNavigate = onNavigate
    }

    public var body: some View {
        VStack(spacing: 0) {
            // Date filter chips
            DateFilterView(selectedDate: $presenter.dateFilter)
                .padding(.horizontal, 16)
                .padding(.vertical, 12)

            if filteredItems.isEmpty {
                EmptyFavoritesView()
            } else {
                FavoriteItemList(
                    items: filteredItems,
                    onItemTap: { item in onNavigate(.timetableDetail(item)) },
                    onToggleFavorite: { item in presenter.toggleFavorite(item) },
                )
            }
        }
        .background(AssetColors.background.swiftUIColor)
        .navigationTitle(String(localized: "お気に入り", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .onAppear {
            presenter.loadInitial()
        }
    }

    private var filteredItems: [TimetableTimeGroupItems] {
        switch presenter.dateFilter {
        case .all:
            return presenter.favoriteTimetableItems
        case .day1:
            return presenter.favoriteTimetableItems.filter { timeGroup in
                guard let firstItem = timeGroup.items.first else { return false }
                let calendar = Calendar.current
                let components = calendar.dateComponents([.day], from: firstItem.timetableItem.startsAt)
                return components.day == 11
            }
        case .day2:
            return presenter.favoriteTimetableItems.filter { timeGroup in
                guard let firstItem = timeGroup.items.first else { return false }
                let calendar = Calendar.current
                let components = calendar.dateComponents([.day], from: firstItem.timetableItem.startsAt)
                return components.day == 12
            }
        }
    }
}

#Preview {
    FavoriteScreen()
}

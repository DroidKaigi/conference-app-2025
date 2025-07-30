import Dependencies
import Model
import Observation
import SwiftUI
import Theme
import Presentation
import Component
import TimetableDetailFeature

public struct HomeScreen: View {
    @State private var presenter = HomePresenter()
    @State private var animationProgress: CGFloat = 0
    @State private var targetTimetableItemId: String?
    @State private var targetLocationPoint: CGPoint?
    @State private var timetableMode: TimetableMode = .list
    @State private var selectedDay: DayTab = .day1
    @State private var selectedTimetableItem: TimetableItemWithFavorite?
    
    let onNavigate: (HomeNavigationDestination) -> Void
    
    public init(onNavigate: @escaping (HomeNavigationDestination) -> Void = { _ in }) {
        self.onNavigate = onNavigate
    }

    public var body: some View {
        let timetableItems = presenter.timetable.dayTimetable[selectedDay.model] ?? []

        ZStack {
            Group {
                switch timetableMode {
                case .list:
                    TimetableListView(
                        selectedDay: $selectedDay,
                        timetableItems: timetableItems,
                        onItemTap: { item in
                            selectedTimetableItem = item
                            onNavigate(.timetableDetail(item))
                        },
                        onFavoriteTap: { item, _ in
                            presenter.timetable.toggleFavorite(item)
                        },
                        animationTrigger: { timetableItem, location in
                            toggleFavorite(timetableItem: timetableItem, adjustedLocationPoint: location)
                        }
                    )
                case .grid:
                    TimetableGridView(
                        selectedDay: $selectedDay,
                        timetableItems: timetableItems,
                        rooms: presenter.timetable.rooms,
                        onItemTap: { item in
                            selectedTimetableItem = item
                            onNavigate(.timetableDetail(item))
                        },
                        isFavorite: { itemId in
                            presenter.timetable.isFavorite(itemId)
                        }
                    )
                }
            }
            
            FavoriteAnimationView(
                targetTimetableItemId: targetTimetableItemId,
                targetLocationPoint: targetLocationPoint,
                animationProgress: animationProgress
            )
        }
        .background(
            Image("background_night", bundle: .module)
                .resizable()
                .edgesIgnoringSafeArea(.all)
        )
        .navigationTitle("Timetable")
        .navigationBarTitleDisplayMode(.automatic)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                HStack {
                    Button(action: {
                        onNavigate(.search)
                    }) {
                        Image(systemName: "magnifyingglass")
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                            .frame(width: 40, height: 40)
                    }
                    
                    Button(action: {
                        timetableMode = timetableMode == .list ? .grid : .list
                    }) {
                        Image(systemName: timetableMode == .list ? "square.grid.2x2" : "list.bullet")
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                            .frame(width: 40, height: 40)
                    }
                }
            }
        }
        .navigationDestination(item: $selectedTimetableItem) { item in
            TimetableDetailScreen(timetableItem: item)
        }
        .toolbarBackground(.hidden, for: .navigationBar)
        .task {
            await presenter.loadInitial()
        }
    }
    
    private func toggleFavorite(timetableItem: any TimetableItem, adjustedLocationPoint: CGPoint?) {
        targetLocationPoint = adjustedLocationPoint
        targetTimetableItemId = timetableItem.id.value

        if targetTimetableItemId != nil {
            withAnimation(.easeOut(duration: 1)) {
                animationProgress = 1
            }
            Task {
                try await Task.sleep(nanoseconds: 1_000_000_000)
                targetTimetableItemId = nil
                targetLocationPoint = nil
                self.animationProgress = 0
            }
        }
    }
}

#Preview {
    HomeScreen()
}

private extension DayTab {
    var model: DroidKaigi2024Day {
        switch self {
        case .day1:
            .conferenceDay1
        case .day2:
            .conferenceDay2
        }
    }
}
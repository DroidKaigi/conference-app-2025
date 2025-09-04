import Component
import Dependencies
import Model
import Observation
import Presentation
import SwiftUI
import Theme

public struct HomeScreen: View {
    @State private var presenter = HomePresenter()
    @State private var timetableMode: TimetableMode = .list
    @State private var selectedDay: DayTab = .day1

    let onNavigate: (HomeNavigationDestination) -> Void

    public init(onNavigate: @escaping (HomeNavigationDestination) -> Void = { _ in }) {
        self.onNavigate = onNavigate
    }

    public var body: some View {
        let timetableItems = presenter.timetable.dayTimetable[selectedDay.model] ?? []

        Group {
            switch timetableMode {
            case .list:
                TimetableListView(
                    selectedDay: $selectedDay,
                    timetableItems: timetableItems,
                    onItemTap: { item in
                        onNavigate(.timetableDetail(item))
                    },
                    onFavoriteTap: { item, _ in
                        presenter.timetable.toggleFavorite(item)
                    },
                )
            case .grid:
                TimetableGridView(
                    selectedDay: $selectedDay,
                    timetableItems: timetableItems,
                    rooms: presenter.timetable.rooms,
                    onItemTap: { item in
                        onNavigate(.timetableDetail(item))
                    }
                )
            }
        }
        .background(
            Image("background_night", bundle: .module)
                .resizable()
                .edgesIgnoringSafeArea(.all)
                .accessibilityLabel(String(localized: "Conference background", bundle: .module))
        )
        .navigationTitle(String(localized: "Timetable", bundle: .module))
        .navigationBarTitleDisplayMode(.automatic)
        .toolbar {
            ToolbarItemGroup(placement: .topBarTrailing) {
                Button(
                    action: {
                        onNavigate(.search)
                    },
                    label: {
                        AssetImages.icSearch.swiftUIImage
                            .resizable()
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                            .frame(width: 24, height: 24)
                            .accessibilityLabel(String(localized: "Search sessions", bundle: .module))
                    })
                Button(
                    action: {
                        timetableMode = timetableMode == .list ? .grid : .list
                    },
                    label: {
                        (timetableMode == .list ? AssetImages.icGridView : AssetImages.icViewTimeline)
                            .swiftUIImage
                            .resizable()
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                            .frame(width: 24, height: 24)
                            .accessibilityLabel(
                                timetableMode == .list
                                    ? String(localized: "Switch to grid view", bundle: .module)
                                    : String(localized: "Switch to list view", bundle: .module))
                    })
            }
        }
        .onAppear {
            presenter.loadInitial()
        }
    }
}

#Preview {
    HomeScreen()
}

extension DayTab {
    fileprivate var model: DroidKaigi2025Day {
        switch self {
        case .day1:
            .conferenceDay1
        case .day2:
            .conferenceDay2
        }
    }
}

import Model
import Observation
import SwiftUI
import Theme
import Presentation

public struct HomeScreen: View {
    @State private var presenter = HomePresenter()
    @State private var animationProgress: CGFloat = 0
    @State private var targetTimetableItemId: String?
    @State private var targetLocationPoint: CGPoint?
    @State private var timetableMode: TimetableMode = .list
    @State private var selectedDay: DayTab = .day1

    public init() {}

    public var body: some View {
        let timetableItems = presenter.timetable.dayTimetable[selectedDay.model] ?? []

        NavigationStack {
            ZStack {
                VStack(spacing: 0) {
                    dayTabBar
                    
                    if timetableMode == .list {
                        TimetableListView(
                            timetableItems: timetableItems,
                            onItemTap: { item in
                                presenter.timetableItemTapped(item)
                            },
                            onFavoriteTap: { item, _ in
                                presenter.timetable.toggleFavorite(item)
                            },
                            animationTrigger: { timetableItem, location in
                                toggleFavorite(timetableItem: timetableItem, adjustedLocationPoint: location)
                            }
                        )
                    } else {
                        TimetableGridView(
                            timetableItems: timetableItems,
                            onItemTap: { item in
                                presenter.timetableItemTapped(item)
                            },
                            isFavorite: { itemId in
                                presenter.timetable.isFavorite(itemId)
                            }
                        )
                    }
                }
                .background(Color(.systemBackground))
                
                FavoriteAnimationView(
                    targetTimetableItemId: targetTimetableItemId,
                    targetLocationPoint: targetLocationPoint,
                    animationProgress: animationProgress
                )
            }
            .navigationTitle("Timetable")
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    HStack {
                        Button(action: {
                            presenter.searchTapped()
                        }) {
                            Image(systemName: "magnifyingglass")
                                .foregroundColor(Color(.label))
                                .frame(width: 40, height: 40)
                        }
                        
                        Button(action: {
                            timetableMode = timetableMode == .list ? .grid : .list
                        }) {
                            Image(systemName: timetableMode == .list ? "square.grid.2x2" : "list.bullet")
                                .foregroundColor(Color(.label))
                                .frame(width: 40, height: 40)
                        }
                    }
                }
            }
        }
        .task {
            await presenter.loadInitial()
        }
    }
    
    private var dayTabBar: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 20) {
                ForEach(DayTab.allCases) { day in
                    Button(action: {
                        selectedDay = day
                    }) {
                        VStack(spacing: 4) {
                            Text(day.rawValue)
                                .font(.system(size: 16, weight: .medium))
                                .foregroundColor(selectedDay == day ? Color.blue : Color(.label))
                            
                            if selectedDay == day {
                                Rectangle()
                                    .fill(Color.blue)
                                    .frame(height: 2)
                            } else {
                                Rectangle()
                                    .fill(Color.clear)
                                    .frame(height: 2)
                            }
                        }
                    }
                    .buttonStyle(PlainButtonStyle())
                }
                
                Spacer()
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 8)
        }
        .background(Color(.secondarySystemBackground))
    }
    
    private func toggleFavorite(timetableItem: TimetableItem, adjustedLocationPoint: CGPoint?) {
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
                animationProgress = 0
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

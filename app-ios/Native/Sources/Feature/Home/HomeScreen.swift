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

    public init() {}

    public var body: some View {
        NavigationStack {
            ZStack {
                VStack(spacing: 0) {
                    dayTabBar
                    
                    if presenter.timetable.timetableMode == .list {
                        timetableListView
                    } else {
                        timetableGridView
                    }
                }
                .background(Color(.systemBackground))
                
                makeHeartAnimationView()
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
                            presenter.timetable.toggleViewMode()
                        }) {
                            Image(systemName: presenter.timetable.timetableMode == .list ? "square.grid.2x2" : "list.bullet")
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
                        presenter.timetable.selectDay(day)
                    }) {
                        VStack(spacing: 4) {
                            Text(day.rawValue)
                                .font(.system(size: 16, weight: .medium))
                                .foregroundColor(presenter.timetable.selectedDay == day ? Color.blue : Color(.label))
                            
                            if presenter.timetable.selectedDay == day {
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
    
    private var timetableListView: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(presenter.timetable.timetableItems) { timeGroup in
                    TimeGroupList(
                        timeGroup: timeGroup,
                        onItemTap: { item in
                            presenter.timetableItemTapped(item)
                        },
                        onFavoriteTap: { item, location in
                            presenter.timetable.toggleFavorite(item)
                            
                            if !item.isFavorited {
                                toggleFavorite(timetableItem: item.timetableItem, adjustedLocationPoint: location)
                            }
                        }
                    )
                    
                    if timeGroup != presenter.timetable.timetableItems.last {
                        Divider()
                            .padding(.horizontal, 16)
                    }
                }
            }
            .padding(.bottom, 60)
        }
    }
    
    private var timetableGridView: some View {
        Text("Grid view not implemented yet")
            .foregroundColor(Color(.secondaryLabel))
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
    
    @ViewBuilder
    private func makeHeartAnimationView() -> some View {
        GeometryReader { geometry in
            if targetTimetableItemId != nil {
                Image(systemName: "heart.fill")
                    .foregroundColor(Color.blue.opacity(0.8))
                    .frame(width: 24, height: 24)
                    .position(animationPosition(geometry: geometry))
                    .opacity(1 - animationProgress)
                    .zIndex(99)
            }
        }
    }
    
    private func animationPosition(geometry: GeometryProxy) -> CGPoint {
        let globalGeometrySize = geometry.frame(in: .global).size
        let defaultGeometrySize = geometry.size
        
        let startPositionY = targetLocationPoint?.y ?? 0
        let endPositionY = defaultGeometrySize.height - 25
        let targetY = startPositionY + (endPositionY - startPositionY) * animationProgress
        
        let adjustedPositionX = animationProgress * (globalGeometrySize.width / 2 - globalGeometrySize.width + 50)
        let targetX = defaultGeometrySize.width - 50 + adjustedPositionX
        
        return CGPoint(x: targetX, y: targetY)
    }
    
    private func toggleFavorite(timetableItem: TimetableItem, adjustedLocationPoint: CGPoint?) {
        targetLocationPoint = adjustedLocationPoint
        targetTimetableItemId = timetableItem.id
        
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

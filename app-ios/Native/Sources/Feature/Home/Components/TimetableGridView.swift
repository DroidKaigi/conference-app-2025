import Component
import Model
import Presentation
import SwiftUI
import Theme

struct TimetableGridView: View {
    @Binding var selectedDay: DayTab
    let timetableItems: [TimetableTimeGroupItems]
    let rooms: [Room]
    let onItemTap: (TimetableItemWithFavorite) -> Void

    @State private var now: Date = Date()
    @State private var timer: Timer? = nil

    @State private var headerHeight: CGFloat = 0
    @State private var timeLabelHeight: CGFloat = 0

    private let layoutConfig = TimetableLayoutConfig()
    private var layoutCalculator: TimetableLayoutCalculator {
        .init(
            config: layoutConfig,
            rooms: rooms,
            dayStart: dayStart,
            dayEnd: dayEnd,
        )
    }

    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: 0) {
                DayTabBar(selectedDay: $selectedDay)
                    .padding(.horizontal)

                timetableGridView

                // Bottom padding for tab bar
                Color.clear.padding(.bottom, 60)
            }
        }
        .onAppear {
            timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
                Task { @MainActor in
                    self.now = Date()
                }
            }
        }
        .onDisappear {
            timer?.invalidate()
        }
    }

    private var timetableGridView: some View {
        HStack(alignment: .top, spacing: 0) {
            timeAxis
                .frame(
                    width: layoutConfig.timeAxisWidth, height: layoutCalculator.timetableHeight(),
                    alignment: .topTrailing
                )
                .padding(.trailing, 4)

            ScrollView(.horizontal) {
                VStack(spacing: 0) {
                    roomHeaderRow

                    ZStack(alignment: .topLeading) {
                        roomLines
                        hourLines
                        sessionCards
                        currentTimeLine
                    }
                    .frame(
                        width: layoutCalculator.timetableWidth(), height: layoutCalculator.timetableHeight(),
                        alignment: .topLeading)
                }
            }

            .padding(.trailing)
        }
    }

    private var roomHeaderRow: some View {
        HStack(spacing: layoutConfig.roomSpacing) {
            Color.clear
                .gridCellUnsizedAxes([.horizontal, .vertical])

            ForEach(rooms, id: \.id) { room in
                Text(room.displayName)
                    .font(Typography.titleMedium)
                    .foregroundStyle(room.roomTheme.primaryColor)
                    .frame(width: layoutConfig.roomWidth)
                    .padding(EdgeInsets(top: 8, leading: 0, bottom: 8, trailing: 0))
            }
        }
        .background(
            GeometryReader { geometry in
                // Get Room Header Height
                Spacer()
                    .onAppear { headerHeight = geometry.size.height }
                    .onChange(of: geometry.size.height) { headerHeight = geometry.size.height }
            }
        )
    }

    private var timeAxis: some View {
        VStack(spacing: 0) {
            // Space same as the room header
            Spacer().frame(height: headerHeight)

            // Get time label height. DO NOT show.
            Text("00:00")
                .font(Typography.labelMedium)
                .background(
                    GeometryReader { geometry in
                        Spacer()
                            .onAppear { timeLabelHeight = geometry.size.height }
                            .onChange(of: geometry.size.height) { timeLabelHeight = geometry.size.height }
                    }
                )
                .hidden()
                .frame(height: 0)

            if let dayStart = dayStart, let dayEnd = dayEnd {
                let totalMinutes = Int(dayEnd.timeIntervalSince(dayStart) / 60)
                ForEach(0...totalMinutes / 60, id: \.self) { hourIndex in
                    let labelDate = Calendar.current.date(byAdding: .minute, value: hourIndex * 60, to: dayStart)!
                    Text(labelDate.formatted(date: .omitted, time: .shortened))
                        .font(Typography.labelMedium)
                        .frame(height: layoutConfig.heightOfMinute * 60, alignment: .top)
                        .offset(y: -timeLabelHeight / 2)
                }
            }
        }
    }

    @ViewBuilder
    private var roomLines: some View {
        ForEach(0...rooms.count, id: \.self) { index in
            let x =
                CGFloat(index) * (layoutConfig.roomWidth + layoutConfig.roomSpacing) - (layoutConfig.roomSpacing / 2)
            Rectangle()
                .fill(Color.gray.opacity(0.3))
                .frame(width: 1, height: layoutCalculator.timetableHeight())
                .offset(x: x)
        }
    }

    @ViewBuilder
    private var hourLines: some View {
        if let dayStart = dayStart, let dayEnd = dayEnd {
            let totalMinutes = Int(dayEnd.timeIntervalSince(dayStart) / 60)
            let hourNum = totalMinutes / 60
            ForEach(0...hourNum, id: \.self) { hourIndex in
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
                    .offset(y: CGFloat(hourIndex * 60) * layoutConfig.heightOfMinute)
            }
        }
    }

    @ViewBuilder
    private var currentTimeLine: some View {
        if let dayStart = dayStart,
            let dayEnd = dayEnd
        {
            if now >= dayStart && now <= dayEnd {
                Rectangle()
                    .fill(Color.red)
                    .frame(height: 2)
                    .frame(maxWidth: .infinity)
                    .offset(y: layoutCalculator.offsetY(for: now, base: dayStart))
            }
        }
    }

    @ViewBuilder
    private var sessionCards: some View {
        if let dayStart = dayStart {
            ForEach(timelineItems, id: \.id) { item in
                let left = layoutCalculator.offsetX(for: item.timetableItem)
                let top = layoutCalculator.offsetY(for: item.timetableItem.startsAt, base: dayStart)
                let width = layoutCalculator.sessionWidth(for: item.timetableItem)
                let height = layoutCalculator.sessionHeight(for: item.timetableItem)

                TimetableGridCard(
                    timetableItem: item.timetableItem,
                    onTap: { _ in onItemTap(item) }
                )
                .frame(width: width, height: height)
                .offset(x: left, y: top)
            }
        }
    }

    // MARK: - Helpers
    private var timelineItems: [TimetableItemWithFavorite] {
        timetableItems.flatMap(\.items)
    }

    private var dayStart: Date? {
        guard let earliest = timelineItems.map({ $0.timetableItem.startsAt }).min() else { return nil }
        // Round to hour unit
        return Calendar.current.date(
            bySettingHour: Calendar.current.component(.hour, from: earliest),
            minute: 0,
            second: 0,
            of: earliest
        )
    }

    private var dayEnd: Date? {
        timelineItems.map({ $0.timetableItem.endsAt }).max()
    }
}

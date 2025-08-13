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

    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: 0) {
                DayTabBar(selectedDay: $selectedDay)
                    .padding(.horizontal)

                horizontalGridScrollView

                // Bottom padding for tab bar
                Color.clear.padding(.bottom, 60)
            }
        }
    }

    private var horizontalGridScrollView: some View {
        ScrollView(.horizontal) {
            Grid(alignment: .leading, horizontalSpacing: 4, verticalSpacing: 2) {
                roomHeaderRow
                DashedDivider(axis: .horizontal)
                timeBlockRows
            }
            .fixedSize(horizontal: false, vertical: true)
            .padding(.trailing)
        }
    }

    private var roomHeaderRow: some View {
        GridRow {
            Color.clear
                .gridCellUnsizedAxes([.horizontal, .vertical])

            ForEach(rooms, id: \.id) { room in
                roomHeaderCell(room: room)
            }
        }
    }

    private func roomHeaderCell(room: Room) -> some View {
        Text(room.displayName)
            .font(Typography.titleMedium)
            .foregroundStyle(room.type.color)
            .frame(width: 192)
    }

    @ViewBuilder
    private var timeBlockRows: some View {
        ForEach(timetableItems) { timeBlock in
            timeBlockRow(timeBlock: timeBlock)
            DashedDivider(axis: .horizontal)
        }
    }

    private func timeBlockRow(timeBlock: TimetableTimeGroupItems) -> some View {
        GridRow {
            timeColumnView(timeBlock: timeBlock)
            sessionsForTimeBlock(timeBlock: timeBlock)
        }
    }

    private func timeColumnView(timeBlock: TimetableTimeGroupItems) -> some View {
        VStack {
            Text(timeBlock.startsTimeString)
                .font(Typography.labelMedium)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
            Spacer()
        }
        .frame(width: 40, height: 153)
    }

    @ViewBuilder
    private func sessionsForTimeBlock(timeBlock: TimetableTimeGroupItems) -> some View {
        if timeBlock.isLunchTime() {
            lunchTimeView(timeBlock: timeBlock)
        } else {
            regularSessionsView(timeBlock: timeBlock)
        }
    }

    @ViewBuilder
    private func lunchTimeView(timeBlock: TimetableTimeGroupItems) -> some View {
        if let lunchItem = timeBlock.items.first {
            TimetableGridCard(
                timetableItem: lunchItem.timetableItem,
                cellCount: rooms.count,
                onTap: { item in
                    onItemTap(lunchItem)
                }
            )
            .gridCellColumns(rooms.count)
        }
    }

    @ViewBuilder
    private func regularSessionsView(timeBlock: TimetableTimeGroupItems) -> some View {
        ForEach(rooms, id: \.self) { room in
            sessionCell(for: room, in: timeBlock)
        }
    }

    @ViewBuilder
    private func sessionCell(for room: Room, in timeBlock: TimetableTimeGroupItems) -> some View {
        if let item = timeBlock.getItem(for: room) {
            TimetableGridCard(
                timetableItem: item.timetableItem,
                cellCount: 1,
                onTap: { _ in
                    onItemTap(item)
                }
            )
        } else {
            Color.clear
                .frame(width: 192, height: 153)
        }
    }
}

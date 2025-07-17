import SwiftUI
import Model
import Presentation

struct TimetableGridView: View {
    let timetableItems: [TimetableTimeGroupItems]
    let onItemTap: (TimetableItemWithFavorite) -> Void
    let isFavorite: (String) -> Bool
    
    // TODO: Get rooms from actual timetable data
    private let rooms: [Room] = []
    
    var body: some View {
        ScrollView([.horizontal, .vertical]) {
            Grid(alignment: .leading, horizontalSpacing: 4, verticalSpacing: 2) {
                // Header row with room names
                GridRow {
                    Color.clear
                        .gridCellUnsizedAxes([.horizontal, .vertical])
                    
                    ForEach(rooms, id: \.id) { room in
                        Text(room.displayName)
                            .font(.system(size: 16, weight: .medium))
                            .foregroundColor(room.type.color)
                            .frame(width: 192)
                    }
                }
                
                DashedDivider(axis: .horizontal)
                
                // Time blocks with sessions
                ForEach(timetableItems) { timeBlock in
                    GridRow {
                        // Time column
                        VStack {
                            Text(timeBlock.startsTimeString)
                                .font(.system(size: 12, weight: .medium))
                                .foregroundColor(Color(.label))
                            Spacer()
                        }
                        .frame(width: 40, height: 153)
                        
                        // Sessions for each room
                        if timeBlock.isLunchTime() {
                            // Lunch spans all columns
                            if let lunchItem = timeBlock.items.first {
                                TimetableGridCard(
                                    timetableItem: lunchItem.timetableItem,
                                    cellCount: rooms.count,
                                    onTap: { item in
                                        onItemTap(
                                            TimetableItemWithFavorite(
                                                timetableItem: item,
                                                isFavorited: isFavorite(item.id.value)
                                            )
                                        )
                                    }
                                )
                                .gridCellColumns(rooms.count)
                            }
                        } else {
                            // Regular sessions
                            ForEach(rooms, id: \.self) { room in
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
                                        .background(Color(.systemGray6))
                                        .cornerRadius(4)
                                }
                            }
                        }
                    }
                    
                    DashedDivider(axis: .horizontal)
                }
            }
            .fixedSize(horizontal: false, vertical: true)
            .padding(.trailing)
            
            // Bottom padding for tab bar
            Color.clear.padding(.bottom, 60)
        }
    }
}

import Component
import Dependencies
import DependencyExtra
import SwiftUI
import Theme

public struct EventMapScreen: View {
    @State private var presenter = EventMapPresenter()
    @State private var selectedFloorMap: FloorMap? = .first
    @Dependency(\.safari) var safari

    public init() {}

    public var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                // Description
                Text(String(localized: "EventDescription", bundle: .module))
                    .font(Typography.bodyMedium)
                    .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 10)
                // Floor selector
                FloorMapSelector(selected: $selectedFloorMap)
                    .onChange(of: selectedFloorMap) { _, newValue in
                        if let floor = newValue {
                            presenter.selectFloorMap(floor)
                        }
                    }

                // Map image
                if selectedFloorMap != nil {
                    // TODO: Replace with actual floor map images
                    Image(presenter.selectedFloorMap.image, bundle: .module)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(maxWidth: .infinity)
                        .padding(.all, 16)
                }

                // Events section
                VStack(alignment: .leading, spacing: 0) {
                    ForEach(presenter.eventMap.events, id: \.self) { event in
                        EventItem(event: event) { url in
                            Task {
                                await safari(url)
                            }
                        }
                        if event.hashValue != presenter.eventMap.events.last?.hashValue {
                            Divider()
                                .padding(.horizontal, 16)
                                .foregroundStyle(AssetColors.outlineVariant.swiftUIColor)
                        }
                    }
                }
                .padding(.bottom, Constant.bottomPadding)  // Tab bar padding
            }
        }
        .background(AssetColors.background.swiftUIColor)
        .navigationTitle(String(localized: "EventMap", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .onAppear {
            presenter.loadInitial()
            if selectedFloorMap == nil {
                selectedFloorMap = .first
            }
        }
    }
}

#Preview {
    EventMapScreen()
}

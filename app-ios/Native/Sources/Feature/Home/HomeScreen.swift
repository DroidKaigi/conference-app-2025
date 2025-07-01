import Observation
import SwiftUI

public struct HomeScreen: View {
    @State private var presenter = HomePresenter()

    public init() {}

    public var body: some View {
        let timetableData = presenter.timetable

        Text("This is Home \(timetableData.timetable)")
            .task {
                await presenter.loadInitial()
            }
    }
}

#Preview {
    HomeScreen()
}

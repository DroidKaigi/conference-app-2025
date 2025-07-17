import Dependencies
import Model
import shared

struct RootPresenter {
    func prepareWindow() {
        prepareDependencies {
            KMPDependencies.prepareKMPDependencies(&$0)
        }
    }
}

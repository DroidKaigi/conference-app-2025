import SwiftUI

extension View {
    /// Applies a closure to modify the current view.
    /// - Parameter closure: A closure that takes the current view as input and returns a modified view.
    /// - Returns: A view that has been modified by the provided closure.
    public func closureModifier(@ViewBuilder _ closure: (Self) -> some View) -> some View {
        closure(self)
    }
}

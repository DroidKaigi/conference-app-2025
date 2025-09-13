import Foundation

public enum Constant {
    /// for Custom Tab Bar space
    public static var bottomPadding: CGFloat {
        if #available(iOS 26.0, *) {
            16
        } else {
            80
        }
    }
}

import Foundation

public struct TimetableAsset: Equatable, Sendable {
    public let videoUrl: String?
    public let slideUrl: String?
    
    public init(videoUrl: String? = nil, slideUrl: String? = nil) {
        self.videoUrl = videoUrl
        self.slideUrl = slideUrl
    }
}
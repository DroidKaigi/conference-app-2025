import Dependencies
import UseCase
import Observation
import Model
import Foundation
import SwiftUI

// Enums for UI state
public enum DayTab: String, CaseIterable, Identifiable {
    public var id: RawValue { rawValue }
    
    case day1 = "Day 1"
    case day2 = "Day 2"
}

public enum TimetableMode {
    case list
    case grid
}

@Observable
public final class TimetableProvider {
    @ObservationIgnored
    @Dependency(\.timetableUseCase) private var timeteableUseCase

    public var timetable: Timetable?
    
    // UI State
    public var selectedDay: DayTab = .day1
    public var timetableMode: TimetableMode = .list
    public var favoriteIds: Set<String> = []
    
    // Computed properties
    public var timetableItems: [TimetableTimeGroupItems] {
        let sessions = selectedDay == .day1 ? SampleTimetableData.day1Sessions : SampleTimetableData.day2Sessions
        
        return sessions.map { timeGroup in
            TimetableTimeGroupItems(
                startsTimeString: timeGroup.startsTimeString,
                endsTimeString: timeGroup.endsTimeString,
                items: timeGroup.items.map { item in
                    TimetableItemWithFavorite(
                        timetableItem: item.timetableItem,
                        isFavorited: favoriteIds.contains(item.timetableItem.id)
                    )
                }
            )
        }
    }

    public init() {}

    @MainActor
    public func fetchTimetable() async {
        do {
            timetable = try await timeteableUseCase.load()
        } catch {
            print(error)
        }
    }
    
    // UI Actions
    public func selectDay(_ day: DayTab) {
        selectedDay = day
    }
    
    public func toggleViewMode() {
        switch timetableMode {
        case .list:
            timetableMode = .grid
        case .grid:
            timetableMode = .list
        }
    }
    
    public func toggleFavorite(_ item: TimetableItemWithFavorite) {
        if item.isFavorited {
            favoriteIds.remove(item.timetableItem.id)
        } else {
            favoriteIds.insert(item.timetableItem.id)
        }
    }
    
    public func isFavorite(_ itemId: String) -> Bool {
        favoriteIds.contains(itemId)
    }
}

public struct SampleTimetableData {
    private static func createDate(hour: Int, minute: Int) -> Date {
        let calendar = Calendar.current
        var components = calendar.dateComponents([.year, .month, .day], from: Date())
        components.hour = hour
        components.minute = minute
        return calendar.date(from: components) ?? Date()
    }
    
    public static let day1Sessions = [
        TimetableTimeGroupItems(
            startsTimeString: "09:00",
            endsTimeString: "09:30",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Registration & Welcome Coffee",
                        startsAt: createDate(hour: 9, minute: 0),
                        endsAt: createDate(hour: 9, minute: 30),
                        room: .roomA,
                        description: "Check-in and enjoy welcome refreshments"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "10:00",
            endsTimeString: "10:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Opening Keynote: The Future of Android Development",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "John Smith")],
                        description: "Explore the latest trends and future directions in Android development",
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Jetpack Compose: Advanced Techniques",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Jane Doe"), Speaker(name: "Bob Johnson")],
                        description: "Deep dive into advanced Jetpack Compose patterns and best practices",
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Security Best Practices",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Sarah Chen")],
                        description: "Learn how to secure your Android applications",
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "11:00",
            endsTimeString: "11:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Kotlin Coroutines in Practice",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "Alice Cooper")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Building Accessible Android Apps",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "David Lee")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Performance Optimization Workshop",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Emma Wilson")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Material You: Implementing Dynamic Color",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Carlos Rodriguez")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "12:00",
            endsTimeString: "13:00",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Lunch Break",
                        startsAt: createDate(hour: 12, minute: 0),
                        endsAt: createDate(hour: 13, minute: 0),
                        room: .roomA,
                        description: "Enjoy lunch and network with fellow developers"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "13:00",
            endsTimeString: "13:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "State Management in Compose",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "Lisa Anderson")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Testing Strategies",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Mark Thompson"), Speaker(name: "Nina Patel")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "GraphQL with Android",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Ryan Kim")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "CI/CD for Android Projects",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Sophie Martin")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "14:00",
            endsTimeString: "14:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Modern App Architecture",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "Tom Wilson")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Kotlin Multiplatform Mobile",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Yuki Tanaka")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Camera2 API Deep Dive",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Alex Zhang")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Debugging Like a Pro",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Rachel Green")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "15:00",
            endsTimeString: "15:30",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Coffee Break",
                        startsAt: createDate(hour: 15, minute: 0),
                        endsAt: createDate(hour: 15, minute: 30),
                        room: .roomA,
                        description: "Take a break and enjoy some refreshments"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "15:30",
            endsTimeString: "16:20",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android 14 New Features",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomA,
                        speakers: [Speaker(name: "Daniel Park")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Building Offline-First Apps",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomB,
                        speakers: [Speaker(name: "Kenji Yamamoto"), Speaker(name: "Maria Garcia")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Custom Views in Compose",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomC,
                        speakers: [Speaker(name: "Oliver James")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Wear OS Development",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomD,
                        speakers: [Speaker(name: "Isabella Lopez")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "16:30",
            endsTimeString: "17:20",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Panel Discussion: Future of Mobile Development",
                        startsAt: createDate(hour: 16, minute: 30),
                        endsAt: createDate(hour: 17, minute: 20),
                        room: .roomA,
                        speakers: [
                            Speaker(name: "John Smith"),
                            Speaker(name: "Jane Doe"),
                            Speaker(name: "Alice Cooper"),
                            Speaker(name: "Tom Wilson")
                        ],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Game Development",
                        startsAt: createDate(hour: 16, minute: 30),
                        endsAt: createDate(hour: 17, minute: 20),
                        room: .roomB,
                        speakers: [Speaker(name: "Lucas Wang")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Machine Learning on Android",
                        startsAt: createDate(hour: 16, minute: 30),
                        endsAt: createDate(hour: 17, minute: 20),
                        room: .roomC,
                        speakers: [Speaker(name: "Priya Sharma")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "17:30",
            endsTimeString: "18:00",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Day 1 Closing Remarks",
                        startsAt: createDate(hour: 17, minute: 30),
                        endsAt: createDate(hour: 18, minute: 0),
                        room: .roomA,
                        speakers: [Speaker(name: "Conference Organizers")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "18:00",
            endsTimeString: "20:00",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Networking Party",
                        startsAt: createDate(hour: 18, minute: 0),
                        endsAt: createDate(hour: 20, minute: 0),
                        room: .roomA,
                        description: "Join us for drinks and networking"
                    )
                )
            ]
        )
    ]
    
    public static let day2Sessions = [
        TimetableTimeGroupItems(
            startsTimeString: "09:30",
            endsTimeString: "10:00",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Day 2 Welcome & Recap",
                        startsAt: createDate(hour: 9, minute: 30),
                        endsAt: createDate(hour: 10, minute: 0),
                        room: .roomA,
                        speakers: [Speaker(name: "Conference Host")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "10:00",
            endsTimeString: "10:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Modern Android Architecture",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "Michael Brown")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Testing Strategies for Android",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Sarah Davis")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Advanced Kotlin Features",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Peter Chang")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android TV Development",
                        startsAt: createDate(hour: 10, minute: 0),
                        endsAt: createDate(hour: 10, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Julia Roberts")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "11:00",
            endsTimeString: "11:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Dependency Injection with Hilt",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "Kevin Liu")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "App Performance Monitoring",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Hiroshi Nakamura")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Building for Foldable Devices",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Amanda White"), Speaker(name: "Chris Black")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Auto Integration",
                        startsAt: createDate(hour: 11, minute: 0),
                        endsAt: createDate(hour: 11, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Roberto Silva")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "12:00",
            endsTimeString: "13:00",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Lunch Break",
                        startsAt: createDate(hour: 12, minute: 0),
                        endsAt: createDate(hour: 13, minute: 0),
                        room: .roomA,
                        description: "Lunch and networking session"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "13:00",
            endsTimeString: "13:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Advanced Animation Techniques",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "Jennifer Lee")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Libraries You Should Know",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Takeshi Yamada")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Reactive Programming with RxJava",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "George Miller")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Building Custom Gradle Plugins",
                        startsAt: createDate(hour: 13, minute: 0),
                        endsAt: createDate(hour: 13, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Anna Schmidt")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "14:00",
            endsTimeString: "14:50",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "App Modularization Strategies",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomA,
                        speakers: [Speaker(name: "David Johnson")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Studio Tips & Tricks",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomB,
                        speakers: [Speaker(name: "Yui Suzuki")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Building for Billions",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomC,
                        speakers: [Speaker(name: "Mohammed Ali")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "App Bundle & Dynamic Delivery",
                        startsAt: createDate(hour: 14, minute: 0),
                        endsAt: createDate(hour: 14, minute: 50),
                        room: .roomD,
                        speakers: [Speaker(name: "Elena Petrova")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "15:00",
            endsTimeString: "15:30",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Coffee Break",
                        startsAt: createDate(hour: 15, minute: 0),
                        endsAt: createDate(hour: 15, minute: 30),
                        room: .roomA,
                        description: "Afternoon refreshments"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "15:30",
            endsTimeString: "16:20",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Privacy & Security in Android 14",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomA,
                        speakers: [Speaker(name: "Steven Park")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Monetization Strategies",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomB,
                        speakers: [Speaker(name: "Akiko Tanaka"), Speaker(name: "James Wilson")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "AR/VR on Android",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomC,
                        speakers: [Speaker(name: "Marcus Jones")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Android Backup & Restore",
                        startsAt: createDate(hour: 15, minute: 30),
                        endsAt: createDate(hour: 16, minute: 20),
                        room: .roomD,
                        speakers: [Speaker(name: "Linda Garcia")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "16:30",
            endsTimeString: "17:20",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Code Review Best Practices",
                        startsAt: createDate(hour: 16, minute: 30),
                        endsAt: createDate(hour: 17, minute: 20),
                        room: .roomA,
                        speakers: [Speaker(name: "Robert Taylor")],
                        language: "EN"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Community Building Workshop",
                        startsAt: createDate(hour: 16, minute: 30),
                        endsAt: createDate(hour: 17, minute: 20),
                        room: .roomB,
                        speakers: [Speaker(name: "Masako Sato")],
                        language: "JA"
                    )
                ),
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Open Source Contribution Guide",
                        startsAt: createDate(hour: 16, minute: 30),
                        endsAt: createDate(hour: 17, minute: 20),
                        room: .roomC,
                        speakers: [Speaker(name: "Frank Chen")],
                        language: "EN"
                    )
                )
            ]
        ),
        TimetableTimeGroupItems(
            startsTimeString: "17:30",
            endsTimeString: "18:00",
            items: [
                TimetableItemWithFavorite(
                    timetableItem: TimetableItem(
                        title: "Closing Keynote & Awards",
                        startsAt: createDate(hour: 17, minute: 30),
                        endsAt: createDate(hour: 18, minute: 0),
                        room: .roomA,
                        speakers: [Speaker(name: "DroidKaigi Team")],
                        description: "Conference closing ceremony and community awards",
                        language: "EN"
                    )
                )
            ]
        )
    ]
}

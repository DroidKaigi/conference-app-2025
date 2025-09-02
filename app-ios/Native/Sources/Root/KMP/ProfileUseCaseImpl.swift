@preconcurrency import Foundation
import Model
import UseCase

// TODO: Replace KMP connection
struct ProfileUseCaseImpl {
    private let nameKey = "name"
    private let occupationKey = "occupation"
    private let urlKey = "url"
    private let imageKey = "image"
    private let cardVariantKey = "cardVariant"

    func load() -> any AsyncSequence<Model.Profile?, Never> {
        return UserDefaultsProfilePublisher()
    }

    func save(_ profile: Model.Profile) async {
        let userDefaults = UserDefaults.standard
        userDefaults.set(profile.name, forKey: nameKey)
        userDefaults.set(profile.occupation, forKey: occupationKey)
        userDefaults.set(profile.url.absoluteString, forKey: urlKey)
        userDefaults.set(profile.image, forKey: imageKey)
        userDefaults.set(profile.cardVariant.rawValue, forKey: cardVariantKey)
    }
}

extension Model.Profile {
    init?(userDefaults: UserDefaults) {
        guard let name = userDefaults.string(forKey: "name"),
            let occupation = userDefaults.string(forKey: "occupation"),
            let urlString = userDefaults.string(forKey: "url"),
            let url = URL(string: urlString),
            let imageData = userDefaults.data(forKey: "image"),
            let cardVariantString = userDefaults.string(forKey: "cardVariant"),
            let cardVariant = Model.ProfileCardVariant(rawValue: cardVariantString)
        else {
            return nil
        }

        self = .init(name: name, occupation: occupation, url: url, image: imageData, cardVariant: cardVariant)
    }
}

extension UserDefaults: @unchecked @retroactive Sendable {}

struct UserDefaultsProfilePublisher: AsyncSequence {
    typealias Element = Model.Profile?
    typealias AsyncIterator = Iterator

    private let userDefaults: UserDefaults

    init(userDefaults: UserDefaults = .standard) {
        self.userDefaults = userDefaults
    }

    func makeAsyncIterator() -> Iterator {
        Iterator(userDefaults: userDefaults)
    }

    struct Iterator: AsyncIteratorProtocol {
        let userDefaults: UserDefaults
        var stream: AsyncStream<Model.Profile?>
        var streamIterator: AsyncStream<Model.Profile?>.Iterator

        init(userDefaults: UserDefaults) {
            self.userDefaults = userDefaults

            self.stream = AsyncStream<Model.Profile?> { [userDefaults] continuation in
                let center = NotificationCenter.default
                // observerはローカル変数で管理
                let observer = center.addObserver(
                    forName: UserDefaults.didChangeNotification, object: userDefaults, queue: nil
                ) { [userDefaults] _ in
                    continuation.yield(Model.Profile(userDefaults: userDefaults))
                }
                // 初期値も流す
                continuation.yield(Model.Profile(userDefaults: userDefaults))

                continuation.onTermination = { [observer] _ in
                    center.removeObserver(observer)
                }
            }
            self.streamIterator = stream.makeAsyncIterator()
        }

        mutating func next() async -> Model.Profile?? {
            await streamIterator.next()
        }
    }
}

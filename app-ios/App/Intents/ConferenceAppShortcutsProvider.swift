import AppIntents

struct ConferenceAppShortcutsProvider: AppShortcutsProvider {
    @AppShortcutsBuilder static var appShortcuts: [AppShortcut] {
        AppShortcut(
            intent: GetSessionsAtDateTimeIntent(),
            phrases: [
                "\(.applicationName)で指定日時のセッションを取得"
            ],
            shortTitle: "Get sessions",
            systemImageName: "calendar",
        )
        AppShortcut(
            intent: GetOngoingSessionsIntent(),
            phrases: [
                "\(.applicationName)で現在のセッションを取得"
            ],
            shortTitle: "Get ongoing sessions",
            systemImageName: "calendar",
        )
    }

    static var shortcutTileColor: ShortcutTileColor { .orange }
}

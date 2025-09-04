import Component
import Dependencies
import Model
import SwiftUI
import Theme

// NOTE: Should use system font
public struct SettingsScreen: View {
    @State private var presenter = SettingsPresenter()

    public init() {}

    public var body: some View {
        List {
            notificationSection
        }
        .scrollContentBackground(.hidden)
        .background(AssetColors.surface.swiftUIColor)
        .navigationTitle(String(localized: "Settings", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
    }

    @ViewBuilder
    private var notificationSection: some View {
        Section {
            // Main notification toggle
            HStack {
                Image(systemName: "bell")
                    .foregroundColor(AssetColors.primary.swiftUIColor)
                    .frame(width: 24, height: 24)

                VStack(alignment: .leading, spacing: 2) {
                    Text("Session Notifications", bundle: .module)
                        .foregroundColor(AssetColors.onSurface.swiftUIColor)

                    if presenter.authorizationStatus == .denied {
                        Text("Enable in Settings", bundle: .module)
                            .font(.caption)
                            .foregroundColor(AssetColors.error.swiftUIColor)
                    } else {
                        Text("Get reminded before sessions start", bundle: .module)
                            .font(.caption)
                            .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
                    }
                }

                Spacer()

                if presenter.isLoading {
                    ProgressView()
                        .scaleEffect(0.8)
                } else if presenter.authorizationStatus == .denied {
                    Button {
                        presenter.openSystemSettings()
                    } label: {
                        Text(String(localized: "Settings", bundle: .module))
                    }
                    .typography(.caption)
                    .padding(.all, 8)
                    .foregroundColor(AssetColors.primary0.swiftUIColor)
                    .background(AssetColors.primary.swiftUIColor)
                    .clipShape(.capsule)
                    .controlSize(.small)
                } else {
                    Toggle(
                        "",
                        isOn: Binding(
                            get: { presenter.notificationSettings.isEnabled },
                            set: { _ in
                                Task {
                                    await presenter.toggleNotifications()
                                }
                            }
                        ))
                }
            }
            .padding(.vertical, 4)

            // Notification settings (only shown when enabled)
            if presenter.notificationSettings.isEnabled && presenter.authorizationStatus == .authorized {
                notificationDetailsSection
            }
        } header: {
            Text("Notifications", bundle: .module)
        } footer: {
            notificationSectionFooter
        }
    }

    @ViewBuilder
    private var notificationDetailsSection: some View {
        // Reminder time picker
        HStack {
            Image(systemName: "clock")
                .foregroundColor(AssetColors.primary.swiftUIColor)
                .frame(width: 24, height: 24)

            Text("Reminder Time", bundle: .module)
                .foregroundColor(AssetColors.onSurface.swiftUIColor)

            Spacer()

            Menu {
                ForEach(NotificationReminderTime.allCases) { reminderTime in
                    Button(
                        NSLocalizedString(reminderTime.displayTextKey, bundle: .module, comment: "Reminder time option")
                    ) {
                        Task {
                            await presenter.updateReminderTime(reminderTime)
                        }
                    }
                }
            } label: {
                HStack {
                    let currentTime = presenter.notificationSettings.reminderTime
                    Text(
                        NSLocalizedString(currentTime.displayTextKey, bundle: .module, comment: "Current reminder time")
                    )
                    .foregroundColor(AssetColors.primary.swiftUIColor)
                    Image(systemName: "chevron.down")
                        .foregroundColor(AssetColors.primary.swiftUIColor)
                        .font(.caption)
                }
            }
        }
        .padding(.vertical, 4)

        // Custom sound toggle
        HStack {
            Image(systemName: "speaker.wave.2")
                .foregroundColor(AssetColors.primary.swiftUIColor)
                .frame(width: 24, height: 24)

            VStack(alignment: .leading, spacing: 2) {
                Text("Custom Sound", bundle: .module)
                    .foregroundColor(AssetColors.onSurface.swiftUIColor)

                Text("Use DroidKaigi notification sound", bundle: .module)
                    .font(.caption)
                    .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
            }

            Spacer()

            Toggle(
                "",
                isOn: Binding(
                    get: { presenter.notificationSettings.useCustomSound },
                    set: { _ in
                        Task {
                            await presenter.toggleCustomSound()
                        }
                    }
                ))
        }
        .padding(.vertical, 4)
    }

    @ViewBuilder
    private var notificationSectionFooter: some View {
        switch presenter.authorizationStatus {
        case .notDetermined:
            Text("You'll be asked for permission when enabling notifications.", bundle: .module)
                .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
        case .denied:
            Text("Notification permission denied. Enable in Settings to receive session reminders.", bundle: .module)
                .foregroundColor(AssetColors.error.swiftUIColor)
        case .authorized, .provisional:
            if presenter.notificationSettings.isEnabled {
                Text(
                    "You'll receive notifications for your favorited sessions at the selected reminder time.",
                    bundle: .module
                )
                .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
            } else {
                Text("Enable notifications to get reminded about your favorited sessions.", bundle: .module)
                    .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
            }
        }
    }
}

#Preview {
    NavigationView {
        SettingsScreen()
    }
}

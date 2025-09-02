import Component
import Model
import SwiftUI
import Theme

public struct SearchScreen: View {
    @State private var presenter = SearchPresenter()
    @FocusState private var isSearchFieldFocused: Bool
    let onNavigate: (SearchNavigationDestination) -> Void

    public init(onNavigate: @escaping (SearchNavigationDestination) -> Void = { _ in }) {
        self.onNavigate = onNavigate
    }

    public var body: some View {
        VStack(spacing: 0) {
            searchBar
            filterAndResultsScrollView
        }
        .background(AssetColors.surface.swiftUIColor)
        .navigationTitle(String(localized: "Search", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .onAppear {
            presenter.loadInitial()
            isSearchFieldFocused = true
        }
    }

    private var searchBar: some View {
        HStack(spacing: 12) {
            Image(systemName: "magnifyingglass")
                .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                .accessibilityLabel(String(localized: "Search icon", bundle: .module))

            TextField(String(localized: "Search sessions", bundle: .module), text: $presenter.searchWord)
                .font(.body)
                .focused($isSearchFieldFocused)
                .submitLabel(.search)
                .onSubmit {
                    // Search is automatic with binding
                }

            if !presenter.searchWord.isEmpty {
                clearButton
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(AssetColors.surfaceVariant.swiftUIColor)
                .stroke(AssetColors.outline.swiftUIColor.opacity(0.2), lineWidth: 1)
        )
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
    }

    private var clearButton: some View {
        Button(
            action: {
                presenter.searchWord = ""
            },
            label: {
                Image(systemName: "xmark.circle.fill")
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                    .accessibilityLabel(String(localized: "Clear search", bundle: .module))
            })
    }

    private var filterAndResultsScrollView: some View {
        VStack {
            filterMenusSection
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    searchResultsSection
                }
                .padding(.top, 0)
            }
        }
        .frame(maxHeight: .infinity)
    }

    private var filterMenusSection: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 12) {
                dayFilterMenu
                categoryFilterMenu
                languageFilterMenu
            }
            .padding(.horizontal, 16)
        }
    }

    @ViewBuilder
    private var searchResultsSection: some View {
        if shouldShowResults {
            VStack(alignment: .leading, spacing: 16) {
                Divider()
                    .padding(.vertical, 4)

                HStack {
                    Text("Results")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(AssetColors.onSurface.swiftUIColor)

                    Spacer()

                    Text("\(presenter.filteredTimetableItems.count)")
                        .font(.subheadline)
                        .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
                }
                .padding(.horizontal, 16)

                searchResultsList
            }
        }
    }

    private var shouldShowResults: Bool {
        !presenter.searchWord.isEmpty || presenter.selectedDay != nil
            || presenter.selectedCategory != nil || presenter.selectedLanguage != nil
    }

    private var searchResultsList: some View {
        LazyVStack(spacing: 16) {
            ForEach(presenter.filteredTimetableItems) { item in
                TimetableCard(
                    timetableItem: item.timetableItem,
                    isFavorite: item.isFavorited,
                    onTap: { _ in
                        onNavigate(.timetableDetail(item))
                    },
                    onTapFavorite: { _, _ in
                        presenter.toggleFavorite(item.timetableItem.id)
                    }
                )
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(AssetColors.surface.swiftUIColor)
                        .shadow(color: AssetColors.onSurface.swiftUIColor.opacity(0.05), radius: 2, x: 0, y: 1)
                )
            }
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 80)  // Tab bar padding
    }

    private var categoryFilterMenu: some View {
        Menu {
            Button("All") {
                presenter.selectedCategory = nil
            }
            if let timetable = presenter.timetable.timetable {
                let uniqueCategories = Set(timetable.timetableItems.map { $0.category })
                let categories = Array(uniqueCategories).sorted { $0.id < $1.id }
                ForEach(categories) { category in
                    Button(category.title.jaTitle) {
                        presenter.selectedCategory = category
                    }
                }
            }
        } label: {
            HStack(spacing: 8) {
                Text("カテゴリ")
                    .font(.subheadline)
                    .foregroundStyle(AssetColors.onSurface.swiftUIColor)

                Text(categoryFilterDisplayText)
                    .font(.subheadline)
                    .foregroundStyle(AssetColors.primary.swiftUIColor)

                Image(systemName: "chevron.down")
                    .font(.caption)
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(AssetColors.surfaceVariant.swiftUIColor)
                    .stroke(AssetColors.outline.swiftUIColor.opacity(0.2), lineWidth: 1)
            )
        }
    }

    private var categoryFilterDisplayText: String {
        presenter.selectedCategory?.title.jaTitle ?? "全て"
    }

    private var languageFilterMenu: some View {
        Menu {
            Button("All") {
                presenter.selectedLanguage = nil
            }
            Button("Japanese") {
                toggleLanguage("JA")
            }
            Button("English") {
                toggleLanguage("EN")
            }
            Button("Mixed") {
                toggleLanguage("MIXED")
            }
        } label: {
            HStack(spacing: 8) {
                Text("言語")
                    .font(.subheadline)
                    .foregroundStyle(AssetColors.onSurface.swiftUIColor)

                Text(languageFilterDisplayText)
                    .font(.subheadline)
                    .foregroundStyle(AssetColors.primary.swiftUIColor)

                Image(systemName: "chevron.down")
                    .font(.caption)
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(AssetColors.surfaceVariant.swiftUIColor)
                    .stroke(AssetColors.outline.swiftUIColor.opacity(0.2), lineWidth: 1)
            )
        }
    }

    private var languageFilterDisplayText: String {
        guard let language = presenter.selectedLanguage else { return "全て" }
        switch language.langOfSpeaker {
        case "JA":
            return "Japanese"
        case "EN":
            return "English"
        case "MIXED":
            return "Mixed"
        default:
            return "全て"
        }
    }

    private func toggleLanguage(_ lang: String) {
        if presenter.selectedLanguage?.langOfSpeaker == lang {
            presenter.selectedLanguage = nil
        } else {
            presenter.selectedLanguage = TimetableLanguage(
                langOfSpeaker: lang, isInterpretationTarget: false)
        }
    }

    private var dayFilterMenu: some View {
        Menu {
            Button("All") {
                presenter.selectedDay = nil
            }
            Button("9/11") {
                presenter.selectedDay = .conferenceDay1
            }
            Button("9/12") {
                presenter.selectedDay = .conferenceDay2
            }
        } label: {
            HStack(spacing: 8) {
                Text("開催日")
                    .font(.subheadline)
                    .foregroundStyle(AssetColors.onSurface.swiftUIColor)

                Text(dayFilterDisplayText)
                    .font(.subheadline)
                    .foregroundStyle(AssetColors.primary.swiftUIColor)

                Image(systemName: "chevron.down")
                    .font(.caption)
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(AssetColors.surfaceVariant.swiftUIColor)
                    .stroke(AssetColors.outline.swiftUIColor.opacity(0.2), lineWidth: 1)
            )
        }
    }

    private var dayFilterDisplayText: String {
        switch presenter.selectedDay {
        case .conferenceDay1:
            return "9/11"
        case .conferenceDay2:
            return "9/12"
        case nil:
            return "全て"
        default:
            return "全て"
        }
    }
}

#Preview {
    SearchScreen()
}

import Component
import Model
import SwiftUI
import Theme

struct SponsorCard: View {
    let sponsor: Model.Sponsor
    let tier: Model.SponsorCategory.SponsorTier

    var cardHeight: CGFloat {
        switch tier {
        case .platinum:
            return 110
        case .gold:
            return 80
        case .supporters:
            return 80
        case .silver:
            return 80
        case .bronze:
            return 80
        }
    }

    var body: some View {
        CacheAsyncImage(url: sponsor.logoUrl) { image in
            image
                .resizable()
                .aspectRatio(contentMode: .fit)
        } placeholder: {
            Color.white
        }
        .frame(maxWidth: .infinity)
        .frame(height: cardHeight)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }
}

struct SponsorSection: View {
    let category: Model.SponsorCategory
    let onSponsorTapped: (Model.Sponsor) -> Void

    private var columns: [GridItem] {
        switch category.tier {
        case .platinum:
            return [GridItem(.flexible())]
        case .gold, .silver, .bronze:
            return [
                GridItem(.flexible(), spacing: 12),
                GridItem(.flexible(), spacing: 12),
            ]
        case .supporters:
            return [
                GridItem(.flexible(), spacing: 12),
                GridItem(.flexible(), spacing: 12),
                GridItem(.flexible(), spacing: 12),
            ]
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text(category.name)
                .typographyStyle(.titleMedium)
                .foregroundStyle(AssetColors.primary90.swiftUIColor)
                .padding(.horizontal, 16)
                .padding(.top, 12)
                .padding(.bottom, 16)

            LazyVGrid(columns: columns, spacing: 12) {
                ForEach(category.sponsors) { sponsor in
                    Button(
                        action: {
                            onSponsorTapped(sponsor)
                        },
                        label: {
                            SponsorCard(
                                sponsor: sponsor,
                                tier: category.tier
                            )
                        }
                    )
                    .sponsorCardStyle()
                }
            }
            .padding(.horizontal, 16)
            .padding(.bottom, 24)
        }
    }
}

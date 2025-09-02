import Model
import SwiftUI
import Theme

public struct ProfileCardInputCardVariant: View {
    @Binding var selectedCardVariant: Model.ProfileCardVariant

    public init(selectedCardType: Binding<Model.ProfileCardVariant>) {
        self._selectedCardVariant = selectedCardType
    }

    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
    ]

    public var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Select Theme", bundle: .module)
                .typographyStyle(.titleMedium)
                .foregroundStyle(.white)

            LazyVGrid(columns: columns, spacing: 12) {
                ForEach(Model.ProfileCardVariant.allCases, id: \.self) { cardVariant in
                    Button {
                        selectedCardVariant = cardVariant
                    } label: {
                        (selectedCardVariant == cardVariant
                            ? cardVariant.selectedImage
                            : cardVariant.image)
                            .resizable()
                            .aspectRatio(184.0 / 112, contentMode: .fill)
                    }
                }
            }
        }
    }
}

#Preview {
    ProfileCardInputCardVariant(selectedCardType: .constant(.nightPill))
}

extension Model.ProfileCardVariant {
    var image: Image {
        switch self {
        case .nightPill:
            return Image("night_pill_off", bundle: .module)
        case .dayPill:
            return Image("day_pill_off", bundle: .module)
        case .nightFlower:
            return Image("night_flower_off", bundle: .module)
        case .dayFlower:
            return Image("day_flower_off", bundle: .module)
        case .nightDiamond:
            return Image("night_diamond_off", bundle: .module)
        case .dayDiamond:
            return Image("day_diamond_off", bundle: .module)
        }
    }

    var selectedImage: Image {
        switch self {
        case .nightPill:
            return Image("night_pill_on", bundle: .module)
        case .dayPill:
            return Image("day_pill_on", bundle: .module)
        case .nightFlower:
            return Image("night_flower_on", bundle: .module)
        case .dayFlower:
            return Image("day_flower_on", bundle: .module)
        case .nightDiamond:
            return Image("night_diamond_on", bundle: .module)
        case .dayDiamond:
            return Image("day_diamond_on", bundle: .module)
        }
    }
}

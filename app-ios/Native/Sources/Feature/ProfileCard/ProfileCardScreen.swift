import Component
import SwiftUI
import Theme

// TODO: fix actual model
enum ProfileCardType: String {
    case dark
    case light
}

public struct ProfileCardScreen: View {
    @State private var presenter = ProfileCardPresenter()
    @State private var isFront: Bool = true
    @State private var animatedDegree: Angle = .zero
    @State private var cardType: ProfileCardType = .dark

    public init() {}

    public var body: some View {
        NavigationStack {
            profileCardScrollView
                .background(AssetColors.surface.swiftUIColor)
                .navigationTitle("Profile Card")
                #if os(iOS)
                    .navigationBarTitleDisplayMode(.large)
                #endif
        }
    }

    private var profileCardScrollView: some View {
        ScrollView {
            VStack(spacing: 0) {
                profileCard
                actionButtons
            }
            .padding(.vertical, 20)
            .padding(.bottom, 80)  // Tab bar padding
        }
        .onAppear {
            withAnimation(.bouncy) {
                animatedDegree = .degrees(30)
            } completion: {
                animatedDegree = .zero
            }
        }
    }

    private var profileCard: some View {
        ZStack {
            if isFront {
                FrontCard(
                    userRole: presenter.userRole,
                    userName: presenter.userName,
                    cardType: cardType
                )
            } else {
                BackCard(
                    cardType: cardType
                )
                .rotation3DEffect(Angle(degrees: 180), axis: (x: 0, y: 1, z: 0))
            }
        }
        .padding(.horizontal, 56)
        .padding(.vertical, 32)
        .onTapGesture {
            withAnimation {
                isFront.toggle()
            }
        }
        .rotation3DEffect(isFront ? animatedDegree : .degrees(180), axis: (x: 0, y: 1, z: 0))
    }

    private var actionButtons: some View {
        VStack(spacing: 8) {
            shareButton
            editButton
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }

    private var shareButton: some View {
        Button {
            presenter.shareProfileCard()
        } label: {
            Label("Share Profile Card", systemImage: "square.and.arrow.up")
                .frame(maxWidth: .infinity)
        }
        .filledButtonStyle()
    }

    private var editButton: some View {
        Button {
            presenter.editProfile()
            if cardType == .dark {
                cardType = .light
            } else {
                cardType = .dark
            }
        } label: {
            Text("Edit Profile")
                .frame(maxWidth: .infinity)
        }
        .textButtonStyle()
    }
}

#Preview {
    ProfileCardScreen()
}

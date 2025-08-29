import Component
import Model
import Observation
import Presentation
import SwiftUI
import Theme

public struct ProfileCardScreen: View {
    @State private var presenter = ProfileCardPresenter()
    @State private var selectedShape: Material3ShapeType = .pill

    public init() {}

    public var body: some View {
        NavigationStack {
            profileCardScrollView
                .background(AssetColors.surface.swiftUIColor)
                .navigationTitle("Profile Card")
                #if os(iOS)
                    .navigationBarTitleDisplayMode(.large)
                #endif
                .onAppear {
                    presenter.loadInitial()
                }
        }
    }

    @ViewBuilder
    private var profileCardScrollView: some View {
        let profile = presenter.profile.profile
        let isLoading = presenter.profile.isLoading
        ScrollView {
            Group {
                if isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                } else if presenter.shouldEditing {
                    editView
                } else {
                    VStack(spacing: 0) {
                        profileCard(profile!)
                        shapeSelector
                        actionButtons
                    }
                    .padding(.vertical, 20)
                }
            }
        }
    }

    private var editView: some View {
        EditProfileCardForm(presenter: $presenter)
    }

    @ViewBuilder
    private func profileCard(_ profile: Model.Profile) -> some View {
        TiltFlipCard(
            front: { normal in
                FrontCard(
                    userRole: profile.occupation,
                    userName: profile.name,
                    cardType: profile.cardVariant.type,
                    image: profile.image,
                    normal: (normal.x, normal.y, normal.z)
                )
            },
            back: { normal in
                BackCard(
                    cardType: profile.cardVariant.type,
                    url: profile.url,
                    normal: (normal.x, normal.y, normal.z),
                )
            }
        )
        .padding(.horizontal, 56)
        .padding(.vertical, 32)
    }

    private var actionButtons: some View {
        VStack(spacing: 8) {
            shareButton
            editButton
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
    
    private var shapeSelector: some View {
        VStack(spacing: 12) {
            Text("Profile Shape")
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .typographyStyle(.titleMedium)
            
            HStack(spacing: 20) {
                ForEach(Material3ShapeType.allCases, id: \.self) { shape in
                    Button {
                        selectedShape = shape
                    } label: {
                        VStack(spacing: 8) {
                            Material3ClippedImage(
                                systemImageName: "person.circle.fill",
                                shapeType: shape,
                                size: 60
                            )
                            .scaleEffect(selectedShape == shape ? 1.1 : 1.0)
                            .animation(.easeInOut(duration: 0.2), value: selectedShape)
                            
                            Text(shape.rawValue.capitalized)
                                .font(.caption)
                                .foregroundStyle(
                                    selectedShape == shape 
                                    ? AssetColors.primary.swiftUIColor 
                                    : AssetColors.onSurfaceVariant.swiftUIColor
                                )
                        }
                        .padding(.vertical, 8)
                        .padding(.horizontal, 12)
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .fill(selectedShape == shape 
                                      ? AssetColors.primaryContainer.swiftUIColor.opacity(0.3)
                                      : Color.clear)
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(selectedShape == shape 
                                        ? AssetColors.primary.swiftUIColor 
                                        : Color.clear, 
                                        lineWidth: 2)
                        )
                    }
                    .buttonStyle(.plain)
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 20)
    }
    
    private var shapeSelector: some View {
        VStack(spacing: 12) {
            Text("Profile Shape")
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .typographyStyle(.titleMedium)
            
            HStack(spacing: 20) {
                ForEach(Material3ShapeType.allCases, id: \.self) { shape in
                    Button {
                        selectedShape = shape
                    } label: {
                        VStack(spacing: 8) {
                            Material3ClippedImage(
                                systemImageName: "person.circle.fill",
                                shapeType: shape,
                                size: 60
                            )
                            .scaleEffect(selectedShape == shape ? 1.1 : 1.0)
                            .animation(.easeInOut(duration: 0.2), value: selectedShape)
                            
                            Text(shape.rawValue.capitalized)
                                .font(.caption)
                                .foregroundStyle(
                                    selectedShape == shape 
                                    ? AssetColors.primary.swiftUIColor 
                                    : AssetColors.onSurfaceVariant.swiftUIColor
                                )
                        }
                        .padding(.vertical, 8)
                        .padding(.horizontal, 12)
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .fill(selectedShape == shape 
                                      ? AssetColors.primaryContainer.swiftUIColor.opacity(0.3)
                                      : Color.clear)
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(selectedShape == shape 
                                        ? AssetColors.primary.swiftUIColor 
                                        : Color.clear, 
                                        lineWidth: 2)
                        )
                    }
                    .buttonStyle(.plain)
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 20)
    }

    private var shareButton: some View {
        let uiImage = OGPProfileShareImage(profile: presenter.profile.profile!).render()!
        let ogpImage = uiImage.pngData()!
        let shareText = String(localized: "Share Message", bundle: .module)

        return ShareLink(
            item: ShareOGPItem(ogpImage: ogpImage), message: Text(shareText),
            preview: SharePreview(shareText, image: Image(uiImage: uiImage))
        ) {
            HStack {
                AssetImages.icShare.swiftUIImage
                    .resizable()
                    .frame(width: 18, height: 18)
                Text(String(localized: "Share", bundle: .module))
            }
            .frame(maxWidth: .infinity)
        }
        .filledButtonStyle()
    }

    private var editButton: some View {
        Button {
            presenter.editProfile()
        } label: {
            Text(String(localized: "Edit", bundle: .module))
                .frame(maxWidth: .infinity)
        }
        .textButtonStyle()
    }
}

struct ShareOGPItem: Transferable {
    static var transferRepresentation: some TransferRepresentation {
        DataRepresentation(exportedContentType: .png) { item in
            item.ogpImage
        }
    }

    let ogpImage: Data
}

#Preview {
    ProfileCardScreen()
}

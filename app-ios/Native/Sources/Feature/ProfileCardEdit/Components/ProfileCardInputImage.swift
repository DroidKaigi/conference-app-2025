import SwiftUI
import Theme
import UIKit
import _PhotosUI_SwiftUI

public struct ProfileCardInputImage: View {
    @State private var isPickerPresented = false
    @State private var selectedImage: Image?
    @Binding var selectedPhoto: PhotosPickerItem?
    var initialImage: UIImage?
    var title: String
    var dismissKeyboard: () -> Void
    var errorMessage: String?

    public init(
        selectedPhoto: Binding<PhotosPickerItem?>,
        initialImage: UIImage? = nil,
        title: String,
        dismissKeyboard: @escaping () -> Void = {},
        errorMessage: String? = nil
    ) {
        self._selectedPhoto = selectedPhoto
        self.initialImage = initialImage
        self.title = title
        self.dismissKeyboard = dismissKeyboard
        self.errorMessage = errorMessage
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            Text(title)
                .typographyStyle(.titleMedium)
                .foregroundStyle(.white)

            if let image = selectedImage {
                ZStack(alignment: .topTrailing) {
                    Button {
                        isPickerPresented = true
                    } label: {
                        image
                            .resizable()
                            .frame(width: 120, height: 120)
                            .padding(.top, 12)
                            .padding(.trailing, 17)
                    }
                    Button {
                        isPickerPresented = true
                    } label: {
                        Image(systemName: "pencil")
                            .resizable()
                            .renderingMode(.template)
                            .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                            .frame(width: 16, height: 16)
                            .padding(4)
                            .background(AssetColors.surfaceVariant.swiftUIColor)
                            .clipShape(Circle())
                            .padding(8)
                    }
                }
            } else {
                Button {
                    dismissKeyboard()
                    isPickerPresented = true
                } label: {
                    HStack {
                        Image(systemName: "plus")
                            .resizable()
                            .renderingMode(.template)
                            .frame(width: 18, height: 18)

                        Text("Add Image", bundle: .module)
                            .typographyStyle(.labelLarge)
                    }
                    .padding(EdgeInsets(top: 11, leading: 16, bottom: 11, trailing: 24))
                    .foregroundStyle(AssetColors.primary.swiftUIColor)
                    .overlay(
                        Capsule()
                            .stroke(
                                AssetColors.outline.swiftUIColor,
                                style: StrokeStyle(lineWidth: 1)
                            )
                    )
                }
            }

            if let errorMessage = errorMessage {
                Text(errorMessage)
                    .typographyStyle(.bodySmall)
                    .foregroundStyle(AssetColors.error.swiftUIColor)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .onAppear {
            if let initialImage = initialImage {
                selectedImage = Image(uiImage: initialImage)
                selectedPhoto = nil
            }
        }
        .onChange(of: initialImage) { _, newInitialImage in
            if let newInitialImage = newInitialImage {
                selectedImage = Image(uiImage: newInitialImage)
                selectedPhoto = nil
            }
        }
        .photosPicker(isPresented: $isPickerPresented, selection: $selectedPhoto)
        .onChange(of: selectedPhoto) { _, newValue in
            newValue?
                .loadTransferable(type: Data.self) { result in
                    DispatchQueue.main.async {
                        switch result {
                        case .success(let data):
                            if let data,
                                let uiImage = UIImage(data: data)
                            {
                                // 向きを修正
                                let fixedImage = uiImage.fixOrientation()
                                self.selectedImage = Image(uiImage: fixedImage)
                            }
                        case .failure:
                            break
                        }
                    }
                }
        }
    }
}

// UIImageの向きを修正する拡張
extension UIImage {
    func fixOrientation() -> UIImage {
        if imageOrientation == .up {
            return self
        }
        UIGraphicsBeginImageContextWithOptions(size, false, scale)
        draw(in: CGRect(origin: .zero, size: size))
        guard let normalizedImage = UIGraphicsGetImageFromCurrentImageContext() else {
            return self
        }
        UIGraphicsEndImageContext()
        return normalizedImage
    }
}

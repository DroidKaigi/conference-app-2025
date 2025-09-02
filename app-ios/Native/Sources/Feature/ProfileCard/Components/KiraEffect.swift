import SwiftUI

private struct KiraEffect: ViewModifier {
    let function: ShaderFunction
    let normal: (Float, Float, Float)
    let monochromeImage: Image
    let isEnabled: Bool

    func body(content: Content) -> some View {
        if isEnabled {
            content.layerEffect(
                Shader(
                    function: function,
                    arguments: [.boundingRect, .float3(normal.0, normal.1, normal.2), .image(monochromeImage)],
                ),
                maxSampleOffset: .zero
            )
        } else {
            content
        }
    }
}

extension View {
    func kiraEffect(
        function: ShaderFunction, normal: (Float, Float, Float), monochromeImage: Image, isEnabled: Bool = true
    ) -> some View {
        self.modifier(
            KiraEffect(function: function, normal: normal, monochromeImage: monochromeImage, isEnabled: isEnabled))
    }
}

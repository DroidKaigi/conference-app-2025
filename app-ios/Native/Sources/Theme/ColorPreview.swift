import SwiftUI

private struct ColorPreview: View {
    var color: Color
    var colorName: String

    var body: some View {
        VStack {
            Circle()
                .fill(color)
                .frame(width: 30, height: 30)
                .overlay {
                    Circle()
                        .stroke(
                            style: StrokeStyle(
                                lineWidth: 1,
                                dash: color == .clear ? [1, 2] : []
                            )
                        )
                }
            Text(colorName)
                .font(.caption2)
        }
    }
}

private struct ColorWithDim: View {
    var color: Color
    var dim: Color
    var colorName: String

    var body: some View {
        VStack {
            HStack {
                Circle()
                    .fill(color)
                    .frame(width: 30, height: 30)
                    .overlay {
                        Circle()
                            .stroke(
                                style: StrokeStyle(
                                    lineWidth: 1,
                                    dash: color == .clear ? [1, 2] : []
                                )
                            )
                    }
                Circle()
                    .fill(dim)
                    .frame(width: 30, height: 30)
                    .overlay {
                        Circle()
                            .stroke(
                                style: StrokeStyle(
                                    lineWidth: 1,
                                    dash: dim == .clear ? [1, 2] : []
                                )
                            )
                    }
            }
            Text(colorName)
                .font(.caption2)
        }
    }
}

#Preview {
    Grid {
        GridRow {
            ColorPreview(
                color: AssetColors.primary.swiftUIColor,
                colorName: "Primary"
            )
            ColorPreview(
                color: AssetColors.secondary.swiftUIColor,
                colorName: "Secondary"
            )
            ColorPreview(
                color: AssetColors.tertiary.swiftUIColor,
                colorName: "Tertiary"
            )
            ColorPreview(
                color: AssetColors.error.swiftUIColor,
                colorName: "Error"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.onPrimary.swiftUIColor,
                colorName: "On Primary"
            )
            ColorPreview(
                color: AssetColors.onSecondary.swiftUIColor,
                colorName: "On Secondary"
            )
            ColorPreview(
                color: AssetColors.onTertiary.swiftUIColor,
                colorName: "On Tertiary"
            )
            ColorPreview(
                color: AssetColors.onError.swiftUIColor,
                colorName: "On Error"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.primaryContainer.swiftUIColor,
                colorName: "Primary Container"
            )
            ColorPreview(
                color: AssetColors.secondaryContainer.swiftUIColor,
                colorName: "Secondary Container"
            )
            ColorPreview(
                color: AssetColors.tertiaryContainer.swiftUIColor,
                colorName: "Tertiary Container"
            )
            ColorPreview(
                color: AssetColors.errorContainer.swiftUIColor,
                colorName: "Error Container"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.onPrimaryContainer.swiftUIColor,
                colorName: "On Primary Container"
            )
            ColorPreview(
                color: AssetColors.onSecondaryContainer.swiftUIColor,
                colorName: "On Secondary Container"
            )
            ColorPreview(
                color: AssetColors.onTertiaryContainer.swiftUIColor,
                colorName: "On Tertiary Container"
            )
            ColorPreview(
                color: AssetColors.onErrorContainer.swiftUIColor,
                colorName: "On Error Container"
            )
        }
        GridRow {
            ColorWithDim(
                color: AssetColors.primaryFixed.swiftUIColor,
                dim: AssetColors.primaryFixedDim.swiftUIColor,
                colorName: "Primary Fixed"
            )
            ColorWithDim(
                color: AssetColors.secondaryFixed.swiftUIColor,
                dim: AssetColors.secondaryFixedDim.swiftUIColor,
                colorName: "Secondary Fixed"
            )
            ColorWithDim(
                color: AssetColors.tertiaryFixed.swiftUIColor,
                dim: AssetColors.tertiaryFixedDim.swiftUIColor,
                colorName: "Primary Fixed"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.onPrimaryFixed.swiftUIColor,
                colorName: "On Primary Fixed"
            )
            ColorPreview(
                color: AssetColors.onSecondaryFixed.swiftUIColor,
                colorName: "On Secondary Fixed"
            )
            ColorPreview(
                color: AssetColors.onTertiaryFixed.swiftUIColor,
                colorName: "On Tertiary Fixed"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.onPrimaryFixedVariant.swiftUIColor,
                colorName: "On Primary Fixed V"
            )
            ColorPreview(
                color: AssetColors.onSecondaryFixedVariant.swiftUIColor,
                colorName: "On Secondary Fixed V"
            )
            ColorPreview(
                color: AssetColors.onTertiaryFixedVariant.swiftUIColor,
                colorName: "On Tertiary Fixed V"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.surfaceDim.swiftUIColor,
                colorName: "Surface Dim"
            )
            ColorPreview(
                color: AssetColors.surface.swiftUIColor,
                colorName: "Surface"
            )
            ColorPreview(
                color: AssetColors.surfaceVariant.swiftUIColor,
                colorName: "Surface V"
            )
            ColorPreview(
                color: AssetColors.surfaceBright.swiftUIColor,
                colorName: "Surface Bright"
            )
            ColorPreview(
                color: AssetColors.surfaceTint.swiftUIColor,
                colorName: "Surface Tint"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.surfaceContainerLowest.swiftUIColor,
                colorName: "SC Lowest"
            )
            ColorPreview(
                color: AssetColors.surfaceContainerLow.swiftUIColor,
                colorName: "SC Low"
            )
            ColorPreview(
                color: AssetColors.surfaceContainer.swiftUIColor,
                colorName: "Surface Container"
            )
            ColorPreview(
                color: AssetColors.surfaceContainerHigh.swiftUIColor,
                colorName: "SC High"
            )
            ColorPreview(
                color: AssetColors.surfaceContainerHighest.swiftUIColor,
                colorName: "SC Highest"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.onSurface.swiftUIColor,
                colorName: "On Surface"
            )
            ColorPreview(
                color: AssetColors.onSurfaceVariant.swiftUIColor,
                colorName: "On Surface V"
            )
            ColorPreview(
                color: AssetColors.outline.swiftUIColor,
                colorName: "Outline"
            )
            ColorPreview(
                color: AssetColors.outlineVariant.swiftUIColor,
                colorName: "Outline Variant"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.inverseSurface.swiftUIColor,
                colorName: "Inverse Surface"
            )
            ColorPreview(
                color: AssetColors.inverseOnSurface.swiftUIColor,
                colorName: "Inverse On Surface"
            )
            ColorPreview(
                color: AssetColors.inversePrimary.swiftUIColor,
                colorName: "Inverse Primary"
            )
            ColorPreview(
                color: AssetColors.scrim.swiftUIColor,
                colorName: "Scrim"
            )
            ColorPreview(
                color: AssetColors.shadow.swiftUIColor,
                colorName: "Shadow"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.iguana.swiftUIColor,
                colorName: "Iguana"
            )
            ColorPreview(
                color: AssetColors.hedgehog.swiftUIColor,
                colorName: "Hedgehog"
            )
            ColorPreview(
                color: AssetColors.giraffe.swiftUIColor,
                colorName: "Giraffe"
            )
            ColorPreview(
                color: AssetColors.flamingo.swiftUIColor,
                colorName: "Flamingo"
            )
            ColorPreview(
                color: AssetColors.jellyfish.swiftUIColor,
                colorName: "Jellyfish"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.iguanaContainer.swiftUIColor,
                colorName: "Iguana Container"
            )
            ColorPreview(
                color: AssetColors.hedgehogContainer.swiftUIColor,
                colorName: "Hedgehog Container"
            )
            ColorPreview(
                color: AssetColors.giraffeContainer.swiftUIColor,
                colorName: "Giraffe Container"
            )
            ColorPreview(
                color: AssetColors.flamingoContainer.swiftUIColor,
                colorName: "Flamingo Container"
            )
            ColorPreview(
                color: AssetColors.jellyfishContainer.swiftUIColor,
                colorName: "Jellyfish Container"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.iguanaDim.swiftUIColor,
                colorName: "Iguana Dim"
            )
            ColorPreview(
                color: AssetColors.hedgehogDim.swiftUIColor,
                colorName: "Hedgehog Dim"
            )
            ColorPreview(
                color: AssetColors.giraffeDim.swiftUIColor,
                colorName: "Giraffe Dim"
            )
            ColorPreview(
                color: AssetColors.flamingoDim.swiftUIColor,
                colorName: "Flamingo Dim"
            )
            ColorPreview(
                color: AssetColors.jellyfishDim.swiftUIColor,
                colorName: "Jellyfish Dim"
            )
        }
        GridRow {
            ColorPreview(
                color: AssetColors.background.swiftUIColor,
                colorName: "Background"
            )
            ColorPreview(
                color: AssetColors.onBackground.swiftUIColor,
                colorName: "On Background"
            )
        }
    }
    .padding()
    .background(Color(uiColor: UIColor.systemGroupedBackground))
}

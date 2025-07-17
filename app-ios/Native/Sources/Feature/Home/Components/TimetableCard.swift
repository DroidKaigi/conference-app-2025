import Model
import SwiftUI
import Theme
import Presentation

struct TimetableCard: View {
    let timetableItem: TimetableItem
    let isFavorite: Bool
    let onTap: (TimetableItem) -> Void
    let onTapFavorite: (TimetableItem, CGPoint?) -> Void
    
    @State private var dragLocation: CGPoint?
    
    var body: some View {
        Button(action: {
            onTap(timetableItem)
        }) {
            VStack(alignment: .leading, spacing: 8) {
                headerRow
                
                Text(timetableItem.title.currentLangTitle)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(Color(.label))
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
                
                if !timetableItem.speakers.isEmpty {
                    speakersList
                }
            }
            .padding(12)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color(.systemBackground))
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(Color(.separator).opacity(0.5), lineWidth: 1)
            )
            .cornerRadius(4)
        }
        .buttonStyle(PlainButtonStyle())
    }
    
    private var headerRow: some View {
        HStack(spacing: 8) {
            RoomTag(room: timetableItem.room)
            
            LanguageTag(language: timetableItem.language)
            
            Spacer()
            
            favoriteButton
        }
    }
    
    private var favoriteButton: some View {
        Button(action: {
            let location = dragLocation
            onTapFavorite(timetableItem, location)
        }) {
            Image(systemName: isFavorite ? "heart.fill" : "heart")
                .foregroundColor(isFavorite ? Color.blue.opacity(0.8) : Color(.secondaryLabel))
                .frame(width: 24, height: 24)
        }
        .buttonStyle(PlainButtonStyle())
        .background(
            GeometryReader { geometry in
                Color.clear
                    .onContinuousHover { phase in
                        switch phase {
                        case .active(let location):
                            dragLocation = location
                        case .ended:
                            break
                        }
                    }
            }
        )
    }
    
    private var speakersList: some View {
        VStack(alignment: .leading, spacing: 4) {
            ForEach(timetableItem.speakers) { speaker in
                HStack(spacing: 8) {
                    CircularUserIcon(imageUrl: speaker.imageUrl)
                        .frame(width: 32, height: 32)
                    
                    Text(speaker.name)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(Color(.label))
                }
            }
        }
    }
}

struct RoomTag: View {
    let room: Room
    
    var body: some View {
        Text(room.displayName)
            .font(.system(size: 12, weight: .medium))
            .foregroundColor(.white)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(room.color)
            .cornerRadius(4)
    }
}

struct LanguageTag: View {
    let language: TimetableLanguage
    
    var body: some View {
        Text(language.displayLanguage)
            .font(.system(size: 12, weight: .medium))
            .foregroundColor(Color(.secondaryLabel))
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(Color(.separator), lineWidth: 1)
            )
    }
}

struct CircularUserIcon: View {
    let imageUrl: String?
    
    var body: some View {
        if let imageUrl = imageUrl, let url = URL(string: imageUrl) {
            AsyncImage(url: url) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            } placeholder: {
                Image(systemName: "person.circle.fill")
                    .foregroundColor(Color(.secondaryLabel))
            }
            .clipShape(Circle())
        } else {
            Image(systemName: "person.circle.fill")
                .resizable()
                .foregroundColor(Color(.secondaryLabel))
        }
    }
}

// TODO: Add preview with proper test data
//#Preview {
//    TimetableCard(
//        timetableItem: ...,
//        isFavorite: false,
//        onTap: { _ in },
//        onTapFavorite: { _, _ in }
//    )
//    .padding()
//}

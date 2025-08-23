package io.github.droidkaigi.confsched.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.UserDataStoreQualifier
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@SingleIn(DataScope::class)
@Inject
public class UserDataStore(
    @param:UserDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    public fun getStream(): Flow<PersistentSet<TimetableItemId>> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[FAVORITE_SESSION_IDS_KEY]?.map { id ->
                    TimetableItemId(id)
                }.orEmpty().toPersistentSet()
            }
    }

    public suspend fun toggleFavorite(id: TimetableItemId) {
        val favoriteIds = dataStore.data.map { it[FAVORITE_SESSION_IDS_KEY] }.firstOrNull().orEmpty().toMutableSet()
        if (favoriteIds.contains(id.value)) {
            favoriteIds.remove(id.value)
        } else {
            favoriteIds.add(id.value)
        }
        dataStore.edit { preferences ->
            preferences[FAVORITE_SESSION_IDS_KEY] = favoriteIds
        }
    }

    public fun getProfileOrNull(): Flow<Profile?> {
        return dataStore.data.map {
            val profile = it[PROFILE_KEY] ?: return@map null
            json.decodeFromString(profile)
        }
    }

    public suspend fun saveProfile(profile: Profile) {
        dataStore.edit { preferences ->
            preferences[PROFILE_KEY] = json.encodeToString(Profile.serializer(), profile)
        }
    }

    public suspend fun clearFavorites() {
        dataStore.edit { preferences ->
            preferences.remove(FAVORITE_SESSION_IDS_KEY)
        }
    }

    private companion object {
        private val FAVORITE_SESSION_IDS_KEY = stringSetPreferencesKey("FAVORITE_SESSION_IDS_KEY")
        private val PROFILE_KEY = stringPreferencesKey("PROFILE_KEY")
    }
}

package io.github.droidkaigi.confsched.data.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.ProfileDataStoreQualifier
import io.github.droidkaigi.confsched.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@Inject
public class ProfileDataStore(
    @param:ProfileDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    public fun getProfileOrNull(): Flow<Profile?> {
        return dataStore.data.map {
            val profile = it[PROFILE_KEY] ?: return@map null
            try {
                json.decodeFromString(profile)
            } catch (_: Throwable) {
                null
            }
        }
    }

    public suspend fun saveProfile(profile: Profile) {
        dataStore.edit { preferences ->
            preferences[PROFILE_KEY] = json.encodeToString(Profile.serializer(), profile)
        }
    }

    private companion object {
        private val PROFILE_KEY = stringPreferencesKey("PROFILE_KEY")
    }
}

package io.github.droidkaigi.confsched.data

import de.jensklingenberg.ktorfit.Ktorfit
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Qualifier
import io.github.droidkaigi.confsched.data.core.defaultJson
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

public abstract class DataScope private constructor()

@Qualifier
public annotation class UseProductionApiBaseUrl

@Qualifier
public annotation class ApiBaseUrl

@ContributesTo(DataScope::class)
public interface DataGraph {
    @UseProductionApiBaseUrl
    public val useProductionApiBaseUrl: Boolean

    @Provides
    @ApiBaseUrl
    public fun provideApiBaseUrl(
        @UseProductionApiBaseUrl useProductionApiBaseUrl: Boolean,
    ): String = if (useProductionApiBaseUrl) {
        "https://ssot-api.droidkaigi.jp/"
    } else {
        "https://ssot-api-staging.an.r.appspot.com/"
    }

    @Provides
    public fun provideKtorfit(
        httpClient: HttpClient,
        @ApiBaseUrl apiBaseUrl: String,
    ): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(httpClient)
            .baseUrl(apiBaseUrl)
            .build()
    }

    @Provides
    public fun provideJson(): Json {
        return defaultJson()
    }
}

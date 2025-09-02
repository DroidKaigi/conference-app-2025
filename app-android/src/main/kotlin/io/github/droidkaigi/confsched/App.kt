package io.github.droidkaigi.confsched

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.createGraphFactory

class App : Application() {
    val appGraph: AppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>()
            .createAndroidAppGraph(
                applicationContext = this,
                licensesJsonReader = AndroidLicensesJsonReader(this),
                useProductionApi = BuildConfig.USE_PRODUCTION_API,
            )
    }
}

val Context.appGraph: AppGraph get() = (applicationContext as App).appGraph

/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package ru.lampa.pangoline

import android.content.Context
import android.os.StrictMode
import android.support.v7.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mozilla.components.service.fretboard.Fretboard
import mozilla.components.service.fretboard.ValuesProvider
import mozilla.components.service.fretboard.source.kinto.KintoExperimentSource
import mozilla.components.service.fretboard.storage.flatfile.FlatFileExperimentStorage
import mozilla.components.support.base.log.Log
import mozilla.components.support.base.log.sink.AndroidLogSink
import ru.lampa.pangoline.locale.LocaleAwareApplication
import ru.lampa.pangoline.session.NotificationSessionObserver
import ru.lampa.pangoline.session.VisibilityLifeCycleCallback
import ru.lampa.pangoline.telemetry.CrashReporterWrapper
import ru.lampa.pangoline.telemetry.TelemetrySessionObserver
import ru.lampa.pangoline.telemetry.TelemetryWrapper
import ru.lampa.pangoline.utils.AdjustHelper
import ru.lampa.pangoline.utils.AppConstants
import ru.lampa.pangoline.utils.EXPERIMENTS_BASE_URL
import ru.lampa.pangoline.utils.EXPERIMENTS_BUCKET_NAME
import ru.lampa.pangoline.utils.EXPERIMENTS_COLLECTION_NAME
import ru.lampa.pangoline.utils.EXPERIMENTS_JSON_FILENAME
import ru.lampa.pangoline.utils.StethoWrapper
import ru.lampa.pangoline.web.CleanupSessionObserver
import ru.lampa.pangoline.web.WebViewProvider
import java.io.File
import kotlin.coroutines.CoroutineContext

class PangolineApplication : LocaleAwareApplication(), CoroutineScope {
    lateinit var fretboard: Fretboard

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val components: Components by lazy { Components() }

    var visibilityLifeCycleCallback: VisibilityLifeCycleCallback? = null
        private set

    override fun onCreate() {
        super.onCreate()

        Log.addSink(AndroidLogSink("Focus"))
        CrashReporterWrapper.init(this)

        StethoWrapper.init(this)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        TelemetryWrapper.init(this@PangolineApplication)
        loadExperiments()

        enableStrictMode()

        components.searchEngineManager.apply {
            launch(IO) {
                load(this@PangolineApplication)
            }

            registerForLocaleUpdates(this@PangolineApplication)
        }

        AdjustHelper.setupAdjustIfNeeded(this@PangolineApplication)

        visibilityLifeCycleCallback = VisibilityLifeCycleCallback(this@PangolineApplication)
        registerActivityLifecycleCallbacks(visibilityLifeCycleCallback)

        components.sessionManager.apply {
            register(NotificationSessionObserver(this@PangolineApplication))
            register(TelemetrySessionObserver())
            register(CleanupSessionObserver(this@PangolineApplication))
        }

        launch(IO) { fretboard.updateExperiments() }
    }

    private fun loadExperiments() {
        val experimentsFile = File(filesDir, EXPERIMENTS_JSON_FILENAME)
        val experimentSource = KintoExperimentSource(
            EXPERIMENTS_BASE_URL, EXPERIMENTS_BUCKET_NAME, EXPERIMENTS_COLLECTION_NAME
        )
        fretboard = Fretboard(experimentSource, FlatFileExperimentStorage(experimentsFile),
            object : ValuesProvider() {
                override fun getClientId(context: Context): String {
                    return TelemetryWrapper.clientId
                }
            })
        fretboard.loadExperiments()
        TelemetryWrapper.recordActiveExperiments(this)
        WebViewProvider.determineEngine(this@PangolineApplication)
    }

    private fun enableStrictMode() {
        // Android/WebView sometimes commit strict mode violations, see e.g.
        // https://github.com/mozilla-mobile/focus-android/issues/660
        if (AppConstants.isReleaseBuild) {
            return
        }

        val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder().detectAll()
        val vmPolicyBuilder = StrictMode.VmPolicy.Builder().detectAll()

        threadPolicyBuilder.penaltyLog()
        vmPolicyBuilder.penaltyLog()

        StrictMode.setThreadPolicy(threadPolicyBuilder.build())
        StrictMode.setVmPolicy(vmPolicyBuilder.build())
    }
}

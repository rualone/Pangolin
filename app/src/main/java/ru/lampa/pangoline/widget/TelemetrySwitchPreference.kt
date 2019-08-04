/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package ru.lampa.pangoline.widget

import android.content.Context
import android.util.AttributeSet
import ru.lampa.pangoline.R
import ru.lampa.pangoline.settings.LearnMoreSwitchPreference
import ru.lampa.pangoline.telemetry.CrashReporterWrapper
import ru.lampa.pangoline.telemetry.TelemetryWrapper
import ru.lampa.pangoline.utils.SupportUtils
import org.mozilla.telemetry.TelemetryHolder

/**
 * Switch preference for enabling/disabling telemetry
 */
internal class TelemetrySwitchPreference(context: Context?, attrs: AttributeSet?) :
    LearnMoreSwitchPreference(context, attrs) {

    init {
        if (context != null) {
            isChecked = TelemetryWrapper.isTelemetryEnabled(context)
        }
    }

    override fun onClick() {
        super.onClick()
        TelemetryHolder.get()
                .configuration
                .setUploadEnabled(isChecked).isCollectionEnabled = isChecked
        CrashReporterWrapper.onIsEnabledChanged(context)
    }

    override fun getDescription(): String? {
        return context.resources.getString(
            R.string.preference_mozilla_telemetry_summary2,
            context.resources.getString(R.string.app_name)
        )
    }

    override fun getLearnMoreUrl(): String {
        return SupportUtils.getSumoURLForTopic(context, SupportUtils.SumoTopic.USAGE_DATA)
    }
}

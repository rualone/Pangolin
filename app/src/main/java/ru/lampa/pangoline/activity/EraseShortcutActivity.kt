/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package ru.lampa.pangoline.activity

import android.app.Activity
import android.os.Bundle
import ru.lampa.pangoline.ext.components
import ru.lampa.pangoline.session.removeAndCloseAllSessions

import ru.lampa.pangoline.telemetry.TelemetryWrapper

class EraseShortcutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        components.sessionManager.removeAndCloseAllSessions()

        TelemetryWrapper.eraseShortcutEvent()

        finishAndRemoveTask()
    }
}

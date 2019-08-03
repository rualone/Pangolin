/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package ru.lampa.pangoline.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import ru.lampa.pangoline.ext.components
import ru.lampa.pangoline.session.removeAndCloseAllSessions
import ru.lampa.pangoline.telemetry.TelemetryWrapper

class EraseAndOpenShortcutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        components.sessionManager.removeAndCloseAllSessions()

        TelemetryWrapper.eraseAndOpenShortcutEvent()

        val intent = Intent(this, MainActivity::class.java)
        intent.action = MainActivity.ACTION_OPEN
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}

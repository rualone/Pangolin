/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package ru.lampa.pangoline.ext

import android.content.Context
import ru.lampa.pangoline.Components
import ru.lampa.pangoline.PangolineApplication

/**
 * Get the PangolineApplication object from a context.
 */
val Context.application: PangolineApplication
    get() = applicationContext as PangolineApplication

/**
 * Get the components of this application.
 */
val Context.components: Components
    get() = application.components

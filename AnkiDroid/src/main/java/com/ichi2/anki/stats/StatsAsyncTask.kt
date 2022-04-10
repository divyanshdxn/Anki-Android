/*
 *  Copyright (c) 2021 David Allison <davidallisongithub@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ichi2.anki.stats

import android.view.View
import com.ichi2.async.CoroutineAsyncTask
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

/**
 * Async task which handles cancellation (#6192)
 */
abstract class StatsAsyncTask<TResult>(scope: CoroutineScope) : CoroutineAsyncTask<View?, Void?, TResult?>(scope, "StatsAsyncTask") {
    override fun doInBackground(vararg params: View?): TResult? {
        return try {
            doInBackgroundSafe(*params)
        } catch (e: Exception) {
            if (this.isCancelled) {
                Timber.w(e, "ignored exception in cancelled stats task")
                return null
            }
            throw e
        }
    }

    protected abstract fun doInBackgroundSafe(vararg views: View?): TResult
}

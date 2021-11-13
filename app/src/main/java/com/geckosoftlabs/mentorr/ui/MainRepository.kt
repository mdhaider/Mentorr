package com.geckosoftlabs.mentorr.ui

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.geckosoftlabs.mentorr.ui.utils.MEMO_KEY
import com.geckosoftlabs.mentorr.ui.utils.dataStore

class MainRepository (
    private val context: Context
) {
    suspend fun setMemo(memo: String) {
        context.dataStore.edit { settings ->
            settings[MEMO_KEY] = memo
        }
    }
}
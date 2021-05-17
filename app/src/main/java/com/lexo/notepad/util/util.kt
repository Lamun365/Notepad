package com.lexo.notepad.util

import android.content.Context
import android.text.SpannableString
import android.text.style.BulletSpan
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

fun List<String>.toBulletList(): CharSequence {
    return SpannableString(this.joinToString("\n")).apply {
        this@toBulletList.foldIndexed(0) { index, acc, span ->
            val end = acc + span.length + if (index != this@toBulletList.size -1) 1 else 0
            this.setSpan(BulletSpan(16), acc, end, 0)
            end
        }
    }
}

inline fun SearchView.onQueryTextChange(crossinline listener: (String) -> Unit) {
    
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sort_preferences")

val <T> T.exhaustive: T
    get() = this


enum class SortOrder {
    ORDER_BY_NAME,
    ORDER_BY_DATE
}
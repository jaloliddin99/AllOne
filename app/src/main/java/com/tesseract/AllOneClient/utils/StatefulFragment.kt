package com.tesseract.AllOneClient.utils

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class StatefulFragment : Fragment() {
    var savedState: Bundle? = null
        private set
    private var saved = false
    override fun onSaveInstanceState(state: Bundle) {
        if (view == null) {
            state.putBundle(_FRAGMENT_STATE, savedState)
        } else {
            val bundle = if (saved) savedState else stateToSave
            state.putBundle(_FRAGMENT_STATE, bundle)
        }
        saved = false
        super.onSaveInstanceState(state)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        if (state != null) {
            savedState = state.getBundle(_FRAGMENT_STATE)
        }
    }

    override fun onDestroyView() {
        savedState = stateToSave
        saved = true
        super.onDestroyView()
    }

    abstract fun hasSavedState(): Boolean
    abstract val stateToSave: Bundle?

    companion object {
        private const val _FRAGMENT_STATE = "FRAGMENT_STATE"
    }
}
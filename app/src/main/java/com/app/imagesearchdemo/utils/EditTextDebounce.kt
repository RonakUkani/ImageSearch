package com.app.imagesearchdemo.utils

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.lang.ref.WeakReference

class EditTextDebounce constructor(@NonNull editText: EditText) {

    private val editTextWeakReference: WeakReference<EditText>?
    private val debounceHandler: Handler = Handler(Looper.getMainLooper())
    private var debounceWorker: Runnable? = null
    private var delayMillis: Int = 0
    private var minLength: Int = 0
    private val textWatcher: TextWatcher
    private var callback: ((String) -> Unit)? = null

    init {
        this.debounceWorker = DebounceRunnable("", callback, minLength)
        this.delayMillis = 500
        this.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //unused
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //unused
            }

            override fun afterTextChanged(s: Editable) {
                debounceHandler.removeCallbacks(debounceWorker!!)
                debounceWorker = DebounceRunnable(s.toString(), callback, minLength)
                debounceHandler.postDelayed(debounceWorker!!, delayMillis.toLong())
            }
        }
        this.editTextWeakReference = WeakReference(editText)
        val editTextInternal = this.editTextWeakReference.get()
        editTextInternal?.addTextChangedListener(textWatcher)
    }

    fun create(editText: EditText): EditTextDebounce {
        return EditTextDebounce(editText)
    }

    fun create(editText: EditText, delayMillis: Int): EditTextDebounce {
        val editTextDebounce = EditTextDebounce(editText)
        editTextDebounce.setDelayMillis(delayMillis)
        return editTextDebounce
    }

    fun watch(@Nullable callback: ((String) -> Unit)) {
        this.callback = callback
    }

    fun watch(@Nullable callback: ((String) -> Unit), delayMillis: Int) {
        this.callback = callback
        this.delayMillis = delayMillis
    }

    fun unwatch() {
        if (editTextWeakReference != null) {
            val editText = editTextWeakReference.get()
            if (editText != null) {
                editText.removeTextChangedListener(textWatcher)
                editTextWeakReference.clear()
                debounceHandler.removeCallbacks(debounceWorker)
            }
        }
        this.callback = null
    }

    private fun setDelayMillis(delayMillis: Int) {
        this.delayMillis = delayMillis
    }

    private class DebounceRunnable internal constructor(
        private val result: String,
        private val debounceCallback: ((String) -> Unit)?,
        private val minLength: Int
    ) : Runnable {

        override fun run() {
            if (result.length >= minLength)
                debounceCallback?.invoke(result)
        }
    }
}
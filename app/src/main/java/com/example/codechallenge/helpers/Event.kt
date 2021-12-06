package com.example.codechallenge.helpers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class Event<T>(t: T) : MutableLiveData<Pair<T, Boolean>>(Pair(t, false)) {
    fun raiseEvent(newValue: T) {
        value = Pair(newValue, true)
    }

    fun observeEvent(owner: LifecycleOwner, onCompletion: (it: T) -> Unit) {
        observe(owner) {
            if (it.second) {
                onCompletion(it.first)
                value = Pair(it.first, false)
            }
        }
    }
}
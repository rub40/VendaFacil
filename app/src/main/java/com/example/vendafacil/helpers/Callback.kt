package com.example.vendafacil.helpers

import com.example.vendafacil.models.Sale

interface Callback {
    fun onCallback(value: MutableList<Sale>)
}


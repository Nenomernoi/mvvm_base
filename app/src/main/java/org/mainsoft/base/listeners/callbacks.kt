package org.mainsoft.base.listeners

import java.io.Serializable


interface BreedsReturnCallback {
    fun onUpdateItem(position: Int)
}

class BackCallback(@Transient
                   val resultListener: BreedsReturnCallback?) : Serializable
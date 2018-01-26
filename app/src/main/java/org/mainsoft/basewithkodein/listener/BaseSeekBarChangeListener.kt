package org.mainsoft.basewithkodein.listener

import android.widget.SeekBar

interface BaseSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //
    }
}

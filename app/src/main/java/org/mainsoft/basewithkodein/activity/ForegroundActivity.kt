package org.mainsoft.basewithkodein.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_foreground_service.*
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.services.ForeGroundService


class ForegroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_foreground_service)

        startServiceButton?.setOnClickListener {
            val intent = Intent(this@ForegroundActivity, ForeGroundService::class.java)
            intent.action = ForeGroundService.ACTION_START_FOREGROUND_SERVICE
            startService(intent)
        }
        stopServiceButton?.setOnClickListener {
            val intent = Intent(this@ForegroundActivity, ForeGroundService::class.java)
            intent.action = ForeGroundService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
        }


    }
}
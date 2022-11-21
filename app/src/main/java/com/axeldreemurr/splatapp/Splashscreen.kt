package com.axeldreemurr.splatapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class Splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // on below line we are configuring
        // our window to full screen

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splashscreen)

        // on below line we are calling
        // handler to run a task
        // for specific time interval

        Handler().postDelayed({

            // on below line we are
            // creating a new intent

            val i = Intent(
                this@Splashscreen,
                SignInActivity::class.java
            )

            // on below line we are
            // starting a new activity.

            startActivity(i)

            // on the below line we are finishing
            // our current activity.

            finish()
        }, 2000)
    }
}
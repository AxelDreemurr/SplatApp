package com.axeldreemurr.splatapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.axeldreemurr.splatapp.databinding.ActivitySignInBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    // declaring notif variables
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    lateinit var mAdView : AdView
    private val channelId = "com.axeldreemurr.splatapp.notifications"
    private val description = "Avisos de inicio de sesión"
    private val textTitle = "Bienvenido"
    private val textContent = "¡Felicidades, has iniciado sesión con éxito!"
    private val GOOGLE_SIGN_IN=100

    var provider = OAuthProvider.newBuilder("github.com")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttongotosignup.setOnClickListener{
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        binding.githubLoginBtn.setOnClickListener {
            val pendingResultTask = firebaseAuth.pendingAuthResult
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask
                    .addOnSuccessListener(
                        OnSuccessListener {
                            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                            Toast.makeText(this, "¡Accediste con GitHub!", Toast.LENGTH_LONG).show()
                            startActivity(intent)

                            // User is signed in.
                            // IdP data available in
                            // authResult.getAdditionalUserInfo().getProfile().
                            // The OAuth access token can also be retrieved:
                            // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                        })
                    .addOnFailureListener {
                        // Handle failure.
                    }
            } else {
                firebaseAuth
                    .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                    .addOnSuccessListener {
                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        Toast.makeText(this, "¡Accediste con GitHub!", Toast.LENGTH_LONG).show()
                        startActivity(intent)
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    }
                    .addOnFailureListener {
                        // Handle failure.
                    }
            }
        }


        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if (it.isSuccessful) {
                            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                            Toast.makeText(this, "¡Accediste con éxito!", Toast.LENGTH_LONG).show()
                            startActivity(intent)

                            val intent2 = Intent(this, HomeActivity::class.java)
                            val pendingIntent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_MUTABLE)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                                notificationChannel.enableLights(true)
                                notificationChannel.lightColor = Color.GREEN
                                notificationChannel.enableVibration(true)
                                notificationManager.createNotificationChannel(notificationChannel)

                                builder = Notification.Builder(this, channelId)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                                    .setContentIntent(pendingIntent)
                            } else {

                                builder = Notification.Builder(this)
                                    .setContentTitle(textTitle)
                                    .setContentText(textContent)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                                    .setContentIntent(pendingIntent)
                            }

                            notificationManager.notify(1235, builder.build())


                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
            } else {
                Toast.makeText(this, "No se permiten campos en blanco.", Toast.LENGTH_SHORT).show()

            }

        }
    }


    private fun setup(){
        val btn_google : Button = findViewById(R.id.sign_in_button)
        btn_google.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("613474827511-pblpkfsargah1hs21po8ki8phopf328n.apps.googleusercontent.com")
                .requestEmail()
                .build()
            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }

    }

    private fun showHome(email: String, provider: ProviderType){
        val intent: Intent = Intent(this, HomeActivity:: class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        Toast.makeText(this, "¡Accediste con Google!", Toast.LENGTH_LONG).show()
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                if (account!=null){
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(account.email?:"",ProviderType.GOOGLE)
                        }else{
                        }
                    }
                }
            }catch (e: ApiException){
            }


        }
    }
}
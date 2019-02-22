package com.ajithkp.application

import android.app.ActivityOptions.makeSceneTransitionAnimation
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.FirebaseStorage
import android.support.annotation.NonNull
import android.transition.TransitionInflater
import android.util.Log
import android.widget.RelativeLayout
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import android.view.ViewAnimationUtils
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View


class SplashActivity : AppCompatActivity() {

    private var splashImage: ImageView? = null
    private var imageUrl: String = ""
    private var glidereq: RequestManager? = null
    private var view:View? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = layoutInflater.inflate(R.layout.activity_splash, null)
        setContentView(view)

        glidereq = Glide.with(this)
        splashImage = findViewById(R.id.image_view) as ImageView

        var relative_view = findViewById(R.id.relative_view) as ImageView

        glidereq?.load(R.drawable.splash_image)?.thumbnail(glidereq?.load(R.drawable.loading))?.into(relative_view!!)

        var storage = FirebaseStorage.getInstance()
        var ref = storage.getReference().child("home_page/img_13.jpg")
        ref.downloadUrl.addOnSuccessListener {
        }
    }

    public fun presentActivity(view:View) {
        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition");
        val revealX =  (view.getX() + view.getWidth() / 2)
        val revealY = (view.getY() + view.getHeight() / 2)
        var intent = Intent(this, DashboardActivity.class)
    }


}


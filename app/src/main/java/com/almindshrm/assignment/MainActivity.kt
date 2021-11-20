package com.almindshrm.assignment

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.LinearInterpolator

import android.animation.Animator

import android.animation.AnimatorListenerAdapter
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {

    var count = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler(Looper.myLooper()!!).postDelayed({
            slideEnter(countLayout)
        }, 300)
        addToCart.setOnClickListener {
            slideExit(countLayout)
            slideExit(addToCart)
        }

        remove.setOnClickListener {
            if (count == 0) {
                slideEnter(countLayout)
                slideEnter(addToCart)
            } else {
                count--
                if (count == 0) {
                    removeIcon.setImageResource(R.drawable.ic_delete)
                    removeText.text = "Remove"
                }
            }
            countText.text = count.toString()
        }

        more.setOnClickListener {
            count ++
            if (count > 0) {
                removeIcon.setImageResource(R.drawable.ic_remove)
                removeText.text = "Less"
            }
            countText.text = count.toString()
        }

    }

    private fun slideExit(currentView: View) {
        currentView.animate().translationXBy(currentView.width.toFloat()).setDuration(300)
            .setInterpolator(LinearInterpolator()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                }
            })

    }

    private fun slideEnter(currentView: View) {
        currentView.animate().translationXBy(-currentView.width.toFloat()).setDuration(300)
            .setInterpolator(LinearInterpolator()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                }
            })

    }


}
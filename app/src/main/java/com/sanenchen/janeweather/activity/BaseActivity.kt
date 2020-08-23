package com.sanenchen.janeweather.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * BaseActivity
 * @author: sanenchen
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        return
    }

    /**
     * 设置顶部Toolbar
     */
    fun setToolbar(toolbar: Toolbar, title: String?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null && title != null) {
            actionBar.title = title
        }
    }
}
package com.example.sampleapp.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityFragmentParentBinding
import com.example.sampleapp.databinding.ActivityMainBinding
import com.moengage.inbox.ui.view.InboxFragment

class FragmentParentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentParentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fragment_parent)


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = InboxFragment()
        fragmentTransaction.replace(R.id.fragment_container_view, fragment)
        fragmentTransaction.commit()

    }
}
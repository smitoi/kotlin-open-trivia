package com.fmi.opentrivia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.fmi.opentrivia.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = this.findViewById(R.id.menu)
        bottomNavigation.setOnItemSelectedListener {
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
            when (it.itemId) {
                R.id.ic_question_answer -> {
                    val action = ProfileDirections.actionProfileToCategoryList()
                    navController.navigate(action)
                }
                R.id.ic_person -> {
                    val action = CategoryListDirections.actionCategoryListToProfile()
                    navController.navigate(action)
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}
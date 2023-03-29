package com.example.todosapp.presentation.addEdit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todosapp.R
import com.example.todosapp.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.example.todosapp.presentation.addEdit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import com.example.todosapp.R
import com.example.todosapp.common.Utils
import com.example.todosapp.data.local.TodosPriority
import com.example.todosapp.databinding.ActivityAddEditBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.main.MainActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private var todosPriority = TodosPriority.LOW
    private var deadlineDate: Long? = null
    private val now by lazy {
        Calendar.getInstance().time
    }
    private val mViewModel by viewModels<AddEditViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStyleEditText()
        setupDropdownPriority()
        setupDatePicker()

        setSupportActionBar(binding.addEditToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.add)
        }
    }

    private fun setupDropdownPriority() {
        val priority = TodosPriority.values().toList()
        val arrayAdapter = ArrayAdapter(
            this@AddEditActivity,
            R.layout.dropdown_item,
            R.id.text_select_priority,
            priority
        )
        binding.autoCompletePriority.apply {
            setAdapter(arrayAdapter)
            setText(TodosPriority.LOW.name, false)
            setOnItemClickListener { _, _, position, _ ->
                todosPriority = priority[position]
            }
        }
    }

    // Change style when user start to typing
    private fun changeStyleEditText() {
        binding.titleEdt.apply {
            doOnTextChanged { _, _, _, _ ->
                if (text.isEmpty()) {
                    setTypeface(
                        ResourcesCompat.getFont(
                            this@AddEditActivity,
                            R.font.roboto_bold
                        ), Typeface.BOLD
                    )
                } else {
                    setTypeface(
                        ResourcesCompat.getFont(
                            this@AddEditActivity,
                            R.font.roboto_regular
                        ), Typeface.BOLD
                    )
                }
            }
        }

        binding.descriptionEdt.apply {
            doOnTextChanged { _, _, _, _ ->
                if (text.isEmpty()) {
                    setTypeface(
                        ResourcesCompat.getFont(
                            this@AddEditActivity,
                            R.font.roboto_medium
                        ), Typeface.BOLD
                    )
                } else {
                    setTypeface(
                        ResourcesCompat.getFont(this@AddEditActivity, R.font.times),
                        Typeface.NORMAL
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDatePicker() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val dateRangePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .setTitleText("Select Deadline")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .setNegativeButtonText("Clear")
                .build()
        dateRangePicker.clearOnNegativeButtonClickListeners()

        binding.btnDeadline.apply {
            setOnClickListener {
                dateRangePicker.show(supportFragmentManager, "")

                dateRangePicker.addOnPositiveButtonClickListener {
                    val convertTime = Utils.convertLongToTime(it)
                    deadlineDate = it
                    this.text = "Deadline set ${convertTime.replace(".", " ")}"
                }

                dateRangePicker.addOnNegativeButtonClickListener {
                    this.text = "Pick Deadline"
                    deadlineDate = null
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.create_todos) {
            if (validateForm()) {
                val todos = Todos(
                    title = binding.titleEdt.text.toString(),
                    description = binding.descriptionEdt.text.toString(),
                    priority = todosPriority,
                    date = now.time, isArchive = binding.checkBoxArchive.isChecked,
                    isDone = false,
                    deadline = deadlineDate
                )
                mViewModel.upsertTodos(todos)
                startActivity(Intent(this@AddEditActivity, MainActivity::class.java))
            } else {
                showErrorForm()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateForm(): Boolean =
        !(binding.titleEdt.text.isEmpty() || binding.descriptionEdt.text.isEmpty())

    private fun showErrorForm() {
        if (binding.titleEdt.text.isEmpty()) {
            binding.titleEdt.error = getString(R.string.error_text)
        }
        if (binding.descriptionEdt.text.isEmpty()) {
            binding.descriptionEdt.error = getString(R.string.error_text)
        }
    }
}
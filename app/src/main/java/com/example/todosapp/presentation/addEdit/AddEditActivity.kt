package com.example.todosapp.presentation.addEdit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.navArgs
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
    private lateinit var todosParam: Todos
    private var isUpdate = false
    private val todosArgs: AddEditActivityArgs by navArgs()
    private var todosPriority = TodosPriority.LOW
    private var deadlineDate: Long? = null
    private var now = Calendar.getInstance().time.time
    private val priority = TodosPriority.values().toList()
    private val mViewModel by viewModels<AddEditViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (todosArgs.todos != null) {
            todosParam = todosArgs.todos!!
            isUpdate = true
            todosPriority = todosParam.priority
            deadlineDate = todosParam.deadline
        }

        setSupportActionBar(binding.addEditToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isUpdate) getString(R.string.edit) else getString(R.string.add)
        }


        setupDropdownPriority()
        setupDatePicker()
        changeStyleEditText()
        setupEditTextFile()

        if (isUpdate) {
            initValue()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initValue() {
        binding.apply {
            titleEdt.setText(todosParam.title)
            descriptionEdt.setText(todosParam.description)
            checkBoxArchive.isChecked = todosParam.isArchive
            btnDeadline.text = setDeadlineText(deadlineDate)
        }
    }

    private fun setupDropdownPriority() {

        val arrayAdapter = ArrayAdapter(
            this@AddEditActivity,
            R.layout.dropdown_item,
            R.id.text_select_priority,
            priority
        )
        binding.autoCompletePriority.apply {
            setAdapter(arrayAdapter)
            setText(todosPriority.name, false)
            setOnItemClickListener { _, _, position, _ ->
                todosPriority = priority[position]
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
                .setTitleText(getString(R.string.select_deadline))
                .setSelection(if (isUpdate) deadlineDate else MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .setNegativeButtonText("Clear")
                .build()
        dateRangePicker.clearOnNegativeButtonClickListeners()

        binding.btnDeadline.apply {
            setOnClickListener {
                dateRangePicker.show(supportFragmentManager, "")

                dateRangePicker.addOnPositiveButtonClickListener {
                    deadlineDate = it
                    this.text = setDeadlineText(it)
                }

                dateRangePicker.addOnNegativeButtonClickListener {
                    this.text = setDeadlineText(null)
                    deadlineDate = null
                }
            }
        }
    }

    private fun setupEditTextFile() {
        binding.titleEdt.apply {
            // set focus when create
            if (isUpdate) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            } else {
                requestFocus()
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mainMenu = if (isUpdate) R.menu.edit_menu else R.menu.add_menu
        menuInflater.inflate(mainMenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_todos -> upsertTodos()
            R.id.edit_todos -> upsertTodos()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun upsertTodos() {
        if (validateForm()) {
            val todos = Todos(
                title = binding.titleEdt.text.toString(),
                description = binding.descriptionEdt.text.toString(),
                priority = todosPriority,
                date = if (isUpdate) todosParam.date else now,
                isArchive = binding.checkBoxArchive.isChecked,
                isDone = false,
                deadline = deadlineDate
            )

            if (isUpdate) {
                todos.id = todosParam.id
            }

            mViewModel.upsertTodos(todos)
            startActivity(Intent(this@AddEditActivity, MainActivity::class.java))
        } else {
            showErrorForm()
        }
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


    private fun setDeadlineText(deadlineDate: Long?) = if (deadlineDate != null) "Deadline set ${
        Utils.convertLongToTime(deadlineDate).replace(".", " ")
    }" else getString(R.string.pick_deadline)
}
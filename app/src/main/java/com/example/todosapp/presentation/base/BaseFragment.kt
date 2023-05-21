package com.example.todosapp.presentation.base

import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.todosapp.databinding.CustomToastErrorBinding
import com.example.todosapp.databinding.CustomToastSuccessBinding

open class BaseFragment : Fragment() {


    fun showToast(isError: Boolean, message: String) {

        if (!isError) {
            val toastView = CustomToastSuccessBinding.inflate(layoutInflater)
            toastView.successMessage.text = message

            Toast(requireContext()).apply {
                setGravity(Gravity.BOTTOM, 0, 180)
                duration = Toast.LENGTH_LONG
                view = toastView.root
            }.show()
        }

        if (isError) {
            val toastView = CustomToastErrorBinding.inflate(layoutInflater)
            toastView.errorMessage.text = message

            Toast(requireContext()).apply {
                setGravity(Gravity.BOTTOM, 0, 180)
                duration = Toast.LENGTH_LONG
                view = toastView.root
            }.show()
        }

    }

}
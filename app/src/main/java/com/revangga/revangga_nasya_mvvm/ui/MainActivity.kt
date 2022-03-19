package com.revangga.revangga_nasya_mvvm.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.revangga.revangga_nasya_mvvm.DogViewModelFactory
import com.revangga.revangga_nasya_mvvm.adapter.DogsAdapter
import com.revangga.revangga_nasya_mvvm.data.repository.MainRepository
import com.revangga.revangga_nasya_mvvm.databinding.ActivityMainBinding
import com.revangga.revangga_nasya_mvvm.dialog.CustomLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DogViewModel by viewModels()
    private lateinit var loadingUI: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.fetchAndLoadDogs()
        }
        loadingUI = CustomLoadingDialog(this)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.message.observe(this) {
            showMessageToast(it)
        }
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }

    }

    private fun showMessageToast(msg: String?) {
        msg?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading() {
        loadingUI.show()
    }

    private fun hideLoading() {
        loadingUI.hide()
    }
}
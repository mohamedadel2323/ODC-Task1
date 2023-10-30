package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listeners()
        observers()
    }

    private fun listeners() {
        binding.liveDataBtn.setOnClickListener { viewModel.changeLiveDataValue("LiveData") }
        binding.stateFlowBtn.setOnClickListener { viewModel.changeStateFlowValue("StateFlow") }
        binding.sharedFlowBtn.setOnClickListener { viewModel.changeSharedFlowValue("SharedFlow") }
        binding.flowBtn.setOnClickListener {
            lifecycleScope.launch { viewModel.changeFlowValue().collectLatest { binding.flowTv.text = it } }
        }
    }
    private fun observers() {
        viewModel.liveData.observe(this) {
            binding.liveDataTv.text = it
            showSnackBar(it)
        }
        collectLatestLifeCycleFlow(viewModel.stateFlow) {
            binding.stateFlowTv.text = it
            showSnackBar(it)
        }
        collectLatestLifeCycleFlow(viewModel.sharedFlow) {
            binding.sharedFlowTv.text = it
            showSnackBar(it)
        }
    }
    private fun showSnackBar(it: String) { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
    private fun <T> collectLatestLifeCycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collectLatest(collect) } }
    }
}
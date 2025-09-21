package com.pvs.celebrare

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pvs.celebrare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fonts = ArrayAdapter.createFromResource(
            this,
            R.array.fonts_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setFontFamily(fonts.getItem(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.myCanvas.attachViewModel(viewModel, this)

        binding.undo.setOnClickListener {
            viewModel.doUndo()
        }

        binding.redo.setOnClickListener {
            viewModel.doRedo()
        }

        binding.plus.setOnClickListener {
            viewModel.increaseFontSize()
        }

        binding.minus.setOnClickListener {
            viewModel.decreaseFontSize()
        }

        binding.bold.setOnClickListener {
            viewModel.toggleBold()
        }

        binding.italic.setOnClickListener {
            viewModel.toggleItalic()
        }

        binding.underline.setOnClickListener {
            viewModel.toggleUnderline()
        }

        viewModel.textStyle.observe(this) {
            binding.textView.text = it.fontSize.toString()
        }


    }

}
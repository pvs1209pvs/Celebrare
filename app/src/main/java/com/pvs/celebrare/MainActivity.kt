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
import androidx.core.view.get
import com.google.android.material.chip.ChipDrawable
import com.pvs.celebrare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

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
        binding.myCanvas.onCanvasEvent = {
            viewModel.addToUndo(it)
        }

        binding.undo.setOnClickListener {
            viewModel.doUndo()
            binding.myCanvas.invalidate()
        }

        binding.redo.setOnClickListener {
            viewModel.doRedo()
            binding.myCanvas.invalidate()
        }

        binding.plus.setOnClickListener {
            viewModel.increaseFontSize()
        }

        binding.minus.setOnClickListener {
            viewModel.decreaseFontSize()
        }

        binding.boldChip.setOnClickListener {
            viewModel.toggleBold()
        }

        binding.italicsChip.setOnClickListener {
            viewModel.toggleItalic()
        }

        binding.underlinedChip.setOnClickListener {
            viewModel.toggleUnderline()
        }

        binding.addText.setOnClickListener {
            viewModel.addTextToCanvas("Paramvir")
        }

        viewModel.textStyle.observe(this) {
            if(it==null) binding.textView.text = 0.toString()
            else binding.textView.text = it.fontSize.toInt().toString()
        }

        viewModel.textStyle.observe(this){
            if(it==null){
                binding.chipGroup.isEnabled = false
                binding.chipGroup[0].isEnabled = false
                binding.chipGroup[1].isEnabled = false
                binding.chipGroup[2].isEnabled = false
                binding.spinner.isEnabled = false
                binding.minus.isEnabled = false
                binding.plus.isEnabled = false
            }
            else{
                binding.chipGroup.isEnabled = true
                binding.chipGroup[0].isEnabled = true
                binding.chipGroup[1].isEnabled = true
                binding.chipGroup[2].isEnabled = true
                binding.spinner.isEnabled = true
                binding.minus.isEnabled = true
                binding.plus.isEnabled = true
            }
        }

    }

}
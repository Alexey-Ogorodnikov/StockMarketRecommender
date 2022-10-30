package com.alexey.stockmarketrecommender.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.alexey.stockmarketrecommender.databinding.FragmentMainBinding
import com.alexey.stockmarketrecommender.json.TimeSeriesDaily
import com.google.gson.GsonBuilder
import java.io.IOException

import java.io.InputStream


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private lateinit var viewModel: MainFragmentViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val jsonFileString: String? = getJsonDataFromAssets("query_stock_data.json")
        Log.d("data : ", jsonFileString.toString())
        val listData = GsonBuilder().create().fromJson(jsonFileString, Array<TimeSeriesDaily>::class.java)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getJsonDataFromAssets(jsonFileName : String): String? {
        val json: String? = try {
            val inputStream: InputStream = binding.root.context.assets.open(jsonFileName)
            val sizeofFile: Int = inputStream.available()
            val bufferData = ByteArray(sizeofFile)
            inputStream.read(bufferData)
            inputStream.close()
            String(bufferData)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return json
    }
}
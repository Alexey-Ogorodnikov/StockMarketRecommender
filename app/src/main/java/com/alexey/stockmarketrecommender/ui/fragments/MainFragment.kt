package com.alexey.stockmarketrecommender.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexey.stockmarketrecommender.AlgorithmManager
import com.alexey.stockmarketrecommender.R
import com.alexey.stockmarketrecommender.databinding.FragmentMainBinding
import com.alexey.stockmarketrecommender.json.StockData
import com.alexey.stockmarketrecommender.json.TimeSeriesDaily
import com.alexey.stockmarketrecommender.models.TableItem
import com.alexey.stockmarketrecommender.ui.adapters.TableRecyclerAdapter
import com.alexey.stockmarketrecommender.ui.viewmodels.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import org.json.JSONException
import java.io.IOException

import java.io.InputStream


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var tableAdapter: TableRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateInformationWindow()

        setupViewModel()
        binding.headerButtonSearch.setOnClickListener { view ->
            val numberOfDays = binding.editTextNumberDays.text.toString().toInt()
            val stockName = binding.editTextStockName.text.toString()
            if(stockName.isBlank()){
                Snackbar.make(view, getString(R.string.header_search_isEmpty), Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view, getString(R.string.header_search_feedback), Snackbar.LENGTH_SHORT).show()
                try {

                    val filteredData = extractDataFromJson(numberOfDays)
                    viewModel.setupData(
                        StockData(
                            stockName = stockName,
                            daysWindow = numberOfDays.toString(),
                            timeSeriesDaily = filteredData
                        )
                    )


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }



    private fun setupViewModel() {
        viewModel.apply {

            stockData.observe(viewLifecycleOwner) {

                initRecyclerView()
                updateInformationWindow()
                binding.apply {
                    formStockInfo.text = getString(R.string.form_stock_info, stockData.value?.stockName)
                    formSocialMediaInfo.text = getString(R.string.form_social_media_info, countSocialMediaPosts().toString() )
                    formStockTimeWindowInfo.text = getString(R.string.form_stock_time_window_info, stockData.value?.daysWindow)
                }

            }
        }
    }



    private fun initRecyclerView(){
        val stockDataItems = ArrayList<TableItem>()
        viewModel.stockData.value?.timeSeriesDaily?.let {
            for (item in it) {
                stockDataItems.add(
                    TableItem(
                        price = item.Close.toString(),
                        date = item.Date,
                        social = item.Social.toString(),
                        recommendation = getRecommendation(item)
                    )
                )
            }
        }

        binding.tableRecyclerview.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            tableAdapter = TableRecyclerAdapter()
            tableAdapter.submitItems(stockDataItems)
            adapter = tableAdapter
        }
    }

    private fun getRecommendation(item: TimeSeriesDaily): String {

        if (viewModel.stockData.value?.timeSeriesDaily != null ){
            val algoManager = AlgorithmManager(item, viewModel.stockData.value?.timeSeriesDaily!!)
            algoManager.selectAlgo(AlgorithmManager.Algorithm.SMART)
            return algoManager.getRecommendation().name
        } else  {
            return "not enough data"
        }
    }

    private fun updateInformationWindow(){
        binding.apply {
            if (viewModel.stockData.value?.timeSeriesDaily?.size == null ) {
                form.visibility = View.INVISIBLE
                table.visibility = View.INVISIBLE
            } else {
                form.visibility = View.VISIBLE
                table.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun extractDataFromJson(numberOfDays : Int) : ArrayList<TimeSeriesDaily> {
        val jsonFileString: String? = getJsonDataFromAssets("query_stock_data.json")
        Log.d("data : ", jsonFileString.toString())
        val listData = GsonBuilder().create().fromJson(jsonFileString, Array<TimeSeriesDaily>::class.java)
        val filteredData = ArrayList<TimeSeriesDaily>()
        for (i in listData.indices) {
            if (i < numberOfDays)
                filteredData.add(listData[i])
        }
        return filteredData
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
package com.alexey.stockmarketrecommender.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var viewModel: MainFragmentViewModel


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    private lateinit var blogAdapter: TableRecyclerAdapter
    private var stockData : StockData? = null


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

//        val jsonFileString: String? = getJsonDataFromAssets("query_stock_data.json")
//        Log.d("data : ", jsonFileString.toString())
//        val listData = GsonBuilder().create().fromJson(jsonFileString, Array<TimeSeriesDaily>::class.java)

        updateInformationWindow()

        binding.headerButtonSearch.setOnClickListener { view ->
            val numberOfDays = binding.editTextNumberDays.text.toString().toInt()
            Snackbar.make(view, getString(R.string.header_search_feedback), Snackbar.LENGTH_SHORT).show()

            try {
                val jsonFileString: String? = getJsonDataFromAssets("query_stock_data.json")
                Log.d("data : ", jsonFileString.toString())
                val listData = GsonBuilder().create().fromJson(jsonFileString, Array<TimeSeriesDaily>::class.java)
                val listData2 = ArrayList<TimeSeriesDaily>(2)
                for (i in listData.indices) {
                    if (i < numberOfDays)
                        listData2.add(listData[i])
                }


                stockData = StockData(stockName = binding.editTextStockName.text.toString(), daysWindow = numberOfDays.toString(), timeSeriesDaily = listData2)


                updateInformationWindow()

                binding.apply {
                    formStockInfo.text = getString(R.string.form_stock_info, stockData?.stockName)
                    formSocialMediaInfo.text = getString(R.string.form_social_media_info, countSocialMediaPosts().toString() )
                    formStockTimeWindowInfo.text = getString(R.string.form_stock_time_window_info, stockData?.daysWindow)
                }
                initRecyclerView()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }


    private fun countSocialMediaPosts(): Int {
        var countSocialMediaPosts = 0
        stockData?.timeSeriesDaily?.let {
            for (item in it) {
                countSocialMediaPosts = countSocialMediaPosts.plus(item.Social)
            }
        }
        return countSocialMediaPosts
    }


    private fun initRecyclerView(){
        val stockDataItems = ArrayList<TableItem>()
        stockData?.timeSeriesDaily?.let {
            for (item in it) {
                stockDataItems.add(
                    TableItem(
                        price = item.Close.toString(),
                        date = item.Date,
                        social = item.Social.toString(),
                        recommendation = "buy"
                    )
                )
            }
        }

        binding.tableRecyclerview.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            blogAdapter = TableRecyclerAdapter()
            blogAdapter.submitItems(stockDataItems)
            adapter = blogAdapter
        }
    }

    private fun updateInformationWindow(){
        binding.apply {
            if (stockData == null) {
                formStockInfo.visibility = View.INVISIBLE
                formSocialMediaInfo.visibility = View.INVISIBLE
                formStockTimeWindowInfo.visibility = View.INVISIBLE
            } else {
                formStockInfo.visibility = View.VISIBLE
                formSocialMediaInfo.visibility = View.VISIBLE
                formStockTimeWindowInfo.visibility = View.VISIBLE
            }
        }
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
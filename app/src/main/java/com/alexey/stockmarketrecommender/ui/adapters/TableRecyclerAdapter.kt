package com.alexey.stockmarketrecommender.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexey.stockmarketrecommender.R
import com.alexey.stockmarketrecommender.models.TableItem
import com.alexey.stockmarketrecommender.ui.adapters.viewholder.TableViewHolder

class TableRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var items: List<TableItem> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return TableViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.table_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is TableViewHolder -> {
                    holder.bind(items[position])
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        fun submitItems(blogList: List<TableItem>) {
            items = blogList
        }
}
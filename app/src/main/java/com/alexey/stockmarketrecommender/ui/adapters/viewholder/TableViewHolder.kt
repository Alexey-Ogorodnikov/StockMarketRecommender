package com.alexey.stockmarketrecommender.ui.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexey.stockmarketrecommender.R
import com.alexey.stockmarketrecommender.models.TableItem

class TableViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(tableItem: TableItem) {

        itemView.apply {
            findViewById<TextView>(R.id.item_price_end_day).text = itemView.context.getString(R.string.table_item_price_end_day, tableItem.price)
            findViewById<TextView>(R.id.item_date).text = itemView.context.getString(R.string.table_item_date, tableItem.date)
            findViewById<TextView>(R.id.table_item_social_rating).text = itemView.context.getString(R.string.table_item_social_rating, tableItem.social)
            findViewById<TextView>(R.id.table_item_marker_recommendation).text = itemView.context.getString(R.string.table_item_marker_recommendation, tableItem.recommendation)
        }

    }

}
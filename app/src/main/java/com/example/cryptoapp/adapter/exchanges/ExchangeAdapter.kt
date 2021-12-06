package com.example.cryptoapp.adapter.exchanges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setCompactPrice
import com.example.cryptoapp.constant.coingekko.ExchangeConstant.loadPng
import com.example.cryptoapp.interfaces.OnItemClickListener
import com.example.cryptoapp.interfaces.OnItemLongClickListener
import com.example.cryptoapp.model.exchanges.Exchange

class ExchangeAdapter (private val mList: MutableList<Exchange>, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener)
    : RecyclerView.Adapter<ExchangeAdapter.ViewHolder>() {

    private val mOnItemClickListener: OnItemClickListener = onItemClickListener
    private val mOnItemLongClickListener: OnItemLongClickListener = onItemLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exchange_element, parent, false)

        return ViewHolder(view, mOnItemClickListener, mOnItemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        if(!itemsViewModel.image.isNullOrEmpty()){
            holder.exchangeLogo.loadPng(itemsViewModel.image)
        }
        if(!itemsViewModel.name.isNullOrEmpty()){
            holder.exchangeName.text = itemsViewModel.name
        }
        if(itemsViewModel.trust_score != null){
           holder.exchangeTrustScore.text = itemsViewModel.trust_score.toString()
        }
        if(itemsViewModel.trade_volume_24h_btc != null){
            holder.volume.text = setCompactPrice(itemsViewModel.trade_volume_24h_btc)
        }
    }

    override fun getItemCount(): Int = mList.size

    fun addData(data : MutableList<Exchange>){
        val insertIndex = mList.size
        mList.addAll(insertIndex, data)
        notifyItemRangeInserted(insertIndex, data.size)
    }

    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{

        private val mOnItemClickListener: OnItemClickListener = onItemClickListener
        private val mOnItemLongClickListener: OnItemLongClickListener = onItemLongClickListener

        val exchangeLogo: ImageView = itemView.findViewById(R.id.exchange_logo)
        val exchangeName: TextView = itemView.findViewById(R.id.exchange_name)
        val exchangeTrustScore: TextView = itemView.findViewById(R.id.exchange_trust_score)
        val volume: TextView = itemView.findViewById(R.id.volume)

        override fun onClick(view: View) {
            mOnItemClickListener.onItemClick(bindingAdapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            mOnItemLongClickListener.onItemLongClick(bindingAdapterPosition)
            return true
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }
}
package com.example.cryptoapp.adapter.cryptocurrencies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setCompactPrice
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.loadSvg
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setPercentage
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setPrice
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.timePeriods
import com.example.cryptoapp.interfaces.OnItemClickListener
import com.example.cryptoapp.interfaces.OnItemLongClickListener
import com.example.cryptoapp.model.allcryptocurrencies.CryptoCurrency
import java.util.*

class CryptoCurrencyAdapter (private val mList: MutableList<CryptoCurrency>, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener)
    : RecyclerView.Adapter<CryptoCurrencyAdapter.ViewHolder>() {

    private val mOnItemClickListener: OnItemClickListener = onItemClickListener
    private val mOnItemLongClickListener: OnItemLongClickListener = onItemLongClickListener
    private var timePeriod = timePeriods[1]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.crypto_element, parent, false)

        return ViewHolder(view, mOnItemClickListener, mOnItemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        holder.percentageChangeText.text = timePeriod.uppercase(Locale.getDefault())
        holder.currencyName.text = itemsViewModel.name
        holder.currencySymbol.text = itemsViewModel.symbol
        if(!itemsViewModel.iconUrl.isNullOrEmpty()){
            holder.currencyLogo.loadSvg(itemsViewModel.iconUrl)
        }
        if(!itemsViewModel.price.isNullOrEmpty()){
            holder.currencyValue.text = setPrice(itemsViewModel.price.toDouble())
        }
        if(!itemsViewModel.change.isNullOrEmpty()){
            setPercentage(itemsViewModel.change.toDouble(), holder.percentChange)
        }
        if(!itemsViewModel.volume.isNullOrEmpty()){
            holder.volume.text = setCompactPrice(itemsViewModel.volume.toDouble())
        }
        if(!itemsViewModel.marketCap.isNullOrEmpty()){
            holder.marketCap.text = setCompactPrice(itemsViewModel.marketCap.toDouble())
        }
    }

    override fun getItemCount(): Int = mList.size

    @SuppressLint("NotifyDataSetChanged")
    fun resetData(data : MutableList<CryptoCurrency>, timePeriod : String = this.timePeriod) {
        mList.clear()
        mList.addAll(data)
        this.timePeriod = timePeriod
        notifyDataSetChanged()
    }

    fun addData(data : MutableList<CryptoCurrency>){
        val insertIndex = mList.size
        mList.addAll(insertIndex, data)
        notifyItemRangeInserted(insertIndex, data.size)
    }

    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{

        private val mOnItemClickListener: OnItemClickListener = onItemClickListener
        private val mOnItemLongClickListener: OnItemLongClickListener = onItemLongClickListener

        val currencyLogo: ImageView = itemView.findViewById(R.id.crypto_logo)
        val currencyName: TextView = itemView.findViewById(R.id.crypto_name)
        val currencySymbol: TextView = itemView.findViewById(R.id.crypto_symbol)
        val currencyValue: TextView = itemView.findViewById(R.id.crypto_value)
        val percentageChangeText: TextView = itemView.findViewById(R.id.percentage_change_text)
        val percentChange: TextView = itemView.findViewById(R.id.percent_change)
        val volume: TextView = itemView.findViewById(R.id.volume)
        val marketCap: TextView = itemView.findViewById(R.id.market_cap)

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
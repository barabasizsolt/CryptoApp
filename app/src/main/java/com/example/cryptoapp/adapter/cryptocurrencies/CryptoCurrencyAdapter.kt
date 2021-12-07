package com.example.cryptoapp.adapter.cryptocurrencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setCompactPrice
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.loadSvg
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setPercentage
import com.example.cryptoapp.constant.cryptocurrencies.CryptoConstant.setPrice
import com.example.cryptoapp.interfaces.OnItemClickListener
import com.example.cryptoapp.interfaces.OnItemLongClickListener
import com.example.cryptoapp.model.allcryptocurrencies.CryptoCurrencyUIModel

class CryptoCurrencyAdapter (
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener)

    : ListAdapter<CryptoCurrencyUIModel, CryptoCurrencyAdapter.CryptoCurrencyViewHolder>(
        object : DiffUtil.ItemCallback<CryptoCurrencyUIModel>(){
            override fun areItemsTheSame(oldItem: CryptoCurrencyUIModel, newItem: CryptoCurrencyUIModel) = oldItem.uuid == newItem.uuid

            override fun areContentsTheSame(oldItem: CryptoCurrencyUIModel, newItem: CryptoCurrencyUIModel) = oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoCurrencyViewHolder = CryptoCurrencyViewHolder(
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.crypto_element, parent, false),
        onItemClickListener = onItemClickListener,
        onItemLongClickListener = onItemLongClickListener
    )

    override fun onBindViewHolder(holder: CryptoCurrencyViewHolder, position: Int) {
        val uiCryptoCurrencyModel = getItem(position)

        holder.percentageChangeText.text = uiCryptoCurrencyModel.timePeriod
        holder.currencyName.text = uiCryptoCurrencyModel.name
        holder.currencySymbol.text = uiCryptoCurrencyModel.symbol
        holder.currencyLogo.loadSvg(uiCryptoCurrencyModel.iconUrl)
        holder.currencyValue.text = setPrice(uiCryptoCurrencyModel.price.toDouble())
        setPercentage(uiCryptoCurrencyModel.change, holder.percentChange)
        holder.volume.text = setCompactPrice(uiCryptoCurrencyModel.volume.toDouble())
        holder.marketCap.text = setCompactPrice(uiCryptoCurrencyModel.marketCap.toDouble())
    }

    class CryptoCurrencyViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val onItemLongClickListener: OnItemLongClickListener)

        : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener
    {
        val currencyLogo: ImageView = itemView.findViewById(R.id.crypto_logo)
        val currencyName: TextView = itemView.findViewById(R.id.crypto_name)
        val currencySymbol: TextView = itemView.findViewById(R.id.crypto_symbol)
        val currencyValue: TextView = itemView.findViewById(R.id.crypto_value)
        val percentageChangeText: TextView = itemView.findViewById(R.id.percentage_change_text)
        val percentChange: TextView = itemView.findViewById(R.id.percent_change)
        val volume: TextView = itemView.findViewById(R.id.volume)
        val marketCap: TextView = itemView.findViewById(R.id.market_cap)

        override fun onClick(view: View) {
            onItemClickListener.onItemClick(bindingAdapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            onItemLongClickListener.onItemLongClick(bindingAdapterPosition)
            return true
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }
}
package com.example.cryptoapp.feature.cryptocurrency

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.CryptoConstant.ROTATE_180
import com.example.cryptoapp.data.constant.CryptoConstant.ROTATE_360
import com.example.cryptoapp.data.constant.CryptoConstant.getFormattedTime
import com.example.cryptoapp.data.constant.CryptoConstant.setPrice
import com.example.cryptoapp.data.constant.CryptoConstant.setValue
import com.example.cryptoapp.data.repository.Cache

class CryptoDetailsInfoFragment : Fragment() {
    private lateinit var rank: TextView
    private lateinit var supply: TextView
    private lateinit var circulating: TextView
    private lateinit var btcPrice: TextView
    private lateinit var allTimeHigh: TextView
    private lateinit var allTimeHighDate: TextView
    private lateinit var description: TextView
    private lateinit var dropDown: ImageView

    private var isDescriptionVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crypto_details_info, container, false)

        bindUI(view)
        initUI()

        return view
    }

    private fun bindUI(view: View) {
        rank = view.findViewById(R.id.crypto_rank_value)
        supply = view.findViewById(R.id.crypto_supply_value)
        circulating = view.findViewById(R.id.crypto_circulating_value)
        btcPrice = view.findViewById(R.id.crypto_btc_rice_value)
        allTimeHigh = view.findViewById(R.id.crypto_all_time_high_value)
        allTimeHighDate = view.findViewById(R.id.crypto_all_time_high_date_value)
        description = view.findViewById(R.id.crypto_description_text)
        dropDown = view.findViewById(R.id.description_drop_down)
    }

    private fun initUI() {
        val coin = Cache.getCryptoCurrency()
        val allTimeHighText = setPrice(coin.allTimeHigh.price.toDouble())
        val allTimeHighDateText = getFormattedTime(coin.allTimeHigh.timestamp)

        rank.text = coin.rank.toString()
        if (!coin.supply.total.isNullOrEmpty()) {
            supply.text = setValue(coin.supply.total.toDouble())
        }
        if (!coin.supply.circulating.isNullOrBlank()) {
            circulating.text = setValue(coin.supply.circulating.toDouble())
        }
        if (!coin.btcPrice.isNullOrBlank()) {
            btcPrice.text = String.format("%.7f", coin.btcPrice.toDouble()) + " Btc"
        }
        if (!coin.description.isNullOrEmpty()) {
            description.text = Html.fromHtml(coin.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        allTimeHigh.text = allTimeHighText
        allTimeHighDate.text = allTimeHighDateText

        description.visibility = View.GONE

        dropDown.setOnClickListener {
            if (isDescriptionVisible) {
                it.animate().rotation(ROTATE_360).start()
                description.visibility = View.GONE
                isDescriptionVisible = false
            } else {
                it.animate().rotation(ROTATE_180).start()
                description.visibility = View.VISIBLE
                isDescriptionVisible = true
            }
        }
    }
}

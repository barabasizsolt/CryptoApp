package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.R
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.ROTATE_180
import com.example.cryptoapp.data.constant.CryptoConstant.ROTATE_360
import com.example.cryptoapp.data.constant.CryptoConstant.getFormattedTime
import com.example.cryptoapp.data.constant.CryptoConstant.setPrice
import com.example.cryptoapp.data.constant.CryptoConstant.setValue
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CryptoDetailsInfoFragment : Fragment() {
    private lateinit var rank: TextView
    private lateinit var supply: TextView
    private lateinit var circulating: TextView
    private lateinit var btcPrice: TextView
    private lateinit var allTimeHigh: TextView
    private lateinit var allTimeHighDate: TextView
    private lateinit var description: TextView
    private lateinit var dropDown: ImageView
    private lateinit var cryptoCurrencyId: String

    private var isDescriptionVisible: Boolean = false

    private val cryptoCurrencyViewModel by sharedViewModel<CryptoCurrencyDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crypto_details_info, container, false)

        cryptoCurrencyId = requireArguments().getString(CryptoConstant.COIN_ID).toString()
        rank = view.findViewById(R.id.crypto_rank_value)
        supply = view.findViewById(R.id.crypto_supply_value)
        circulating = view.findViewById(R.id.crypto_circulating_value)
        btcPrice = view.findViewById(R.id.crypto_btc_rice_value)
        allTimeHigh = view.findViewById(R.id.crypto_all_time_high_value)
        allTimeHighDate = view.findViewById(R.id.crypto_all_time_high_date_value)
        description = view.findViewById(R.id.crypto_description_text)
        dropDown = view.findViewById(R.id.description_drop_down)
        initUI()

        return view
    }

    private fun initUI() {
        cryptoCurrencyViewModel.loadCryptoCurrencyDetails(cryptoCurrencyId)
        cryptoCurrencyViewModel.cryptoCurrencyDetails
            .onEach { response ->
                if (response != null && response.isSuccessful) {
                    Log.d("Details", response.body()?.data?.coin.toString())

                    val coin = response.body()?.data?.coin
                    val allTimeHighText = coin?.allTimeHigh?.price?.let { setPrice(it.toDouble()) }
                    val allTimeHighDateText = coin?.allTimeHigh?.let { getFormattedTime(it.timestamp) }

                    rank.text = coin?.rank.toString()
                    if (!coin?.supply?.total.isNullOrEmpty()) {
                        supply.text = coin?.supply?.total?.toDouble()?.let { setValue(it) }
                    }
                    if (!coin?.supply?.circulating.isNullOrBlank()) {
                        circulating.text = coin?.supply?.circulating?.let { setValue(it.toDouble()) }
                    }
                    if (!coin?.btcPrice.isNullOrBlank()) {
                        val price = String.format("%.7f", coin?.btcPrice?.toDouble()) + " Btc"
                        btcPrice.text = price
                    }
                    if (!coin?.description.isNullOrEmpty()) {
                        description.text =
                            Html.fromHtml(coin?.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
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
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}

package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.ROTATE_180
import com.example.cryptoapp.data.constant.CryptoConstant.ROTATE_360
import com.example.cryptoapp.data.constant.CryptoConstant.getFormattedTime
import com.example.cryptoapp.data.constant.CryptoConstant.setPrice
import com.example.cryptoapp.data.constant.CryptoConstant.setValue
import com.example.cryptoapp.databinding.FragmentCryptoDetailsInfoBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CryptoDetailsInfoFragment : Fragment() {
    private lateinit var cryptoCurrencyId: String
    private var isDescriptionVisible: Boolean = false
    private lateinit var binding: FragmentCryptoDetailsInfoBinding
    private val viewModel by viewModel<CryptoCurrencyDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoDetailsInfoBinding.inflate(inflater, container, false)
        cryptoCurrencyId = requireArguments().getString(CryptoConstant.COIN_ID).toString()
        initUI()
        return binding.root
    }

    private fun initUI() {
        viewModel.cryptoCurrencyDetails
            .onEach { response ->
                if (response != null && response.isSuccessful) {
                    Log.d("Details", response.body()?.data?.coin.toString())

                    val coin = response.body()?.data?.coin
                    val allTimeHighText = coin?.allTimeHigh?.price?.let { setPrice(it.toDouble()) }
                    val allTimeHighDateText = coin?.allTimeHigh?.let { getFormattedTime(it.timestamp) }

                    binding.cryptoRankValue.text = coin?.rank.toString()
                    if (!coin?.supply?.total.isNullOrEmpty()) {
                        binding.cryptoSupplyValue.text = coin?.supply?.total?.toDouble()?.let { setValue(it) }
                    }
                    if (!coin?.supply?.circulating.isNullOrBlank()) {
                        binding.cryptoCirculatingValue.text = coin?.supply?.circulating?.let { setValue(it.toDouble()) }
                    }
                    if (!coin?.btcPrice.isNullOrBlank()) {
                        val price = String.format("%.7f", coin?.btcPrice?.toDouble()) + " Btc"
                        binding.cryptoBtcRiceValue.text = price
                    }
                    if (!coin?.description.isNullOrEmpty()) {
                        binding.cryptoDescriptionText.text =
                            Html.fromHtml(coin?.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    }
                    binding.cryptoAllTimeHighValue.text = allTimeHighText
                    binding.cryptoAllTimeHighDateValue.text = allTimeHighDateText

                    binding.cryptoDescriptionText.visibility = View.GONE

                    binding.descriptionDropDown.setOnClickListener {
                        if (isDescriptionVisible) {
                            it.animate().rotation(ROTATE_360).start()
                            binding.cryptoDescriptionText.visibility = View.GONE
                            isDescriptionVisible = false
                        } else {
                            it.animate().rotation(ROTATE_180).start()
                            binding.cryptoDescriptionText.visibility = View.VISIBLE
                            isDescriptionVisible = true
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.loadCryptoCurrencyDetails(cryptoCurrencyId)
    }
}

package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cryptoapp.databinding.FragmentCryptoDetailsInfoBinding
import com.example.cryptoapp.feature.shared.BundleArgumentDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CryptoDetailsInfoFragment : Fragment() {
    private var isDescriptionVisible: Boolean = false
    private lateinit var binding: FragmentCryptoDetailsInfoBinding
    private val viewModel: CryptoCurrencyDetailsViewModel by viewModel { parametersOf(arguments?.cryptoCurrencyId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoDetailsInfoBinding.inflate(inflater, container, false)
        // initUI()
        return binding.root
    }

//    private fun initUI() {
//        viewModel.cryptoCurrencyDetailsInfo
//            .onEach { coin ->
//                if (coin != null) {
//                    val allTimeHighText = coin.allTimeHigh.price.convertToPrice()
//                    val allTimeHighDateText = coin.allTimeHigh.timestamp.getFormattedTime()
//                    val btcPrice = String.format("%.7f", coin.btcPrice.toDouble()) + " Btc"
//                    binding.cryptoRankValue.text = coin.rank
//                    binding.cryptoSupplyValue.text = coin.totalSupply.formatInput()
//                    binding.cryptoCirculatingValue.text = coin.circulating.formatInput()
//                    binding.cryptoBtcRiceValue.text = btcPrice
//                    binding.cryptoDescriptionText.text = Html.fromHtml(coin.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
//                    binding.cryptoAllTimeHighValue.text = allTimeHighText
//                    binding.cryptoAllTimeHighDateValue.text = allTimeHighDateText
//                    binding.cryptoDescriptionText.visibility = View.GONE
//
//                    binding.descriptionDropDown.setOnClickListener {
//                        if (isDescriptionVisible) {
//                            it.animate().rotation(ROTATE_360).start()
//                            binding.cryptoDescriptionText.visibility = View.GONE
//                            isDescriptionVisible = false
//                        } else {
//                            it.animate().rotation(ROTATE_180).start()
//                            binding.cryptoDescriptionText.visibility = View.VISIBLE
//                            isDescriptionVisible = true
//                        }
//                    }
//                }
//            }.launchIn(viewLifecycleOwner.lifecycleScope)
//    }

    companion object {
        private var Bundle.cryptoCurrencyId by BundleArgumentDelegate.String(key = "crypto_currency_id", defaultValue = "")

        fun newInstance(cryptoCurrencyId: String) = CryptoDetailsInfoFragment().apply {
            arguments = Bundle().apply {
                this.cryptoCurrencyId = cryptoCurrencyId
            }
        }
    }
}

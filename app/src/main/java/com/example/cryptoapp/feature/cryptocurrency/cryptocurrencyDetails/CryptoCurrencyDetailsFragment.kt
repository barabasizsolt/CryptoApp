package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyDetailsBinding
import com.example.cryptoapp.feature.shared.BundleArgumentDelegate
import com.example.cryptoapp.feature.shared.getColorFromAttr
import com.example.cryptoapp.feature.shared.toHexStringColor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CryptoCurrencyDetailsFragment : Fragment() {
    private lateinit var chartBackgroundColor: String
    private lateinit var chartTextColor: String
    private lateinit var chartColor: String
    private lateinit var binding: FragmentCryptoCurrencyDetailsBinding
    private val viewModel: CryptoCurrencyDetailsViewModel by viewModel {
        parametersOf(arguments?.cryptoCurrencyId, chartBackgroundColor, chartTextColor, chartColor)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCurrencyDetailsBinding.inflate(inflater, container, false)
        initChartColors()

        val detailsAdapter = CryptoCurrencyDetailsAdapter(
            onChipClicked = viewModel::onChipClicked,
            onDescriptionArrowClicked = viewModel::onDescriptionArrowClicked
        )
        binding.recyclerview.adapter = detailsAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.listItem.onEach(detailsAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

//    override fun onPause() {
//        super.onPause()
//        (activity as MainActivity).favoriteMenuItem.isVisible = false
//        (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist)
//    }
//
    private fun initChartColors() {
        val intBgColor =
            context?.let { R.attr.app_background_color.getColorFromAttr(context = it, defaultColor = Color.WHITE) }
        if (intBgColor != null) {
            chartBackgroundColor = intBgColor.toHexStringColor()
        }

        val intTxtColor =
            context?.let { R.attr.app_text_color.getColorFromAttr(context = it, defaultColor = Color.BLACK) }
        if (intTxtColor != null) {
            chartTextColor = intTxtColor.toHexStringColor()
        }

        val intChartColor =
            context?.let { R.attr.crypto_chart_color.getColorFromAttr(context = it, defaultColor = Color.YELLOW) }
        if (intChartColor != null) {
            chartColor = intChartColor.toHexStringColor()
        }
    }

//    private fun isFavourite() {
//        if (Cache.getUserWatchList().contains(cryptoCurrencyId)) {
//            (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist_gold)
//            isAddedToFavorite = true
//        }
//    }
//
//    private fun initTobBarListener() {
//        (activity as MainActivity).topAppBar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.favorite -> {
//                    // TODO: check if exist
//                    if (!isAddedToFavorite) {
//                        (activity as MainActivity).fireStore.collection(CURRENCY_FIRE_STORE_PATH)
//                            .add(
//                                hashMapOf(
//                                    "uuid" to cryptoCurrencyId,
//                                    "userid" to (activity as MainActivity).mAuth.currentUser?.uid
//                                )
//                            )
//                            .addOnSuccessListener {
//                                (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist_gold)
//                                isAddedToFavorite = true
//                                Cache.addUserWatchList(cryptoCurrencyId)
//                                Toast.makeText(requireContext(), "Added to watchlist", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener { e ->
//                                Log.w("fireStore", "Error adding document", e)
//                            }
//                    }
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//
//
//
//
//    private fun refreshChart(data: MutableList<DataEntry>) {
//        val series = areaChart.area(data)
//        with(series) {
//            setName("Cryptocurrency History")
//            setStroke("1 $chartTextColor")
//            hovered.markers.setEnabled(true)
//            series.markers.setZIndex(100.0)
//            hovered.setStroke("3 $chartTextColor")
//            fill(chartColor, 5)
//        }
//        with(series.hovered.markers) {
//            setType(MarkerType.CIRCLE)
//            setSize(4.0)
//            setStroke("1.5 $chartTextColor")
//        }
//        areaChart.setData(data)
//    }

    companion object {
        private var Bundle.cryptoCurrencyId by BundleArgumentDelegate.String(key = "crypto_currency_id", defaultValue = "")

        fun newInstance(cryptoCurrencyId: String) = CryptoCurrencyDetailsFragment().apply {
            arguments = Bundle().apply {
                this.cryptoCurrencyId = cryptoCurrencyId
            }
        }
    }
}

package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.anychart.anychart.AnyChart.area
import com.anychart.anychart.Cartesian
import com.anychart.anychart.DataEntry
import com.anychart.anychart.HoverMode
import com.anychart.anychart.MarkerType
import com.anychart.anychart.ScaleStackMode
import com.anychart.anychart.Stroke
import com.anychart.anychart.StrokeLineCap
import com.anychart.anychart.StrokeLineJoin
import com.anychart.anychart.TooltipDisplayMode
import com.anychart.anychart.ValueDataEntry
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetailsUIModel
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.SingleCryptoCurrencyHistoryResponse
import com.example.cryptoapp.data.repository.Cache
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyDetailsBinding
import com.example.cryptoapp.feature.cryptocurrency.Constant.CALENDAR
import com.example.cryptoapp.feature.cryptocurrency.Constant.CURRENCY_FIRE_STORE_PATH
import com.example.cryptoapp.feature.cryptocurrency.Constant.DAY7
import com.example.cryptoapp.feature.cryptocurrency.Constant.HOUR24
import com.example.cryptoapp.feature.cryptocurrency.Constant.MAX_HOUR
import com.example.cryptoapp.feature.cryptocurrency.Constant.MAX_MONTH
import com.example.cryptoapp.feature.cryptocurrency.Constant.YEAR1
import com.example.cryptoapp.feature.cryptocurrency.Constant.YEAR6
import com.example.cryptoapp.feature.shared.BundleArgumentDelegate
import com.example.cryptoapp.feature.shared.convertToCompactPrice
import com.example.cryptoapp.feature.shared.convertToPrice
import com.example.cryptoapp.feature.shared.getColorFromAttr
import com.example.cryptoapp.feature.shared.getTime
import com.example.cryptoapp.feature.shared.handleReplace
import com.example.cryptoapp.feature.shared.toHexStringColor
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MONTH

class CryptoCurrencyDetailsFragment : Fragment() {
    private val areaChart: Cartesian = area()
    private lateinit var cryptoCurrencyId: String
    private var currentTimeFrame = HOUR24
    private var isAddedToFavorite = false
    private lateinit var chartBackgroundColor: String
    private lateinit var chartTextColor: String
    private lateinit var chartColor: String
    private lateinit var binding: FragmentCryptoCurrencyDetailsBinding
    private val viewModel: CryptoCurrencyDetailsViewModel by viewModel { parametersOf(cryptoCurrencyId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCurrencyDetailsBinding.inflate(inflater, container, false)
        cryptoCurrencyId = arguments?.cryptoCurrencyId.toString()
        initChartColors()
        initializeChart()

        viewModel.cryptoCurrencyDetails
            .onEach { cryptoDetails ->
                if (cryptoDetails != null) {
                    viewModel.refreshCoinHistory(uuid = cryptoCurrencyId, timePeriod = HOUR24)
                    initUI(cryptoDetails)
                    binding.tabLayout.getTabAt(1)!!.select()
                    binding.tabLayout.getTabAt(0)!!.select()
                    isFavourite()
                    (activity as MainActivity).favoriteMenuItem.isVisible = true
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.cryptoCurrencyDetailsHistory
            .onEach { cryptoHistory ->
                if (cryptoHistory != null) {
                    when (currentTimeFrame) {
                        HOUR24 -> {
                            val currencyHistory = createDataForAreaChart(cryptoHistory.history, HOUR24)
                            refreshChart(currencyHistory)
                        }
                        DAY7 -> {
                            val currencyHistory = createDataForAreaChart(cryptoHistory.history, DAY7)
                            refreshChart(currencyHistory)
                        }
                        YEAR1 -> {
                            val currencyHistory = createDataForAreaChart(cryptoHistory.history, YEAR1)
                            refreshChart(currencyHistory)
                        }
                        YEAR6 -> {
                            val currencyHistory = createDataForAreaChart(cryptoHistory.history, YEAR6)
                            refreshChart(currencyHistory)
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        initTobBarListener()
        initializeChipGroup(cryptoCurrencyId)
        initializeTabLayout()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).favoriteMenuItem.isVisible = false
        (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist)
    }

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

    private fun initUI(coin: CryptoCurrencyDetailsUIModel) {
        val currentTime = System.currentTimeMillis().getTime()
        var currentHour = currentTime.hour.toString()
        var currentMinute = currentTime.minute.toString()
        if (currentHour.toInt() < 10) {
            currentHour = "0$currentHour"
        }
        if (currentMinute.toInt() < 10) {
            currentMinute = "0$currentMinute"
        }
        val coinValueSymbol = coin.symbol + "/" + "USD" + " - AVG - " + currentHour + ":" + currentMinute
        binding.cryptoLogo.loadImage(coin.iconUrl)
        binding.cryptoName.text = coin.name
        binding.cryptoSymbol.text = coin.symbol
        binding.cryptoValueSymbol.text = coinValueSymbol
        binding.cryptoPrice.text = coin.price.convertToPrice()
        binding.percentChange24h.setPercentage(coin.change)
        binding.volume.text = coin.volume.convertToCompactPrice()
        binding.marketCap.text = coin.marketCap.convertToCompactPrice()
    }

    private fun isFavourite() {
        if (Cache.getUserWatchList().contains(cryptoCurrencyId)) {
            (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist_gold)
            isAddedToFavorite = true
        }
    }

    private fun initTobBarListener() {
        (activity as MainActivity).topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    // TODO: check if exist
                    if (!isAddedToFavorite) {
                        (activity as MainActivity).fireStore.collection(CURRENCY_FIRE_STORE_PATH)
                            .add(
                                hashMapOf(
                                    "uuid" to cryptoCurrencyId,
                                    "userid" to (activity as MainActivity).mAuth.currentUser?.uid
                                )
                            )
                            .addOnSuccessListener {
                                (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist_gold)
                                isAddedToFavorite = true
                                Cache.addUserWatchList(cryptoCurrencyId)
                                Toast.makeText(requireContext(), "Added to watchlist", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w("fireStore", "Error adding document", e)
                            }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun initializeChipGroup(cryptoCurrencyId: String) {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_24h -> {
                    viewModel.refreshCoinHistory(uuid = cryptoCurrencyId, timePeriod = HOUR24)
                    currentTimeFrame = HOUR24
                }
                R.id.chip_7d -> {
                    viewModel.refreshCoinHistory(uuid = cryptoCurrencyId, timePeriod = DAY7)
                    currentTimeFrame = DAY7
                }
                R.id.chip_1y -> {
                    viewModel.refreshCoinHistory(uuid = cryptoCurrencyId, timePeriod = YEAR1)
                    currentTimeFrame = YEAR1
                }
                R.id.chip_6y -> {
                    viewModel.refreshCoinHistory(uuid = cryptoCurrencyId, timePeriod = YEAR6)
                    currentTimeFrame = YEAR6
                }
            }
        }
    }

    private fun initializeTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> activity?.supportFragmentManager?.handleReplace(containerId = R.id.crypto_details_fragment_container) {
                        CryptoDetailsInfoFragment.newInstance(cryptoCurrencyId)
                    }
                    1 -> {
                        // TODO:Implement it
                    }
                    2 -> {
                        // TODO:Implement it
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun createDataForAreaChart(history: MutableList<SingleCryptoCurrencyHistoryResponse>, timeFrame: String): MutableList<DataEntry> {
        val currencyHistory: MutableList<DataEntry> = ArrayList()

        // TODO:refactor it
        when (timeFrame) {
            HOUR24 -> {
                val groupedHistory = sortedMapOf<Int, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<Int, Double?>> = mutableListOf()

                val currentHour: Int = CALENDAR.get(HOUR_OF_DAY)

                history.forEach { curr ->
                    val time = curr.timestamp.getTime().hour
                    if (!groupedHistory.containsKey(time)) {
                        groupedHistory[time] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) {
                        groupedHistory[time]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem ->
                    tmpHistory.add(Pair(elem.key, elem.value.maxOfOrNull { it }))
                }

                for (i in (currentHour - 12)..(currentHour + 12)) {
                    val idx = i.mod(MAX_HOUR)
                    if (idx < tmpHistory.size) {
                        val elem = tmpHistory[idx]
                        currencyHistory.add(ValueDataEntry(elem.first.toString() + ":00", elem.second))
                    }
                }
            }
            DAY7 -> {
                val groupedHistory = mutableMapOf<DayOfWeek, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<DayOfWeek, Double?>> = mutableListOf()

                val currentDay: Int = LocalDate.now().dayOfWeek.value - 1

                history.sortWith(compareBy { it.timestamp.getTime().dayOfWeek.ordinal })
                history.forEach { curr ->
                    val dayOfWeek = curr.timestamp.getTime().dayOfWeek
                    if (!groupedHistory.containsKey(dayOfWeek)) {
                        groupedHistory[dayOfWeek] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) {
                        groupedHistory[dayOfWeek]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem ->
                    tmpHistory.add(Pair(elem.key, elem.value.maxOfOrNull { it }))
                }

                for (i in (currentDay - 3)..(currentDay + 3)) {
                    val idx = i.mod(MAX_HOUR)
                    if (idx < tmpHistory.size) {
                        val elem = tmpHistory[idx]
                        currencyHistory.add(ValueDataEntry(elem.first.name.substring(0, 3), elem.second))
                    }
                }
            }
            YEAR1 -> {
                val groupedHistory = mutableMapOf<Month, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<Month, Double?>> = mutableListOf()

                val currentMonth: Int = CALENDAR.get(MONTH)

                history.sortWith(compareBy { it.timestamp.getTime().month.ordinal })
                history.forEach { curr ->
                    val month = curr.timestamp.getTime().month
                    if (!groupedHistory.containsKey(month)) {
                        groupedHistory[month] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) {
                        groupedHistory[month]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem ->
                    tmpHistory.add(Pair(elem.key, elem.value.maxOfOrNull { it }))
                }

                for (i in (currentMonth - 6)..(currentMonth + 6)) {
                    val idx = i.mod(MAX_MONTH)
                    if (idx < tmpHistory.size) {
                        val elem = tmpHistory[idx]
                        currencyHistory.add(ValueDataEntry(elem.first.name.substring(0, 3), elem.second))
                    }
                }
            }
            YEAR6 -> {
                val groupedHistory = mutableMapOf<String, MutableList<Double>>()

                history.sortWith(compareBy { it.timestamp.getTime().year })
                history.forEach { curr ->
                    val year = curr.timestamp.getTime().year.toString()
                    if (!groupedHistory.containsKey(year)) {
                        groupedHistory[year] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) {
                        groupedHistory[year]?.add(curr.price.toDouble())
                    }
                }

                groupedHistory.forEach { elem -> currencyHistory.add(ValueDataEntry(elem.key, elem.value.maxOfOrNull { it })) }
            }
        }

        return currencyHistory
    }

    private fun initializeChart() {
        binding.anyChartView.setBackgroundColor(chartBackgroundColor)

        with(areaChart) {
            yScale.setStackMode(ScaleStackMode.VALUE)
            yGrid.setEnabled(true)
            background.fill(chartBackgroundColor, 0)
            interactivity.setHoverMode(HoverMode.BY_X)
        }
        with(areaChart.crosshair) {
            setEnabled(true)
            setYStroke(null as Stroke?, null as Number?, null as String?, null as StrokeLineJoin?, null as StrokeLineCap?)
            setXStroke(chartTextColor, 1.0, null, null as StrokeLineJoin?, null as StrokeLineCap?)
            setZIndex(39.0)
            getYLabel(0).setEnabled(true)
        }
        with(areaChart.tooltip) {
            setValuePrefix("$")
            setDisplayMode(TooltipDisplayMode.SINGLE)
        }
        with(areaChart.legend) {
            setEnabled(true)
            setFontSize(13.0)
            setPadding(0.0, 0.0, 20.0, 0.0)
        }
        with(areaChart.getXAxis(0)) {
            setTitle(false)
            labels.setFontColor(chartTextColor)
        }
        with(areaChart.getYAxis(0)) {
            setTitle("Value (in US Dollars)")
            title.setFontColor(chartTextColor)
            labels.setFormat("\${%value}")
            labels.setFontColor(chartTextColor)
        }
        binding.anyChartView.setChart(areaChart)
    }

    private fun refreshChart(data: MutableList<DataEntry>) {
        val series = areaChart.area(data)
        with(series) {
            setName("Cryptocurrency History")
            setStroke("1 $chartTextColor")
            hovered.markers.setEnabled(true)
            series.markers.setZIndex(100.0)
            hovered.setStroke("3 $chartTextColor")
            fill(chartColor, 5)
        }
        with(series.hovered.markers) {
            setType(MarkerType.CIRCLE)
            setSize(4.0)
            setStroke("1.5 $chartTextColor")
        }
        areaChart.setData(data)
    }

    companion object {
        private var Bundle.cryptoCurrencyId by BundleArgumentDelegate.String(key = "crypto_currency_id", defaultValue = "")

        fun newInstance(cryptoCurrencyId: String) = CryptoCurrencyDetailsFragment().apply {
            arguments = Bundle().apply {
                this.cryptoCurrencyId = cryptoCurrencyId
            }
        }
    }
}

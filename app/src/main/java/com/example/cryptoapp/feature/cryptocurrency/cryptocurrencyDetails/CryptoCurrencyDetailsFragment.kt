package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.anychart.anychart.AnyChart.area
import com.anychart.anychart.AnyChartView
import com.anychart.anychart.Cartesian
import com.anychart.anychart.Crosshair
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
import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.constant.CryptoConstant.CALENDAR
import com.example.cryptoapp.data.constant.CryptoConstant.CURRENCY_FIRE_STORE_PATH
import com.example.cryptoapp.data.constant.CryptoConstant.DAY7
import com.example.cryptoapp.data.constant.CryptoConstant.HOUR24
import com.example.cryptoapp.data.constant.CryptoConstant.MAX_HOUR
import com.example.cryptoapp.data.constant.CryptoConstant.MAX_MONTH
import com.example.cryptoapp.data.constant.CryptoConstant.YEAR1
import com.example.cryptoapp.data.constant.CryptoConstant.YEAR6
import com.example.cryptoapp.data.constant.CryptoConstant.getTime
import com.example.cryptoapp.data.constant.CryptoConstant.loadSvg
import com.example.cryptoapp.data.constant.CryptoConstant.setCompactPrice
import com.example.cryptoapp.data.constant.CryptoConstant.setPercentage
import com.example.cryptoapp.data.constant.CryptoConstant.setPrice
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoHistory
import com.example.cryptoapp.data.repository.Cache
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyDetailsBinding
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MONTH
import kotlin.collections.ArrayList

class CryptoCurrencyDetailsFragment : Fragment() {
    private lateinit var areaChart: Cartesian
    private lateinit var cryptoLogo: ImageView
    private lateinit var cryptoName: TextView
    private lateinit var cryptoSymbol: TextView
    private lateinit var cryptoPrice: TextView
    private lateinit var cryptoValueSymbol: TextView
    private lateinit var percentageChange24H: TextView
    private lateinit var volume: TextView
    private lateinit var marketCap: TextView
    private lateinit var chipGroup: ChipGroup
    private lateinit var tabLayout: TabLayout
    private lateinit var cryptoCurrencyId: String

    private var currentTimeFrame = HOUR24

    private var isAddedToFavorite = false

    private lateinit var binding: FragmentCryptoCurrencyDetailsBinding
    private val cryptoCurrencyViewModel by viewModel<CryptoCurrencyDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCurrencyDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        cryptoCurrencyId = requireArguments().getString(CryptoConstant.COIN_ID).toString()
        Log.d("ID", cryptoCurrencyId)
        initializeChart(view)
        tabLayout = view.findViewById(R.id.tab_layout)
        cryptoLogo = view.findViewById(R.id.crypto_logo)
        cryptoName = view.findViewById(R.id.crypto_name)
        cryptoSymbol = view.findViewById(R.id.crypto_symbol)
        cryptoPrice = view.findViewById(R.id.crypto_price)
        cryptoValueSymbol = view.findViewById(R.id.crypto_value_symbol)
        percentageChange24H = view.findViewById(R.id.percent_change_24h)
        volume = view.findViewById(R.id.volume)
        marketCap = view.findViewById(R.id.market_cap)
        chipGroup = view.findViewById(R.id.chip_group)

        cryptoCurrencyViewModel.loadCryptoCurrencyDetails(cryptoCurrencyId)
        cryptoCurrencyViewModel.cryptoCurrencyDetails
            .onEach { response ->
                if (response != null && response.isSuccessful) {
                    response.body()?.let { cryptoDetails ->
                        initUI(cryptoDetails)
                        tabLayout.getTabAt(1)!!.select()
                        tabLayout.getTabAt(0)!!.select()
                        isFavourite()
                        (activity as MainActivity).favoriteMenuItem.isVisible = true
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        cryptoCurrencyViewModel.loadCryptoCurrencyHistory(uuid = cryptoCurrencyId, timePeriod = HOUR24)
        cryptoCurrencyViewModel.cryptoCurrencyHistory
            .onEach { response ->
                if (response != null && response.isSuccessful) {
                    when (currentTimeFrame) {
                        HOUR24 -> {
                            val currencyHistory = createDataForAreaChart(response.body()?.data?.history!! as MutableList, HOUR24)
                            refreshChart(currencyHistory)
                        }
                        DAY7 -> {
                            val currencyHistory = createDataForAreaChart(response.body()?.data?.history!! as MutableList, DAY7)
                            refreshChart(currencyHistory)
                        }
                        YEAR1 -> {
                            val currencyHistory = createDataForAreaChart(response.body()?.data?.history!! as MutableList, YEAR1)
                            refreshChart(currencyHistory)
                        }
                        YEAR6 -> {
                            val currencyHistory = createDataForAreaChart(response.body()?.data?.history!! as MutableList, YEAR6)
                            refreshChart(currencyHistory)
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        initTobBarListener()
        initializeChipGroup(cryptoCurrencyId)
        initializeTabLayout()

        return view
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).favoriteMenuItem.isVisible = false
        (activity as MainActivity).favoriteMenuItem.setIcon(R.drawable.ic_watchlist)
    }

    private fun initUI(cryptoCurrencyDetails: CryptoCurrencyDetails) {
        val coin = cryptoCurrencyDetails.data.coin
        val currentTime = getTime(System.currentTimeMillis())
        var currentHour = currentTime.hour.toString()
        var currentMinute = currentTime.minute.toString()
        if (currentHour.toInt() < 10) {
            currentHour = "0$currentHour"
        }
        if (currentMinute.toInt() < 10) {
            currentMinute = "0$currentMinute"
        }
        val coinValueSymbol = coin.symbol + "/" + "USD" + " - AVG - " + currentHour + ":" + currentMinute
        if (!coin.iconUrl.isNullOrEmpty()) {
            cryptoLogo.loadSvg(coin.iconUrl)
        }
        cryptoName.text = coin.name
        cryptoSymbol.text = coin.symbol
        cryptoValueSymbol.text = coinValueSymbol
        if (!coin.price.isNullOrEmpty()) {
            cryptoPrice.text = setPrice(coin.price.toDouble())
        }
        if (!coin.change.isNullOrEmpty()) {
            setPercentage(coin.change, percentageChange24H)
        }
        if (!coin.volume.isNullOrEmpty()) {
            volume.text = setCompactPrice(coin.volume)
        }
        if (!coin.marketCap.isNullOrEmpty()) {
            marketCap.text = setCompactPrice(coin.marketCap)
        }
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
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_24h -> {
                    Log.d("CH24", "Chipped")
                    cryptoCurrencyViewModel.loadCryptoCurrencyHistory(uuid = cryptoCurrencyId, timePeriod = HOUR24)
                    currentTimeFrame = HOUR24
                }
                R.id.chip_7d -> {
                    Log.d("CH7", "Chipped")
                    cryptoCurrencyViewModel.loadCryptoCurrencyHistory(uuid = cryptoCurrencyId, timePeriod = DAY7)
                    currentTimeFrame = DAY7
                }
                R.id.chip_1y -> {
                    Log.d("CH1", "Chipped")
                    cryptoCurrencyViewModel.loadCryptoCurrencyHistory(uuid = cryptoCurrencyId, timePeriod = YEAR1)
                    currentTimeFrame = YEAR1
                }
                R.id.chip_6y -> {
                    Log.d("CH3", "Chipped")
                    cryptoCurrencyViewModel.loadCryptoCurrencyHistory(uuid = cryptoCurrencyId, timePeriod = YEAR6)
                    currentTimeFrame = YEAR6
                }
            }
        }
    }

    private fun initializeTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        val fragment = CryptoDetailsInfoFragment()
                        val bundle = Bundle()
                        bundle.putString(CryptoConstant.COIN_ID, cryptoCurrencyId)
                        fragment.arguments = bundle
                        (activity as MainActivity).replaceFragment(fragment, R.id.crypto_details_fragment_container)
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

    private fun createDataForAreaChart(history: MutableList<CryptoHistory>, timeFrame: String): MutableList<DataEntry> {
        val currencyHistory: MutableList<DataEntry> = ArrayList()

        // TODO:refactor it
        when (timeFrame) {
            HOUR24 -> {
                val groupedHistory = sortedMapOf<Int, MutableList<Double>>()
                val tmpHistory: MutableList<Pair<Int, Double?>> = mutableListOf()

                val currentHour: Int = CALENDAR.get(HOUR_OF_DAY)

                history.forEach { curr ->
                    val time = getTime(curr.timestamp).hour
                    if (!groupedHistory.containsKey(time)) {
                        groupedHistory[time] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) { groupedHistory[time]?.add(curr.price.toDouble()) }
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

                history.sortWith(compareBy { getTime(it.timestamp).dayOfWeek.ordinal })
                history.forEach { curr ->
                    val dayOfWeek = getTime(curr.timestamp).dayOfWeek
                    if (!groupedHistory.containsKey(dayOfWeek)) {
                        groupedHistory[dayOfWeek] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) { groupedHistory[dayOfWeek]?.add(curr.price.toDouble()) }
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

                history.sortWith(compareBy { getTime(it.timestamp).month.ordinal })
                history.forEach { curr ->
                    val month = getTime(curr.timestamp).month
                    if (!groupedHistory.containsKey(month)) {
                        groupedHistory[month] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) { groupedHistory[month]?.add(curr.price.toDouble()) }
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

                history.sortWith(compareBy { getTime(it.timestamp).year })
                history.forEach { curr ->
                    val year = getTime(curr.timestamp).year.toString()
                    if (!groupedHistory.containsKey(year)) {
                        groupedHistory[year] = mutableListOf()
                    }
                    if (!curr.price.isNullOrBlank()) { groupedHistory[year]?.add(curr.price.toDouble()) }
                }

                groupedHistory.forEach { elem -> currencyHistory.add(ValueDataEntry(elem.key, elem.value.maxOfOrNull { it })) }
            }
        }

        return currencyHistory
    }

    private fun initializeChart(view: View) {
        val anyChartView: AnyChartView = view.findViewById(R.id.any_chart_view)
        anyChartView.setBackgroundColor("#212121")

        areaChart = area()

        val crossHair: Crosshair = areaChart.crosshair
        crossHair.setEnabled(true)

        crossHair.setYStroke(null as Stroke?, null as Number?, null as String?, null as StrokeLineJoin?, null as StrokeLineCap?)
            .setXStroke("#fff", 1.0, null, null as StrokeLineJoin?, null as StrokeLineCap?)
            .setZIndex(39.0)
        crossHair.getYLabel(0).setEnabled(true)

        areaChart.yScale.setStackMode(ScaleStackMode.VALUE)
        areaChart.yGrid.setEnabled(true)
        areaChart.background.fill("#212121", 0)

        areaChart.legend.setEnabled(true)
        areaChart.legend.setFontSize(13.0)
        areaChart.legend.setPadding(0.0, 0.0, 20.0, 0.0)

        areaChart.getXAxis(0).setTitle(false)
        areaChart.getXAxis(0).labels.setFontColor("#fff")

        areaChart.getYAxis(0).labels.setFontColor("#fff")
        areaChart.getYAxis(0).labels.setFormat("\${%value}")
        areaChart.getYAxis(0).title.setFontColor("#fff")
        areaChart.getYAxis(0).setTitle("Value (in US Dollars)")

        areaChart.interactivity.setHoverMode(HoverMode.BY_X)
        areaChart.tooltip
            .setValuePrefix("$")
            .setDisplayMode(TooltipDisplayMode.SINGLE)

        anyChartView.setChart(areaChart)
    }

    private fun refreshChart(data: MutableList<DataEntry>) {
        val series = areaChart.area(data)
        series.setName("Cryptocurrency History")
        series.setStroke("3 #fff")
        series.hovered.setStroke("3 #fff")
        series.hovered.markers.setEnabled(true)
        series.hovered.markers
            .setType(MarkerType.CIRCLE)
            .setSize(4.0)
            .setStroke("1.5 #fff")
        series.markers.setZIndex(100.0)
        series.fill("#64ffda", 5)
        areaChart.setData(data)
    }
}

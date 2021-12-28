package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cryptoapp.databinding.FragmentCryptoCurrencyDetailsBinding
import com.example.cryptoapp.feature.shared.BundleArgumentDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.R
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CryptoCurrencyDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCryptoCurrencyDetailsBinding
    private val viewModel: CryptoCurrencyDetailsViewModel by viewModel {
        parametersOf(
            arguments?.cryptoCurrencyId,
            MaterialColors.getColor(requireContext(), R.attr.app_background_color, Color.WHITE),
            MaterialColors.getColor(requireContext(), R.attr.app_text_color, Color.BLACK),
            ContextCompat.getColor(requireContext(), R.color.orange)
        )
    }

    private lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCurrencyDetailsBinding.inflate(inflater, container, false)

        val detailsAdapter = CryptoCurrencyDetailsAdapter(
            onChipClicked = viewModel::onChipClicked,
            onDescriptionArrowClicked = viewModel::onDescriptionArrowClicked
        )
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.let {
            it.adapter = detailsAdapter
            it.layoutManager = layoutManager
            it.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
        }
        viewModel.listItem.onEach(detailsAdapter::submitList).launchIn(viewLifecycleOwner.lifecycleScope)

//        chart = binding.lineChart
//        chart.setTouchEnabled(false)
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)
//        chart.setPinchZoom(false)
//        chart.setDrawGridBackground(false)
//        chart.description.isEnabled = false
//        chart.legend.isEnabled = true
//        chart.legend.textColor = Color.WHITE
//        chart.legend.textSize = 13f
//        chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//        chart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
//        chart.legend.setDrawInside(false)
//        val legendEntry = LegendEntry()
//        legendEntry.label = "Cryptocurrency value changes"
//        legendEntry.formColor = ContextCompat.getColor(requireContext(), R.color.orange)
//        legendEntry.form
//        chart.legend.setCustom(arrayListOf(legendEntry))
//        chart.xAxis.textColor = Color.WHITE
//        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        chart.xAxis.setDrawGridLines(true)
//        chart.axisLeft.textColor = Color.WHITE
//        chart.axisLeft.valueFormatter = CryptoAxisFormatter()
//        chart.axisLeft.setDrawGridLines(true)
//        chart.setBackgroundColor(Color.BLACK)
//        chart.data = LineData(arrayListOf<ILineDataSet>(refresh(ctr = 25f)))
//        chart.invalidate()

//        var ctr = 50
//        binding.button.setOnClickListener {
//            chart.data.clearValues()
//            chart.data = LineData(arrayListOf<ILineDataSet>(refresh(ctr = ctr.toFloat())))
//            ctr += 20
//            chart.notifyDataSetChanged()
//            chart.invalidate()
//        }


        return binding.root
    }

//    private fun refresh(ctr: Float): LineDataSet {
//        val entryArrayList: ArrayList<Entry> = ArrayList()
//        entryArrayList.add(Entry(0f, 60f, "1"))
//        entryArrayList.add(Entry(1f, 55f, "2"))
//        entryArrayList.add(Entry(2f, 60f, "3"))
//        entryArrayList.add(Entry(3f, 40f, "4"))
//        entryArrayList.add(Entry(4f, 45f, "5"))
//        entryArrayList.add(Entry(5f, 36f, "6"))
//        entryArrayList.add(Entry(6f, ctr, "7"))
//
//        val xAxisLabel: ArrayList<String> = ArrayList()
//        xAxisLabel.add("Mon")
//        xAxisLabel.add("Tue")
//        xAxisLabel.add("Wed")
//        xAxisLabel.add("Thu")
//        xAxisLabel.add("Fri")
//        xAxisLabel.add("Sat")
//        xAxisLabel.add("Sun")
//        chart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)
//
//        val lineDataSet = LineDataSet(entryArrayList, "")
//        lineDataSet.lineWidth = 3f
//        lineDataSet.color = Color.WHITE
//        lineDataSet.highLightColor = Color.RED
//        lineDataSet.setDrawValues(false)
//        lineDataSet.circleRadius = 10f
//        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
//        lineDataSet.cubicIntensity = 0.1f
//        lineDataSet.setDrawFilled(true)
//        lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.orange)
//        lineDataSet.fillAlpha = 255
//        lineDataSet.setDrawCircles(false)
//
//       return lineDataSet
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

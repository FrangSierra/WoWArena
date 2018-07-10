package durdinstudios.wowarena.misc

import durdinstudios.wowarena.data.models.warcraft.pvp.ArenaBracket
import durdinstudios.wowarena.domain.arena.model.ArenaInfo
import durdinstudios.wowarena.domain.arena.model.ArenaStats
import durdinstudios.wowarena.settings.Settings
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import java.util.*
import java.util.concurrent.TimeUnit


object LineChartUtils {

    fun prepareChartData(chart: LineChartView?, stats: List<ArenaStats>, settings: Settings): Boolean {

        val lines = ArrayList<Line>()
        val vs2Values = stats.filter { it.vs2 != null }.map { it.vs2!! to it.timestamp }
        createLine(vs2Values, ArenaBracket.BRACKET_2_VS_2)?.takeIf { settings.show2vs2Stats }?.let { lines.add(it) }
        val vs3Values = stats.filter { it.vs3 != null }.map { it.vs3!! to it.timestamp }
        createLine(vs3Values, ArenaBracket.BRACKET_3_VS_3)?.takeIf { settings.show3vs3Stats }?.let { lines.add(it) }
        val rbgValues = stats.filter { it.rbg != null }.map { it.rbg!! to it.timestamp }
        createLine(rbgValues, ArenaBracket.RBG)?.takeIf { settings.showRbgStats }?.let { lines.add(it) }
        if (lines.isEmpty()) return false
        if (lines.all { line -> line.values.all { value -> value.y == line.values[0].y } }) return false
        val data = LineChartData(lines)

        val filteredDates = vs2Values.plus(vs3Values).plus(rbgValues).map { it.second }.distinct().sorted()

        val axisXValues = getPossibleDatesValues(filteredDates).map { AxisValue(it.first.toFloat()).setLabel(it.second) }

        data.axisXBottom = Axis() //TIME
                .setValues(axisXValues)
                .setHasLines(true)
                .setHasTiltedLabels(true)

        data.axisYLeft = Axis() //Rating
                .setHasLines(true)
                .setAutoGenerated(true)
                .setHasTiltedLabels(true)

        chart?.lineChartData = data
        return true
    }

    private fun getPossibleDatesValues(filteredDates: List<Long>): List<Pair<Int, String>> {
        return filteredDates.mapToDate()
                .distinctBy { it }
                .mapIndexed { index, date -> index to date }
    }

    private fun createLine(info: List<Pair<ArenaInfo, Long>>, bracket: ArenaBracket): Line? {
        val values = ArrayList<PointValue>()
        val filteredValues = info.distinctBy { it.second.toMonthlyDay() }
                .filter { it.first.rating > 0 }
                .sortedBy { it.second }

        filteredValues.mapIndexedTo(values) { index, data ->
            PointValue(index.toFloat(), data.first.rating.toFloat())
        }

        if (values.size <= 1) return null

        return Line(values).apply {
            color = when (bracket) {
                ArenaBracket.BRACKET_2_VS_2 -> ChartUtils.COLORS[0]
                ArenaBracket.BRACKET_3_VS_3 -> ChartUtils.COLORS[1]
                ArenaBracket.RBG -> ChartUtils.COLORS[2]
            }

            shape = ValueShape.CIRCLE
            isCubic = isCubic
            isFilled = isFilled


            setHasLabels(true)
            setHasLines(true)
            setHasPoints(true)
        }
    }
}
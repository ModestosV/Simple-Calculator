package com.simplemobiletools.calculator.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.opencsv.CSVWriter
import com.simplemobiletools.calculator.BuildConfig
import com.simplemobiletools.calculator.R
import com.simplemobiletools.calculator.export.ExportManager
import com.simplemobiletools.calculator.helpers.*
import com.simplemobiletools.calculator.helpers.CONSTANT.HISTORY_FILE
import com.simplemobiletools.commons.helpers.LICENSE_AUTOFITTEXTVIEW
import com.simplemobiletools.commons.helpers.LICENSE_ESPRESSO
import com.simplemobiletools.commons.helpers.LICENSE_KOTLIN
import com.simplemobiletools.commons.helpers.LICENSE_ROBOLECTRIC
import kotlinx.android.synthetic.main.activity_history.*
import java.io.File
import java.io.FileWriter
import java.util.*


/**
 * Created by Marc-Andre Dragon on 2018-03-01.
 */
class HistoryActivity : SimpleActivity(), Calculator {

    private lateinit var calc: CalculatorImpl
    private lateinit var equations: ArrayList<String>
    private lateinit var results: ArrayList<String>
    private lateinit var export: ExportManager
    private lateinit var  exportFile: File
    private var index = 0


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (index == 0) {
            menu?.add(R.string.about)
            menu?.add(R.string.binary_calculator)
            menu?.add(R.string.export)
            menu?.add(R.string.settings)
            menu?.add(R.string.unit_conversion)
            index++
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title?.toString()) {
            getString(R.string.export) -> try { exportData() } catch (e : Exception) { System.err.print(e.localizedMessage) }
            getString(R.string.settings) -> launchSettings()
            getString(R.string.about) -> launchAbout()
            getString(R.string.unit_conversion) -> launchUnitConversion()
            getString(R.string.binary_calculator) -> launchBinaryCalculator()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    @SuppressLint("MissingSuperCall", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        calc = CalculatorImpl(this, applicationContext)
        results = calc.getResults()
        equations = calc.getHistoryEntries()
        val fileManager = FileHandler(this)
        if (isExternalStorageWritable()) {
            //Gets the SD cards absolute Path
            val dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).absolutePath
            val fileName = "Export.csv"
            //Creates the path name
            val path = dir + File.separator + fileName
            //Creates the file with the fileManager Object
            exportFile = fileManager.chooseFileType(HISTORY_FILE, path)
        }
        val equationsText = findViewById<TableLayout>(R.id.table_equations)
        val resultsText = findViewById<TableLayout>(R.id.table_results)

        val scrollEquations = ScrollView(this)
        val scrollResults = ScrollView(this)

        resultsText.addView(scrollResults)
        equationsText.addView(scrollEquations)

        results.forEach {
            val tbrow = TableRow(this)
            val textViewRes = TextView(this)

            //Text View
            textViewRes.text = it
            textViewRes.gravity = R.id.center
            textViewRes.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewRes.setTextColor(getColor(R.color.white))
            textViewRes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
            //Table row
            tbrow.addView(textViewRes)
            resultsText.addView(tbrow)
        }

        equations.forEach {
            val textViewEq = TextView(this)
            val tbrow = TableRow(this)
            textViewEq.text = it
            textViewEq.gravity = R.id.center
            textViewEq.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewEq.setTextColor(getColor(R.color.white))
            textViewEq.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
            tbrow.addView(textViewEq)
            table_equations.addView(tbrow)
        }

        result_mean.text = getMean(results)
        result_median.text = getMedian(results)
        result_mode.text = getMode(results)
        result_range.text = getRange(results)

        result_mean.movementMethod = ScrollingMovementMethod()
        result_median.movementMethod = ScrollingMovementMethod()
        result_mode.movementMethod = ScrollingMovementMethod()
        result_range.movementMethod = ScrollingMovementMethod()
    }

    fun exportData() {
        //Create CSV Writer
        val writerCSV : CSVWriter
        // File exist and it is not a directory
        //exportFile.createNewFile()
        when (exportFile.exists() && !exportFile.isDirectory) {
            true -> writerCSV = CSVWriter(FileWriter(exportFile, false))
            false -> { exportFile.createNewFile(); writerCSV = CSVWriter(FileWriter(exportFile, false)) }
        }
        export = ExportManager()
        export.writeLine(writerCSV, equations)
        export.writeLine(writerCSV, results)
        writerCSV.flush()
        writerCSV.close()
    }

    private fun launchUnitConversion(){
        startActivity(Intent(applicationContext, UnitConversionActivity::class.java))
    }
    private fun launchBinaryCalculator(){
        startActivity(Intent(applicationContext, BinaryCalculatorActivity::class.java))
    }

    private fun launchSettings() {
        startActivity(Intent(applicationContext, SettingsActivity::class.java))
    }

    private fun launchAbout() {
        startAboutActivity(R.string.app_name, LICENSE_KOTLIN or LICENSE_AUTOFITTEXTVIEW or LICENSE_ROBOLECTRIC or LICENSE_ESPRESSO, BuildConfig.VERSION_NAME)
    }

    @SuppressLint("MissingSuperCall")
    override fun onResume() {
        super.onResume()
    }

    override fun setValue(value: String, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setValueDouble(d: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFormula(value: String, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFormula(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}
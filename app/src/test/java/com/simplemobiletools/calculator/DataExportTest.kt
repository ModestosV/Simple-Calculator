package com.simplemobiletools.calculator

import android.content.Context
import com.simplemobiletools.calculator.activities.HistoryActivity
import com.simplemobiletools.calculator.activities.MainActivity
import com.simplemobiletools.calculator.activities.UnitConversionActivity
import com.simplemobiletools.calculator.conversions.TemperatureConversion
import com.simplemobiletools.calculator.helpers.Calculator
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [21])
class DataExportTest {

    @Test
    fun exportTest() {
        var mockHistory = Mockito.mock(HistoryActivity::class.java)
        mockHistory.exportData()
        verify(mockHistory).exportData()
    }
}
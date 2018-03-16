package com.simplemobiletools.calculator.helpers
import android.content.Context
import android.widget.Toast
import com.simplemobiletools.calculator.R
import com.simplemobiletools.calculator.helpers.CONSTANT.ABSOLUTE_VALUE
import com.simplemobiletools.calculator.helpers.CONSTANT.ARCCOS
import com.simplemobiletools.calculator.helpers.CONSTANT.ARCSINE
import com.simplemobiletools.calculator.helpers.CONSTANT.ARCTANGENT
import com.simplemobiletools.calculator.helpers.CONSTANT.CEILING
import com.simplemobiletools.calculator.helpers.CONSTANT.COSINE
import com.simplemobiletools.calculator.helpers.CONSTANT.CUBE
import com.simplemobiletools.calculator.helpers.CONSTANT.DIGIT
import com.simplemobiletools.calculator.helpers.CONSTANT.DIVIDE
import com.simplemobiletools.calculator.helpers.CONSTANT.E
import com.simplemobiletools.calculator.helpers.CONSTANT.EQUALS
import com.simplemobiletools.calculator.helpers.CONSTANT.ERROR_READ_VALUE
import com.simplemobiletools.calculator.helpers.CONSTANT.ERROR_SAVE_VALUE
import com.simplemobiletools.calculator.helpers.CONSTANT.FILE
import com.simplemobiletools.calculator.helpers.CONSTANT.FLOOR
import com.simplemobiletools.calculator.helpers.CONSTANT.LEFT_BRACKET
import com.simplemobiletools.calculator.helpers.CONSTANT.LOGARITHM
import com.simplemobiletools.calculator.helpers.CONSTANT.MEMORY_ONE
import com.simplemobiletools.calculator.helpers.CONSTANT.MEMORY_THREE
import com.simplemobiletools.calculator.helpers.CONSTANT.MEMORY_TWO
import com.simplemobiletools.calculator.helpers.CONSTANT.MINUS
import com.simplemobiletools.calculator.helpers.CONSTANT.MODULO
import com.simplemobiletools.calculator.helpers.CONSTANT.MULTIPLY
import com.simplemobiletools.calculator.helpers.CONSTANT.NATURAL_LOGARITHM
import com.simplemobiletools.calculator.helpers.CONSTANT.PI
import com.simplemobiletools.calculator.helpers.CONSTANT.PLUS
import com.simplemobiletools.calculator.helpers.CONSTANT.POWER
import com.simplemobiletools.calculator.helpers.CONSTANT.RANDOM
import com.simplemobiletools.calculator.helpers.CONSTANT.RIGHT_BRACKET
import com.simplemobiletools.calculator.helpers.CONSTANT.ROOT
import com.simplemobiletools.calculator.helpers.CONSTANT.ROUNDING
import com.simplemobiletools.calculator.helpers.CONSTANT.SINE
import com.simplemobiletools.calculator.helpers.CONSTANT.SQUARE
import com.simplemobiletools.calculator.helpers.CONSTANT.TANGENT
import com.simplemobiletools.calculator.helpers.CONSTANT.TEMP_FILE
import com.simplemobiletools.calculator.javaluator.ExtendedDoubleEvaluator
import java.io.*

class CalculatorImpl(calculator: Calculator, private val context: Context) {
    var displayedFormula: String
    var displayedNumber: String
    var lastKey: String
    private var canUseDecimal: Boolean
    private var mCallback: Calculator? = calculator
    private var mSavedValue1: File
    private var mSavedValue2: File
    private var mSavedValue3: File
    private var mEquationHistory: File
    private var mResultHistory: File
    private var fileManager: FileHandler = FileHandler(context)

    //If any listOfSpecialLastEntries precedes listOfSpecialOperations, automatically add a * in between them. 4pi = 4*pi.
    //See implementation in fun handleOperation(operation: String)
    private val listOfSpecialLastEntries = listOf(DIGIT, PI, E, RIGHT_BRACKET)
    private val listOfSpecialOperations = listOf(LEFT_BRACKET, PI, E, SINE, COSINE,  TANGENT,
                                                    LOGARITHM, NATURAL_LOGARITHM, ROOT)

    //Every time a digit or operation is entered, we keep track of the length. In this way, when we
    //delete digits or operations, our program will automatically delete the appropriate amount of
    //characters in the formula string. Example: sin(90) would delete in the following order: ) ->
    //0 -> 9 -> sin( ... This prevents user's from deleting an operation one letter at a time.
    private val listOfInputLengths = mutableListOf<Int>()

    init {
        displayedFormula = ""
        displayedNumber = ""
        lastKey = ""
        canUseDecimal = true
        mSavedValue1 = fileManager.chooseFileType(TEMP_FILE, "one")
        mSavedValue2 = fileManager.chooseFileType(TEMP_FILE, "two")
        mSavedValue3 = fileManager.chooseFileType(TEMP_FILE, "three")
        mEquationHistory = fileManager.chooseFileType(FILE, "History")
        mResultHistory = fileManager.chooseFileType(FILE, "Results")
    }

    fun setValue(value: String) {
        //Big Text ANSWER
        mCallback!!.setValue(value, context)
        displayedNumber = value
    }

    private fun setFormula(value: String) { //Small text OPERATIONS
        mCallback!!.setFormula(value, context)
        displayedFormula = value
    }

    private fun addDigit(number: Int) {
        setFormula(number.toString())
    }

    private fun updateResult(value: Double) {
        setValue(Formatter.doubleToString(value))
    }

    private fun calculateResult(str: String) {
        val evaluator = ExtendedDoubleEvaluator()
        try {
            val result = evaluator.evaluate(str)
            updateResult(result)
            storeResult(result.toString())
        } catch (e: IllegalArgumentException) {
            throw e
        }
    }

    fun handleOperation(operation : String) {
        //if the last character of our formula is a digit and an operation is called from the list,
        //then add a multiplication before the operation
        if(displayedFormula.isNotEmpty()){
            if(listOfSpecialOperations.contains(operation) && listOfSpecialLastEntries.contains(lastKey)) {
                setFormula("*")
                listOfInputLengths.add(1)
            }
        }
        listOfInputLengths.add(getSign(operation).length)
        setFormula(getSign(operation))
        canUseDecimal = true
        lastKey = operation
    }

    fun handleStore(value : String, id: String) {
        if (lastKey == EQUALS && displayedNumber != "") {
            when (id) {
                //SetFormula: small text, SetValue BIG TEXT
                MEMORY_ONE -> { mSavedValue1.writeText(value); setFormula(""); setValue(value) }
                MEMORY_TWO -> { mSavedValue2.writeText(value); setFormula(""); setValue(value)}
                MEMORY_THREE -> { mSavedValue3.writeText(value); setFormula(""); setValue(value) }
            }
        }
        else {
            val message = Toast.makeText(context, ERROR_SAVE_VALUE, Toast.LENGTH_SHORT)
            message.show()
        }
    }

    fun handleViewValue(id: String) {
        val message: Toast?
        val variable: String?

        when (id) {
            MEMORY_ONE -> { variable = mSavedValue1.readText()
                if(variable == "") {
                    message = Toast.makeText(context, ERROR_READ_VALUE, Toast.LENGTH_SHORT)
                    message.show()
                }
                else {
                    setFormula(variable)
                }
            }
            MEMORY_TWO -> { variable = mSavedValue2.readText()
                if(variable == "") {
                    message = Toast.makeText(context, ERROR_READ_VALUE, Toast.LENGTH_SHORT)
                    message.show()
                }
                else {
                    setFormula(variable)
                }
            }
            MEMORY_THREE -> { variable = mSavedValue3.readText()
                if(variable == "") {
                    message = Toast.makeText(context, ERROR_READ_VALUE, Toast.LENGTH_SHORT)
                    message.show()
                }
                else {
                    setFormula(variable)
                }
            }
        }
    }

    fun handleClear(formula : String) {
        val newValue: String
        if(formula.isNotEmpty())
        {
            val removeThisManyCharacters = listOfInputLengths[listOfInputLengths.size - 1]
            newValue = formula.substring(0, (formula.length - removeThisManyCharacters))
            listOfInputLengths.removeAt(listOfInputLengths.size - 1)
            setFormula("")
            setFormula(newValue)
            setValue("")
        }
    }

    fun handleReset() {
        lastKey = ""
        canUseDecimal = true
        setValue("")
        setFormula("")
    }

    fun handleEquals(str: String) {
        calculateResult(str)
        storeHistory(str)
        lastKey = EQUALS
    }

    //TODO: Finish history method that stores the information with the fie explorer
    private fun storeHistory(equation: String) {
        val write: Writer = BufferedWriter(FileWriter(mEquationHistory, true))
        write.write(equation)
        write.appendln()
        write.flush()
        write.close()
    }

    //TODO: Finish the results history section
    private fun storeResult(result: String) {
        val writer: Writer = BufferedWriter(FileWriter(mResultHistory, true))
        writer.write(result)
        writer.appendln()
        writer.flush()
        writer.close()
    }

    fun getHistoryEntries(): ArrayList<String> {
        val reader: Reader = BufferedReader(FileReader(mEquationHistory))
        val list: ArrayList<String> = ArrayList()
        reader.forEachLine {
            list.add(it)
        }
        reader.close()
        return list
    }

    fun getResults(): ArrayList<String> {
        val reader: Reader = BufferedReader(FileReader(mResultHistory))
        val list: ArrayList<String> = ArrayList()
        reader.forEachLine {
            list.add(it)
        }
        reader.close()
        return list
    }

    private fun decimalClicked() {
        if(canUseDecimal)
            setFormula(".")
            canUseDecimal = false
    }


    private fun getSign(lastOperation: String?) = when (lastOperation) {
        PLUS -> "+"
        MINUS -> "-"
        MULTIPLY -> "*"
        DIVIDE -> "/"
        MODULO -> "%"
        POWER -> "^"
        ROOT -> "sqrt("
        LEFT_BRACKET -> "("
        RIGHT_BRACKET -> ")"
        PI -> "pi"
        E -> "e"
        SINE -> "sin("
        COSINE -> "cos("
        TANGENT -> "tan("
        LOGARITHM -> "log("
        NATURAL_LOGARITHM -> "ln("
        ARCSINE -> "asin("
        ARCCOS -> "acos("
        ARCTANGENT -> "atan("
        ROUNDING -> "round("
        CEILING -> "ceil("
        SQUARE -> "^(2)"
        CUBE -> "^(3)"
        ABSOLUTE_VALUE -> "abs("
        FLOOR -> "floor("


        else -> ""
    }

    fun numpadClicked(id: Int) {
        listOfInputLengths.add(1)
        lastKey = DIGIT
        when (id) {
            R.id.btn_decimal -> decimalClicked()
            R.id.btn_0 -> addDigit(0)
            R.id.btn_1 -> addDigit(1)
            R.id.btn_2 -> addDigit(2)
            R.id.btn_3 -> addDigit(3)
            R.id.btn_4 -> addDigit(4)
            R.id.btn_5 -> addDigit(5)
            R.id.btn_6 -> addDigit(6)
            R.id.btn_7 -> addDigit(7)
            R.id.btn_8 -> addDigit(8)
            R.id.btn_9 -> addDigit(9)
        }
    }

    fun setHistoryFile(file : File) {
        mEquationHistory = file
    }

    fun setResultFile(file : File) {
        mResultHistory = file
    }

    fun getFileManager() : FileHandler {
        return fileManager
    }

    fun reciprocalOfResult(){
        if(displayedNumber.isNotEmpty()) {
            val resultWithoutCommas = displayedNumber.replace(",", "")
            calculateResult("1/$resultWithoutCommas")
        }
    }

    fun randomNumberBetweenZeroAndOne(){
        handleReset()
        calculateResult("random")
    }

    fun negationOfResult(){
        if(displayedNumber.isNotEmpty()) {
            val resultWithoutCommas = displayedNumber.replace(",", "")
            calculateResult("1/$resultWithoutCommas")
        }
    }

}
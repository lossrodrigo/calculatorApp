package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //button number action
    fun numberAction(view: View) {

        if (view is Button){

            // add a decimal number
            if (view.text == "."){
                if (canAddDecimal)
                    inputTV.append(view.text)//add to inputTV the "." decimal
                canAddDecimal = false
            }
            else
                inputTV.append(view.text) //add to inputTV the text from which button is clicked ex: button 9
                canAddOperation = true
        }
    }

    //button operator action
    fun operationAction(view: View) {
        if (view is Button && canAddOperation){
            inputTV.append(view.text) //add to inputTV the text from which button is clicked ex: button +
            canAddOperation = false
            canAddDecimal = true
        }
    }

    //button clear action
    fun allClearAction(view: View) {
        inputTV.text = ""
        resultsTV.text = ""
    }

    //button backspace action
    fun backSpaceAction(view: View) {
        val  length = inputTV.length()
        if (length > 0 ){
            inputTV.text = inputTV.text.subSequence(0, length-1)
        }
    }

    //button equals action
    fun equalsAction(view: View) {
        resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitOperators = digitsOperators()
        if (digitOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)

        return result.toString()
    }

    //fun that calculate + an -
    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex ){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float

                when(operator){
                    '+' -> result += nextDigit
                    '-' -> result -= nextDigit
                }
            }
        }

        return result
    }

    //fun that calculate * and / first
    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList

        while (list.contains('x') || list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){

            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex ){
                val operator =  passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            //just for calculate separately each times and division operation. ex: input 9*3/7
            // fun timesDivisionCalculate will reapet while existing * and / operators,
            //and solve them one by one, like in this example first 9*3 = 27 and then 27/7
            if (i > restartIndex) {
                newList.add(passedList[i])
            }
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in inputTV.text){

            //this "if" add a integer or decimal number to var currentDigit
            if(character.isDigit() || character == '.' ){
                currentDigit += character
            }
            //else add others characters like +, -, * and /
            else {
                list.add(currentDigit.toFloat()) //Firstly add currentDigit(number) before operation character
                currentDigit = "" //cleaning currentDigit
                list.add(character) //add operation character
            }
        }

        // if the last character of inputTv was a number(currentDigit in the last indice != ""), this "if" add this to the val list
        if (currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return list
    }
}
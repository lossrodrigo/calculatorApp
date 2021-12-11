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

    fun numberAction(view: View) {

        if (view is Button){

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

    fun operationAction(view: View) {
        if (view is Button && canAddOperation){
            inputTV.append(view.text) //add to input text the text from which button is clicked ex: button +
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        inputTV.text = ""
        resultsTV.text = ""
    }

    fun backSpaceAction(view: View) {
        val  lenght = inputTV.length()
        if (lenght > 0 ){
            inputTV.text = inputTV.text.subSequence(0, lenght-1)
        }
    }

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
            if(character.isDigit() || character == '.' ){
                currentDigit += character
            }
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return list
    }
}
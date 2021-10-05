package com.example.jsonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var dateText: TextView
    private lateinit var orderText: TextView
    private lateinit var inputNumber: EditText
    private lateinit var listSpinner: Spinner
    private lateinit var convert: Button
    private lateinit var result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="Convert Currency"
        dateText = findViewById(R.id.tvDate)
        inputNumber = findViewById(R.id.etEntry)
        orderText = findViewById(R.id.tvOrder)
        convert = findViewById(R.id.buttonConvert)
        listSpinner = findViewById(R.id.convertSpinner)
        result = findViewById(R.id.tvResult)
        val listOfCoverts = arrayListOf<String>()
        val listOfCovertsNO = arrayListOf<Double>()
        var date = ""
        var selected: Int = 0
        var mode = 0

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<BookDetails?>? = apiInterface!!.doGetListResources()
        convert.setOnClickListener {
            dateText.text = "Date: $date"
            var number: Double = 0.0
            if (inputNumber.text.isNotBlank())
                number = inputNumber.text.toString().toDouble()
            var s = 0.0
            if (mode == 0) {
                s = listOfCovertsNO[selected] * number
                result.text = "result $s ${listOfCoverts[selected]}"
            } else {
                s = number / listOfCovertsNO[selected]
                result.text = "result $s Euro"
            }
        }
        call?.enqueue(object : Callback<BookDetails?> {
            override fun onResponse(
                call: Call<BookDetails?>?,
                response: Response<BookDetails?>
            ) {
                val resource: BookDetails? = response.body()
                date = resource?.date!!

                val datumList = resource.eur

                if (datumList != null) {
                    val datum = datumList.keySet()
//list in arrayAdapter
                    if (listSpinner != null) {
                        val adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item, datum.toTypedArray()
                        )
                        listSpinner.adapter = adapter

                        listSpinner.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {

                                selected = position
                                if (mode == 1) {
                                    orderText.text = "Enter ${listOfCoverts[selected]} Value"
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                Log.d("TAG", "Nothing Selected")
                            }
                        }
                    }
                    for (d in datum) {
                        listOfCovertsNO.add(datumList.get(d).toString().toDouble())
                        listOfCoverts.add(d)
                    }
                }
            }

            override fun onFailure(call: Call<BookDetails?>, t: Throwable?) {
                call.cancel()
            }
        })


    }

    fun convertCurrency() {

    }
}
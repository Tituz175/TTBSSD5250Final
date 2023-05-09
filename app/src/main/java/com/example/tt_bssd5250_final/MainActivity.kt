package com.example.tt_bssd5250_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    var input1: String? = null
    var input2: String? = null
    var input3: String? = null
    var words = mutableListOf<String>()
    var results = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to the UI elements in the layout
        val submit_button = findViewById<Button>(R.id.submit_button)
        val generate_button = findViewById<Button>(R.id.generate_button)
        val input_text1 = findViewById<EditText>(R.id.first_word_input)
        val input_text2 = findViewById<EditText>(R.id.second_word_input)
        val input_text3 = findViewById<EditText>(R.id.third_word_input)

        // Set a listener for the submit button
        submit_button.setOnClickListener {
            // Get the input from the text fields and add them to the words list
            input1 = input_text1.text.toString()
            words.add(input1.toString())
            input2 = input_text2.text.toString()
            words.add(input2.toString())
            input3 = input_text3.text.toString()
            words.add(input3.toString())
        }

        // Set a listener for the generate button
        generate_button.setOnClickListener {
            // Use coroutine to run the network operation on a background thread
            CoroutineScope(Dispatchers.IO).launch {
                // Loop through each word in the words list
                for (word in words) {
                    // Create a URL object with the rhymebrain API endpoint and the current word as a parameter
                    val url = URL("https://rhymebrain.com/talk?function=getRhymes&word=$word")

                    // Open a connection to the URL
                    with(url.openConnection() as HttpURLConnection) {
                        // Set the request method to GET
                        requestMethod = "GET"

                        // Create a BufferedReader object to read the response from the API
                        BufferedReader(InputStreamReader(inputStream)).use {
                            // Create a StringBuffer object to hold the response
                            val response = StringBuffer()

                            // Read each line of the response and append it to the response StringBuffer
                            var inputLine = it.readLine()
                            while (inputLine != null) {
                                response.append(inputLine)
                                inputLine = it.readLine()
                            }

                            // Add the response to the results list
                            results.add(response.toString())
                        }
                    }
                }

                // Switch back to the main thread to update the UI with the results
                withContext(Dispatchers.Main) {
                    Log.d("res", "$results")
                }
            }
        }
    }
}

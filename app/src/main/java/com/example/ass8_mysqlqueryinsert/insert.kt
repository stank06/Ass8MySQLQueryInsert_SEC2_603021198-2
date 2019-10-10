package com.example.ass8_mysqlqueryinsert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_insert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class insert : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
    }

    fun add(v: View){

        var radioGroup: RadioGroup = findViewById(R.id.radiogroup)
        var selectedID : Int = radioGroup.checkedRadioButtonId;
        var radioButton: RadioButton = findViewById(selectedID)


        val api : EmployeeAPI = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").addConverterFactory(
            GsonConverterFactory.create())
            .build().create(EmployeeAPI ::class.java)

        api.insertEmp(
            edt_name.text.toString(),
            radioButton.text.toString(),
            edt_mail.text.toString(),
            edt_sal.text.toString().toInt()).enqueue(object : Callback<Employee>{
            override fun onFailure(call: Call<Employee>, t: Throwable) {
                Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
                if(response.isSuccessful()){
                    Toast.makeText(applicationContext, "Successfully Inserted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,"Error", Toast.LENGTH_SHORT).show()
                }
            }

        })




    }
    fun cancel(v: View){
        finish()

    }
}

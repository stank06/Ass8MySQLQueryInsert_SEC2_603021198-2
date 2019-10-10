package com.example.ass8_mysqlqueryinsert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var employeeList = arrayListOf<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(
            DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL)
        )
    }
    override fun onResume() {
        super.onResume()
        callEmployee()

    }

    fun addEmployee(v: View) {
        val intent = Intent(this@MainActivity,insert::class.java)
        startActivity(intent)
    }

    fun callEmployee(){
        employeeList.clear()

        var serv : EmployeeAPI = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(EmployeeAPI ::class.java)

        serv.retrieveStudent().enqueue(object : Callback<List<Employee>> {


            override fun onResponse(call: Call<List<Employee>>, response: Response<List<Employee>>) {
                response.body()?.forEach(){
                    employeeList.add(Employee(it.emp_name, it.emp_gender, it.emp_email, it.emp_salary))
                    recycler_view.adapter = EmployeeAdapter(employeeList, applicationContext)              }
            }
            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {

            }

        })



    }
}
interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}
fun RecyclerView.addOnItemTouchListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener{
        override fun onChildViewDetachedFromWindow(view: View) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view?.setOnClickListener{
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    })
}


package com.example.studymentor.UI.Student

import ScheduleAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymentor.R
import com.example.studymentor.StudentCalendarActivity
import com.example.studymentor.apiservice.RetrofitClient
import com.example.studymentor.model.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorSchedulesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutor_schedules)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btHome = findViewById<ImageButton>(R.id.btHome)
        val btTutorList = findViewById<ImageButton>(R.id.btTutors)
        val btPerfil = findViewById<ImageButton>(R.id.btPerfilEstudiante)
        val btCalendar = findViewById<ImageButton>(R.id.btCalendar)
        recyclerView = findViewById(R.id.rv_schedules)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btHome.setOnClickListener {
            val intent = Intent(this@TutorSchedulesActivity, HomeStudentActivity::class.java)
            startActivity(intent)
        }

        btTutorList.setOnClickListener {
            val intent = Intent(this@TutorSchedulesActivity, TutorListActivity::class.java)
            startActivity(intent)
        }

        btPerfil.setOnClickListener{
            val intent = Intent(this@TutorSchedulesActivity, StudentProfileActivity::class.java)
            startActivity(intent)
        }

        btCalendar.setOnClickListener{
            val intent = Intent(this@TutorSchedulesActivity, StudentCalendarActivity::class.java)
            startActivity(intent)
        }

        fetchSchedules()
    }

    private fun fetchSchedules() {
        RetrofitClient.scheduleService.getSchedules().enqueue(object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    scheduleAdapter = ScheduleAdapter(schedules)
                    recyclerView.adapter = scheduleAdapter
                } else {
                    Log.e("TutorSchedule", "Failed to get schedules: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                Log.e("TutorSchedule", "Error fetching schedules", t)
            }
        })
    }
}
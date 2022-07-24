package com.singularitycoder.tenthousandhours

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.singularitycoder.tenthousandhours.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: SkillDao

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setupUI()
    }

    private fun ActivityMainBinding.setupUI() {
        rvSkills.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = SkillAdapter()
        }
        (rvSkills.adapter as SkillAdapter).setPlusOneClickListener { it: Skill ->
            println("hours: ${it.hours}")
            CoroutineScope(IO).launch { dao.update(it) }
        }
        ibAddSkill.setOnClickListener {
            cardAddSkill.performClick()
        }
        cardAddSkill.setOnClickListener {
            if (etSkill.text.isNullOrBlank()) return@setOnClickListener
            CoroutineScope(IO).launch {
                dao.insert(
                    Skill(
                        name = etSkill.text.toString(),
                        hours = 0,
                        dateAdded = System.currentTimeMillis(),
                        level = SkillLevel.BEGINNER.value
                    )
                )
                withContext(Main) {
                    etSkill.setText("")
                }
            }
        }
        dao.getAllSkillListLiveData().observe(this@MainActivity) { it: List<Skill>? ->
            (rvSkills.adapter as SkillAdapter).apply {
                skillList = it ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}
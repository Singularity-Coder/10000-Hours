package com.singularitycoder.tenthousandhours

import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        binding.setupUserActionListeners()
        binding.observeForData()
    }

    private fun ActivityMainBinding.setupUI() {
        rvSkills.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = SkillAdapter()
        }
    }

    private fun ActivityMainBinding.setupUserActionListeners() {
        (rvSkills.adapter as SkillAdapter).setPlusOneClickListener { it: Skill ->
            println("hours: ${it.hours}")
            CoroutineScope(IO).launch { dao.update(it) }
        }
        (rvSkills.adapter as SkillAdapter).setItemLongClickListener { it: Skill ->
//            val skillLongPressOptionsList = listOf(
//                BottomSheetMenu(1, "Update skill", R.drawable.round_update_24),
//                BottomSheetMenu(2, "Delete skill", R.drawable.round_remove_circle_outline_24),
//            )
//            MenuBottomSheetFragment.newInstance(skillLongPressOptionsList).show(supportFragmentManager, TAG_MENU_MODAL_BOTTOM_SHEET)
            MaterialAlertDialogBuilder(this@MainActivity, com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Dialog).apply {
                setCancelable(false)
                setTitle("Delete this skill?")
                setMessage("Careful. You cannot undo this action!")
                background = ContextCompat.getDrawable(this@MainActivity, R.drawable.alert_dialog_bg)
                setNegativeButton("No") { dialog, which -> }
                setPositiveButton("Yes") { dialog, which ->
                    CoroutineScope(IO).launch { dao.delete(it) }
                }
                show()
            }
        }
        (rvSkills.adapter as SkillAdapter).setItemClickListener { it: EditText ->
            it.showKeyboard()
        }
        (rvSkills.adapter as SkillAdapter).setDismissKeyboardClickListener { it: EditText ->
            it.hideKeyboard()
        }
        (rvSkills.adapter as SkillAdapter).setApproveUpdateClickListener { it: Skill ->
            CoroutineScope(IO).launch { dao.update(it) }
        }
        (rvSkills.adapter as SkillAdapter).setCancelUpdateClickListener { it: Skill ->
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
                        dateLastAttempted = System.currentTimeMillis(),
                        level = SkillLevel.BEGINNER.value
                    )
                )
                withContext(Main) {
                    etSkill.setText("")
                }
            }
        }
    }

    private fun ActivityMainBinding.observeForData() {
        dao.getAllSkillListLiveData().observe(this@MainActivity) { it: List<Skill>? ->
            (rvSkills.adapter as SkillAdapter).apply {
                skillList = it ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}

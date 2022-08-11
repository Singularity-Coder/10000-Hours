package com.singularitycoder.tenthousandhours

import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
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

    private val skillAdapter = SkillAdapter()
    private val duplicateSkillList = mutableListOf<Skill>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setupUI()
        binding.setupUserActionListeners()
        observeForData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.cardAddSkillParent.isVisible = binding.root.isKeyboardVisible.not()
    }

    private fun ActivityMainBinding.setupUI() {
        rvSkills.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = skillAdapter
        }
    }

    private fun ActivityMainBinding.setupUserActionListeners() {
        etSearch.doAfterTextChanged { keyWord: Editable? ->
            ibClearSearch.isVisible = keyWord.isNullOrBlank().not()
            if (keyWord.isNullOrBlank()) {
                skillAdapter.skillList = duplicateSkillList
            } else {
                skillAdapter.skillList = skillAdapter.skillList.filter { it: Skill -> it.name.contains(keyWord) }
            }
            skillAdapter.notifyDataSetChanged()
        }
        ibClearSearch.setOnClickListener {
            etSearch.setText("")
        }
        skillAdapter.setPlusOneClickListener { it: Skill ->
            println("hours: ${it.hours}")
            CoroutineScope(IO).launch { dao.update(it) }
        }
        skillAdapter.setItemLongClickListener { it: Skill ->
//            val skillLongPressOptionsList = listOf(
//                BottomSheetMenu(1, "Update skill", R.drawable.round_update_24),
//                BottomSheetMenu(2, "Delete skill", R.drawable.round_remove_circle_outline_24),
//            )
//            MenuBottomSheetFragment.newInstance(skillLongPressOptionsList).show(supportFragmentManager, TAG_MENU_MODAL_BOTTOM_SHEET)
            MaterialAlertDialogBuilder(this@MainActivity, com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Dialog).apply {
                setCancelable(false)
                setTitle("Delete this skill?")
                setMessage("Careful! You cannot undo this action.")
                background = ContextCompat.getDrawable(this@MainActivity, R.drawable.alert_dialog_bg)
                setNegativeButton("No") { dialog, which -> }
                setPositiveButton("Yes") { dialog, which ->
                    CoroutineScope(IO).launch { dao.delete(it) }
                }
                show()
            }
        }
        skillAdapter.setItemClickListener { it: EditText ->
            cardAddSkillParent.isVisible = false
            it.showKeyboard()
        }
        skillAdapter.setDismissKeyboardClickListener { it: EditText ->
            cardAddSkillParent.isVisible = true
            it.hideKeyboard()
        }
        skillAdapter.setApproveUpdateClickListener { it: Skill ->
            CoroutineScope(IO).launch { dao.update(it) }
        }
        skillAdapter.setCancelUpdateClickListener { it: Skill ->
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

    private fun observeForData() {
        dao.getAllSkillListLiveData().observe(this@MainActivity) { it: List<Skill>? ->
            skillAdapter.apply {
                skillList = it ?: emptyList()
                duplicateSkillList.clear()
                duplicateSkillList.addAll(skillList)
                notifyDataSetChanged()
            }
        }
    }
}

package com.singularitycoder.tenthousandhours

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.singularitycoder.tenthousandhours.databinding.ActivityMainBinding
import com.singularitycoder.tenthousandhours.databinding.FragmentSkillsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SkillsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(skillLevel: String) = SkillsFragment().apply {
            arguments = Bundle().apply { putString(ARG_PARAM_SKILL_LEVEL, skillLevel) }
        }
    }

    @Inject
    lateinit var dao: SkillDao

    private lateinit var binding: FragmentSkillsBinding

    private val skillAdapter = SkillAdapter()
    private val duplicateSkillList = mutableListOf<Skill>()

    private var skillLevelParam: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skillLevelParam = arguments?.getString(ARG_PARAM_SKILL_LEVEL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSkillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupUI()
        binding.setupUserActionListeners()
        observeForData()
    }

    private fun FragmentSkillsBinding.setupUI() {
        rvSkills.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = skillAdapter
        }
    }

    private fun FragmentSkillsBinding.setupUserActionListeners() {
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
            CoroutineScope(Dispatchers.IO).launch {
                dao.update(it.apply {
                    level = when {
                        hours in (10000 / 3)..(10000 / 2) -> SkillLevel.BEGINNER.value
                        hours in (10000 / 2)..10000 -> SkillLevel.PROFESSIONAL.value
                        hours > 10000 -> SkillLevel.EXPERT.value
                        else -> ""
                    }
                })
            }
        }
        skillAdapter.setItemLongClickListener { it: Skill ->
//            val skillLongPressOptionsList = listOf(
//                BottomSheetMenu(1, "Update skill", R.drawable.round_update_24),
//                BottomSheetMenu(2, "Delete skill", R.drawable.round_remove_circle_outline_24),
//            )
//            MenuBottomSheetFragment.newInstance(skillLongPressOptionsList).show(supportFragmentManager, TAG_MENU_MODAL_BOTTOM_SHEET)
            MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Dialog).apply {
                setCancelable(true)
                setTitle("Delete this skill?")
                setMessage(it.name)
                background = ContextCompat.getDrawable(requireContext(), R.drawable.alert_dialog_bg)
                setNegativeButton("No") { dialog, which -> }
                setPositiveButton("Yes") { dialog, which ->
                    CoroutineScope(Dispatchers.IO).launch { dao.delete(it) }
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
            CoroutineScope(Dispatchers.IO).launch { dao.update(it) }
        }
        skillAdapter.setCancelUpdateClickListener { it: Skill ->
        }
        ibAddSkill.setOnClickListener {
            cardAddSkill.performClick()
        }
        cardAddSkill.setOnClickListener {
            if (etSkill.text.isNullOrBlank()) return@setOnClickListener
            CoroutineScope(Dispatchers.IO).launch {
                dao.insert(
                    Skill(
                        name = etSkill.text.toString(),
                        hours = 0,
                        dateAdded = System.currentTimeMillis(),
                        dateLastAttempted = System.currentTimeMillis(),
                        level = SkillLevel.BEGINNER.value
                    )
                )
                withContext(Dispatchers.Main) {
                    etSkill.setText("")
                }
            }
        }
    }

    private fun observeForData() {
        dao.getAllSkillListLiveData().observe(viewLifecycleOwner) { it: List<Skill>? ->
            skillAdapter.apply {
                skillList = it?.filter { it: Skill -> it.level == skillLevelParam } ?: emptyList()
                duplicateSkillList.clear()
                duplicateSkillList.addAll(skillList)
                notifyDataSetChanged()
            }
        }
    }
}

const val ARG_PARAM_SKILL_LEVEL = "ARG_PARAM_SKILL_LEVEL"
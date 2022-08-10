package com.singularitycoder.tenthousandhours

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.singularitycoder.tenthousandhours.databinding.ListItemSkillBinding

class SkillAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var skillList = emptyList<Skill>()
    private var plusOneClickListener: (skill: Skill) -> Unit = {}
    private var itemLongClickListener: (skill: Skill) -> Unit = {}
    private var itemClickListener: (editText: EditText) -> Unit = {}
    private var dismissKeyboardClickListener: (editText: EditText) -> Unit = {}
    private var approveUpdateClickListener: (skill: Skill) -> Unit = {}
    private var cancelUpdateClickListener: (skill: Skill) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ListItemSkillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SkillViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SkillViewHolder).setData(skillList[position])
    }

    override fun getItemCount(): Int = skillList.size

    override fun getItemViewType(position: Int): Int = position

    fun setPlusOneClickListener(listener: (skill: Skill) -> Unit) {
        plusOneClickListener = listener
    }

    fun setItemLongClickListener(listener: (skill: Skill) -> Unit) {
        itemLongClickListener = listener
    }

    fun setItemClickListener(listener: (editText: EditText) -> Unit) {
        itemClickListener = listener
    }

    fun setDismissKeyboardClickListener(listener: (editText: EditText) -> Unit) {
        dismissKeyboardClickListener = listener
    }

    fun setApproveUpdateClickListener(listener: (skill: Skill) -> Unit) {
        approveUpdateClickListener = listener
    }

    fun setCancelUpdateClickListener(listener: (skill: Skill) -> Unit) {
        cancelUpdateClickListener = listener
    }

    inner class SkillViewHolder(
        private val itemBinding: ListItemSkillBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(skill: Skill) {
            itemBinding.apply {
                tvSkillName.text = skill.name
                tvLastAttempt.text = (if (skill.hours > 0) "Last attempt: " else "Added on: ") + skill.dateLastAttempted.toIntuitiveDateTime()
                tvHours.text = "${skill.hours} h"

                ibAddSkill.setOnClickListener {
                    cardAddSkill.performClick()
                }
                cardAddSkill.setOnClickListener {
                    plusOneClickListener.invoke(skill.apply {
                        hours = (skill.hours + 1).toShort()
                        dateLastAttempted = timeNow
                    })
                }

                ibApproveUpdate.setOnClickListener {
                    clSkill.isVisible = true
                    cardUpdate.isVisible = false
                    etUpdateSkill.requestFocus()
                    dismissKeyboardClickListener.invoke(etUpdateSkill)
                    approveUpdateClickListener.invoke(skill.apply {
                        name = etUpdateSkill.text.toString()
                    })
                }
                ibCancelUpdate.setOnClickListener {
                    clSkill.isVisible = true
                    cardUpdate.isVisible = false
                    etUpdateSkill.requestFocus()
                    dismissKeyboardClickListener.invoke(etUpdateSkill)
                    cancelUpdateClickListener.invoke(skill)
                }

                root.setOnClickListener {
                    clSkill.isVisible = false
                    cardUpdate.isVisible = true
                    etUpdateSkill.setText(skill.name)
                    etUpdateSkill.requestFocus()
                }
                root.setOnLongClickListener {
                    itemLongClickListener.invoke(skill)
                    false
                }

                etUpdateSkill.setOnFocusChangeListener { view, isFocused ->
                    if (isFocused) itemClickListener.invoke(etUpdateSkill)
                }
            }
        }
    }
}

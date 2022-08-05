package com.singularitycoder.tenthousandhours

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singularitycoder.tenthousandhours.databinding.ListItemSkillBinding

class SkillAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var skillList = emptyList<Skill>()
    private var plusOneClickListener: (skill: Skill) -> Unit = {}

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

    inner class SkillViewHolder(
        private val itemBinding: ListItemSkillBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(skill: Skill) {
            itemBinding.apply {
                tvSkillName.text = skill.name
                tvDate.text = skill.dateAdded.toIntuitiveDateTime()
                ibAddSkill.setOnClickListener {
                    cardAddSkill.performClick()
                }
                tvHours.text = "${skill.hours} h"
                cardAddSkill.setOnClickListener {
                    plusOneClickListener.invoke(skill.apply {
                        hours = (skill.hours + 1).toShort()
                    })
                }
            }
        }
    }
}

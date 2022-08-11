package com.singularitycoder.tenthousandhours

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.singularitycoder.tenthousandhours.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: SkillDao

    val skillLevelTabNamesList = listOf(
        SkillLevel.BEGINNER.value,
        SkillLevel.PROFESSIONAL.value,
        SkillLevel.EXPERT.value
    )

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        binding.viewpagerSkills.adapter = SkillsViewPagerAdapter(fragmentManager = supportFragmentManager, lifecycle = lifecycle)
        TabLayoutMediator(binding.tabLayoutReminders, binding.viewpagerSkills) { tab, position ->
            tab.text = skillLevelTabNamesList[position]
        }.attach()
    }

    inner class SkillsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = skillLevelTabNamesList.size
        override fun createFragment(position: Int): Fragment = SkillsFragment.newInstance(skillLevelTabNamesList[position])
    }
}

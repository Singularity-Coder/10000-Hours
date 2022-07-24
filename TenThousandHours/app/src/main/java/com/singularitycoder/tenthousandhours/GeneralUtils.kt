package com.singularitycoder.tenthousandhours

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

const val TABLE_SKILL = "table_skill"
const val DB_SKILL = "db_skill"

fun View.showSnackBar(
    message: String,
    anchorView: View? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionBtnText: String = "NA",
    action: () -> Unit = {},
) {
    Snackbar.make(this, message, duration).apply {
        this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        if (null != anchorView) this.anchorView = anchorView
        if ("NA" != actionBtnText) setAction(actionBtnText) { action.invoke() }
        this.show()
    }
}

enum class SkillLevel(value: String) {
    BEGINNER(value = "Beginner"),
    PROFESSIONAL(value = "Professional"),
    EXPERT(value = "Expert")
}

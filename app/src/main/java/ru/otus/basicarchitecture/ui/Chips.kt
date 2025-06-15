package ru.otus.basicarchitecture.ui

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import ru.otus.basicarchitecture.R

object InterestChips {

    private fun getCornerRadius(context: Context): Float =
         context.resources.getDimensionPixelSize(R.dimen.chip_corner_radius).toFloat()

    fun load(chipGroup: ChipGroup, tags: Map<String, Boolean>, style: (Chip.() -> Unit) = {
        isClickable = true
    }) {
        val sam = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, getCornerRadius(chipGroup.context))
            .build()

        tags.forEach { tag ->
            val chip = Chip(chipGroup.context).apply {
                style()
                isCheckable = isClickable

                if (isCheckable) {
                    isChecked = tag.value
                    /*setTextColor(
                        resources.getColor(
                            com.google.android.material.R.color.design_default_color_primary_variant
                        )
                    )*/
                }
                shapeAppearanceModel = sam
                text = tag.key
            }

            if (chip.isCheckable) chipGroup.addView(chip)
            else if (tag.value) chipGroup.addView(chip)
        }
    }
}

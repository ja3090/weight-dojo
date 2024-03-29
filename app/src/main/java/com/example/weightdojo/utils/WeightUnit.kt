package com.example.weightdojo.utils

import com.example.weightdojo.AppConfig

class WeightUnit {
    companion object {
        fun convert(
            from: WeightUnits = AppConfig.internalDefaultWeightUnit,
            to: WeightUnits = AppConfig.internalDefaultWeightUnit,
            value: Float
        ): Float {
            return when (from) {
                WeightUnits.KG -> when (to) {
                    WeightUnits.LBS -> value / 0.45359236f
                    WeightUnits.KG -> value
                }

                WeightUnits.LBS -> when (to) {
                    WeightUnits.KG -> value * 0.45359236f
                    WeightUnits.LBS -> value
                }
            }
        }
    }
}

enum class WeightUnits {
    KG, LBS
}
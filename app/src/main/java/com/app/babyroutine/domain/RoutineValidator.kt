package com.app.babyroutine.domain

import com.app.babyroutine.model.Routine

data class ValidationResult(
    val isValid: Boolean,
    val message: String? = null
)

object RoutineValidator {

    fun validateRequiredFields(
        title: String,
        category: String,
        time: String
    ): ValidationResult {
        return when {
            title.trim().isBlank() ->
                ValidationResult(false, "Le nom de la routine est obligatoire.")

            category.trim().isBlank() ->
                ValidationResult(false, "La catégorie est obligatoire.")

            time.trim().isBlank() ->
                ValidationResult(false, "L'heure de la routine est obligatoire.")

            else ->
                ValidationResult(true)
        }
    }

    fun validateTimeFormat(time: String): ValidationResult {
        val cleanedTime = time.trim()
        val regex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")

        return if (regex.matches(cleanedTime)) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Le format de l'heure doit être HH:mm.")
        }
    }

    fun validateLocationData(routine: Routine): ValidationResult {
        val hasLatitude = routine.latitude != null
        val hasLongitude = routine.longitude != null

        return if (hasLatitude.xor(hasLongitude)) {
            ValidationResult(false, "Les coordonnées du lieu sont incomplètes.")
        } else {
            ValidationResult(true)
        }
    }

    fun validateForSave(routine: Routine): ValidationResult {
        val required = validateRequiredFields(
            title = routine.title,
            category = routine.category,
            time = routine.time
        )
        if (!required.isValid) return required

        val timeCheck = validateTimeFormat(routine.time)
        if (!timeCheck.isValid) return timeCheck

        val locationCheck = validateLocationData(routine)
        if (!locationCheck.isValid) return locationCheck

        return ValidationResult(true)
    }
}
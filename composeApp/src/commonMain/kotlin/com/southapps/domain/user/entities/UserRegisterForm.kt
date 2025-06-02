package com.southapps.domain.user.entities

import com.southapps.domain.common.validation.form.FormField

data class UserRegisterForm(
    val name: FormField,
    val email: FormField,
    val phone: FormField,
    val password: FormField,
    val confirmPassword: FormField
)

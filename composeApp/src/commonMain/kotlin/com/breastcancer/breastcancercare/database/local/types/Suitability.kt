package com.breastcancer.breastcancercare.database.local.types

enum class Suitability(val key: String) {
    Early("early_breast_cancer_post_active_treatment"),
    Metastatic("metastatic_breast_cancer"),
    Newly("newly_diagnosed_and_in_treatment")
}
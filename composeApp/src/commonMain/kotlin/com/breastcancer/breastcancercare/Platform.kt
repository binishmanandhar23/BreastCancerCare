package com.breastcancer.breastcancercare

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
package com.breastcancer.breastcancercare.models

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class StateTickWrapper<T>(val data: T, val tick: Long = getTick())

@OptIn(ExperimentalTime::class)
fun getTick() = Clock.System.now().epochSeconds

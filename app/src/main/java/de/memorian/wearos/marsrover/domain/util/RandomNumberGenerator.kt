package de.memorian.wearos.marsrover.domain.util

import javax.inject.Inject
import kotlin.random.Random

class RandomNumberGenerator @Inject constructor() {

    fun generate(until: Int) = Random.nextInt(until)
}
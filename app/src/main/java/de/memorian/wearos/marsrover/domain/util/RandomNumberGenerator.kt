package de.memorian.wearos.marsrover.domain.util

import kotlin.random.Random

class RandomNumberGenerator {

    fun generate(until: Int) = Random.nextInt(until)
}
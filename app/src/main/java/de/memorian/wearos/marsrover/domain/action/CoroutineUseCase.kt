package de.memorian.wearos.marsrover.domain.action

import kotlinx.coroutines.flow.Flow

interface CoroutineUseCase<Params, ReturnType> {

    suspend fun execute(params: Params): ReturnType
}

abstract class FlowUseCase<Params, ReturnType> : CoroutineUseCase<Params, Flow<ReturnType>>
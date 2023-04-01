package de.memorian.wearos.marsrover.domain.action

interface CoroutineUseCase<Params, ReturnType> {

    suspend fun execute(params: Params): Result<ReturnType>
}

package de.memorian.wearos.marsrover.app.domain.action

interface CoroutineUseCase<Params, ReturnType> {

    suspend fun execute(params: Params): Result<ReturnType>
}

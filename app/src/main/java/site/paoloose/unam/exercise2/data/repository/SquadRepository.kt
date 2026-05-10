package site.paoloose.unam.exercise2.data.repository

import site.paoloose.unam.exercise2.data.remote.ApiFootballService
import site.paoloose.unam.exercise2.data.remote.WorldCupApiService
import site.paoloose.unam.exercise2.data.remote.dto.TeamSquadDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SquadRepository(
    private val computomovilService: WorldCupApiService,
    private val footballApiService: ApiFootballService
) {
    suspend fun getSquad(teamId: Int, useFootballApi: Boolean): Result<TeamSquadDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = if (useFootballApi) {
                    footballApiService.getSquad(teamId)
                } else {
                    computomovilService.getSquad(teamId)
                }
                val squad = response.response.firstOrNull()
                if (squad != null) {
                    Result.success(squad)
                } else {
                    Result.failure(NoSquadFoundException(teamId))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

class NoSquadFoundException(teamId: Int) : Exception("No squad found for team $teamId")

package site.paoloose.unam.exercise1.data.repository

import site.paoloose.unam.exercise1.data.remote.WorldCupApiService
import site.paoloose.unam.exercise1.data.remote.dto.TeamSquadDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SquadRepository(private val apiService: WorldCupApiService) {
    suspend fun getSquad(teamId: Int): Result<TeamSquadDto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSquad(teamId)
            val squad = response.response.firstOrNull()
            if (squad != null) {
                Result.success(squad)
            } else {
                Result.failure(Exception("No squad found for this team"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

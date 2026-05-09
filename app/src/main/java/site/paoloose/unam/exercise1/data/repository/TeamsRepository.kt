package site.paoloose.unam.exercise1.data.repository

import site.paoloose.unam.exercise1.data.remote.WorldCupApiService
import site.paoloose.unam.exercise1.data.remote.dto.TeamVenueDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamsRepository(private val apiService: WorldCupApiService) {
    suspend fun getTeams(): Result<List<TeamVenueDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTeams()
            Result.success(response.response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

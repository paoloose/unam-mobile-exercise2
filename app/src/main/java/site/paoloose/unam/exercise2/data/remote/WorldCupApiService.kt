package site.paoloose.unam.exercise2.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import site.paoloose.unam.exercise2.data.remote.dto.TeamsResponse

interface WorldCupApiService {
    @GET("teams2026.php")
    suspend fun getTeams(
        @Query("league") league: Int = 1,
        @Query("season") season: Int = 2026
    ): TeamsResponse

    @GET("squads.php")
    suspend fun getSquad(
        @Query("team") teamId: Int
    ): site.paoloose.unam.exercise2.data.remote.dto.SquadResponse
}

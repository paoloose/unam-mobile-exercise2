package site.paoloose.unam.exercise2.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import site.paoloose.unam.exercise2.data.remote.dto.SquadResponse

interface ApiFootballService {
    @GET("players/squads")
    suspend fun getSquad(@Query("team") teamId: Int): SquadResponse
}

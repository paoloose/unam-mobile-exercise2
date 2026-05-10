package site.paoloose.unam.exercise2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SquadResponse(
    @SerializedName("response") val response: List<TeamSquadDto>
)

data class TeamSquadDto(
    @SerializedName("team") val team: SquadTeamDto,
    @SerializedName("players") val players: List<PlayerDto>
)

data class SquadTeamDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String?
)

data class PlayerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int?,
    @SerializedName("number") val number: Int?,
    @SerializedName("position") val position: String,
    @SerializedName("photo") val photo: String?
)

package site.paoloose.unam.exercise2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TeamsResponse(
    @SerializedName("response") val response: List<TeamVenueDto>
)

data class TeamVenueDto(
    @SerializedName("team") val team: TeamDto,
    @SerializedName("venue") val venue: VenueDto
)

data class TeamDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String?,
    @SerializedName("country") val country: String,
    @SerializedName("founded") val founded: Int?,
    @SerializedName("national") val national: Boolean,
    @SerializedName("logo") val logo: String?
)

data class VenueDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("surface") val surface: String?,
    @SerializedName("image") val image: String?
)

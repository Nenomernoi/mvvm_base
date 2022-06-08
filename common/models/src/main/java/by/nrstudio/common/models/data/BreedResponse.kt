package by.nrstudio.common.models.data

import com.squareup.moshi.Json

data class BreedResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "adaptability") val adaptability: Int,
    @field:Json(name = "affection_level") val affectionLevel: Int,
    @field:Json(name = "alt_names") val altNames: String,
    @field:Json(name = "cfa_url") val cfaUrl: String,
    @field:Json(name = "child_friendly") val childFriendly: Int,
    @field:Json(name = "country_code") val countryCode: String,
    @field:Json(name = "country_codes") val countryCodes: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "dog_friendly") val dogFriendly: Int,
    @field:Json(name = "energy_level") val energyLevel: Int,
    @field:Json(name = "experimental") val experimental: Int,
    @field:Json(name = "grooming") val grooming: Int,
    @field:Json(name = "hairless") val hairless: Int,
    @field:Json(name = "health_issues") val healthIssues: Int,
    @field:Json(name = "hypoallergenic") val hypoallergenic: Int,
    @field:Json(name = "image") val image: ImageResponse?,
    @field:Json(name = "indoor") val indoor: Int,
    @field:Json(name = "intelligence") val intelligence: Int,
    @field:Json(name = "lap") val lap: Int,
    @field:Json(name = "life_span") val lifeSpan: String,
    @field:Json(name = "natural") val natural: Int,
    @field:Json(name = "origin") val origin: String,
    @field:Json(name = "rare") val rare: Int,
    @field:Json(name = "reference_image_id") val referenceImageId: String,
    @field:Json(name = "rex") val rex: Int,
    @field:Json(name = "shedding_level") val sheddingLevel: Int,
    @field:Json(name = "short_legs") val shortLegs: Int,
    @field:Json(name = "social_needs") val socialNeeds: Int,
    @field:Json(name = "stranger_friendly") val strangerFriendly: Int,
    @field:Json(name = "suppressed_tail") val suppressedTail: Int,
    @field:Json(name = "temperament") val temperament: String,
    @field:Json(name = "vcahospitals_url") val vcaHospitalsUrl: String,
    @field:Json(name = "vetstreet_url") val vetStreetUrl: String,
    @field:Json(name = "vocalisation") val vocalisation: Int,
    @field:Json(name = "weight") val weight: WeightResponse,
    @field:Json(name = "wikipedia_url") val wikipediaUrl: String
)

data class ImageResponse(
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "id") val id: String,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "width") val width: Int
)

data class WeightResponse(
    @field:Json(name = "imperial") val imperial: String,
    @field:Json(name = "metric") val metric: String
)

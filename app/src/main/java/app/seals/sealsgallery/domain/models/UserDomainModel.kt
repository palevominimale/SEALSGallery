package app.seals.sealsgallery.domain.models

data class UserDomainModel(
    var uid: String = "",
    var name: String = "",
    var photoLink: String = "",
    var lastLogin: Long = 0,
    var firstLogin: Long = 0,
    var numberOfTracks: Int = 0,
    var listOfSubscribers: List<String> = listOf(""),
    var listOfSubscriptions: List<String> = listOf(""),
    var bio: String = "",
    var country: String = "",
    var language: String = "",
    var dateOfBirth: Long = 0,
    var maxRange: Int = 0,
    var avgRange: Int = 0,
    var totalTime: Int = 0
)
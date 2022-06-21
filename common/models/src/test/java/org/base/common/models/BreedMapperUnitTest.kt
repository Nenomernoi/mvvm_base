package org.base.common.models

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.base.common.models.data.BreedResponse
import org.base.common.models.data.ImageResponse
import org.base.common.models.data.WeightResponse
import org.base.common.models.domain.Breed
import org.base.common.models.mapper.BreedMapper
import org.base.common.models.mapper.BreedMapperImpl
import org.base.common.models.presentation.BreedUi
import org.base.db.model.BreedDb
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BreedMapperUnitTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testCoroutineDispatcher)
    private val mapper: BreedMapper = BreedMapperImpl(defaultDispatcher = testCoroutineDispatcher)

    @Test
    fun `assert map REMOTE BreedResponse to DOMAIN Breed is passing the right data`() = testScope.runTest {
        val remoteBreed = BreedResponse(
            id = "abys",
            name = "Abyssinian",
            cfaUrl = "http://cfa.org/Breeds/BreedsAB/Abyssinian.aspx",
            vetStreetUrl = "http://www.vetstreet.com/cats/abyssinian",
            vcaHospitalsUrl = "https://vcahospitals.com/know-your-pet/cat-breeds/abyssinian",
            temperament = "Active, Energetic, Independent, Intelligent, Gentle",
            origin = "Egypt",
            countryCodes = "EG",
            countryCode = "EG",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            lifeSpan = "14 - 15",
            indoor = 0,
            lap = 1,
            altNames = "",
            adaptability = 5,
            affectionLevel = 5,
            childFriendly = 3,
            dogFriendly = 4,
            energyLevel = 5,
            grooming = 1,
            healthIssues = 2,
            intelligence = 5,
            sheddingLevel = 2,
            socialNeeds = 5,
            strangerFriendly = 5,
            vocalisation = 1,
            experimental = 0,
            hairless = 0,
            natural = 1,
            rare = 0,
            rex = 0,
            suppressedTail = 0,
            shortLegs = 0,
            wikipediaUrl = "https://en.wikipedia.org/wiki/Abyssinian_(cat)",
            hypoallergenic = 0,
            referenceImageId = "0XYvRd7oD",
            weight = WeightResponse(
                imperial = "7  -  10",
                metric = "3 - 5"
            ),
            image = ImageResponse(
                id = "0XYvRd7oD",
                width = 1204,
                height = 1445,
                url = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
            )
        )
        val remoteBreeds = listOf(remoteBreed)

        val domainBreed = mapper.mapRemoteListToDomain(remoteBreeds)
            .first()

        assertEquals(
            "Remote breed 'id' is not the same as the Domain breed 'id'",
            remoteBreed.id,
            domainBreed.id
        )
        assertEquals(
            "Remote breed 'name' is not the same as the Domain breed 'name'",
            remoteBreed.name,
            domainBreed.name
        )
        assertEquals(
            "Remote breed 'description' is not the same as the Domain breed 'description'",
            remoteBreed.description,
            domainBreed.description
        )
        assertEquals(
            "Remote breed 'image' is not the same as the Domain breed 'image'",
            remoteBreed.image?.url,
            domainBreed.image
        )
        assertEquals(
            "Remote breed 'lifeSpan' is not the same as the Domain breed 'lifeSpan'",
            remoteBreed.lifeSpan,
            domainBreed.lifeSpan
        )
        assertEquals(
            "Remote breed 'origin' is not the same as the Domain breed 'origin'",
            remoteBreed.origin,
            domainBreed.origin
        )
        assertEquals(
            "Remote breed 'countryCode' is not the same as the Domain breed 'countryCode'",
            remoteBreed.countryCode,
            domainBreed.countryCode
        )
        assertEquals(
            "Remote breed 'weight' is not the same as the Domain breed 'weight'",
            remoteBreed.weight.metric,
            domainBreed.weight
        )
    }

    @Test
    fun `assert map DOMAIN Breed to DB BreedDb is passing the right data`() = testScope.runTest {
        val domainBreed = Breed(
            id = "abys",
            name = "Abyssinian",
            origin = "Egypt",
            countryCode = "EG",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            lifeSpan = "14 - 15",
            weight = "3 - 5",
            image = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
        )
        val domainBreeds = listOf(domainBreed)

        val dbBreed = mapper.mapDomainListToDb(domainBreeds)
            .first()

        assertEquals(
            "Domain breed 'id' is not the same as the Dd breed 'id'",
            domainBreed.id,
            dbBreed.uuid
        )
        assertEquals(
            "Domain breed 'name' is not the same as the Dd breed 'name'",
            domainBreed.name,
            dbBreed.name
        )
        assertEquals(
            "Domain breed 'origin' is not the same as the Dd breed 'origin'",
            domainBreed.origin,
            dbBreed.origin
        )
        assertEquals(
            "Domain breed 'countryCode' is not the same as the Dd breed 'countryCode'",
            domainBreed.countryCode,
            dbBreed.countryCode
        )
        assertEquals(
            "Domain breed 'description' is not the same as the Dd breed 'description'",
            domainBreed.description,
            dbBreed.description
        )
        assertEquals(
            "Domain breed 'lifeSpan' is not the same as the Dd breed 'lifeSpan'",
            domainBreed.lifeSpan,
            dbBreed.lifeSpan
        )
        assertEquals(
            "Domain breed 'weight' is not the same as the Dd breed 'weight'",
            domainBreed.weight,
            dbBreed.weight
        )
        assertEquals(
            "Domain breed 'image' is not the same as the Dd breed 'image'",
            domainBreed.image,
            dbBreed.image
        )
    }

    @Test
    fun `assert map DOMAIN Breed to UI BreedUi is passing the right data`() = testScope.runTest {
        val domainBreed = Breed(
            id = "abys",
            name = "Abyssinian",
            origin = "Egypt",
            countryCode = "EG",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            lifeSpan = "14 - 15",
            weight = "3 - 5",
            image = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
        )
        val domainBreeds = listOf(domainBreed)

        val uiBreed = mapper.mapDomainListToUi(domainBreeds)
            .first()

        assertEquals(
            "Domain breed 'id' is not the same as the UI breed 'id'",
            domainBreed.id,
            uiBreed.id
        )
        assertEquals(
            "Domain breed 'name' is not the same as the UI breed 'name'",
            domainBreed.name,
            uiBreed.name
        )
        assertEquals(
            "Domain breed 'description' is not the same as the UI breed 'description'",
            domainBreed.description,
            uiBreed.description
        )
        assertEquals(
            "Domain breed 'countryCode' is not the same as the UI breed 'countryFlag'",
            "https://countryflagsapi.com/png/${domainBreed.countryCode.lowercase()}",
            uiBreed.countryFlag
        )
        assertEquals(
            "Domain breed 'image' is not the same as the UI breed 'image'",
            domainBreed.image,
            uiBreed.image
        )
    }

    @Test
    fun `assert map DB Breed to UI BreedUi is passing the right data`() = testScope.runTest {
        val dbBreed = BreedDb(
            uuid = "abys",
            name = "Abyssinian",
            origin = "Egypt",
            countryCode = "EG",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            lifeSpan = "14 - 15",
            weight = "3 - 5",
            image = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
        )
        val dbBreeds = listOf(dbBreed)

        val uiBreed = mapper.mapDbListToUi(dbBreeds)
            .first()

        assertEquals(
            "DB breed 'id' is not the same as the UI breed 'id'",
            dbBreed.uuid,
            uiBreed.id
        )
        assertEquals(
            "DB breed 'name' is not the same as the UI breed 'name'",
            dbBreed.name,
            uiBreed.name
        )
        assertEquals(
            "DB breed 'description' is not the same as the UI breed 'description'",
            dbBreed.description,
            uiBreed.description
        )
        assertEquals(
            "DB breed 'countryCode' is not the same as the UI breed 'countryFlag'",
            "https://countryflagsapi.com/png/${dbBreed.countryCode.lowercase()}",
            uiBreed.countryFlag
        )
        assertEquals(
            "DB breed 'image' is not the same as the UI breed 'image'",
            dbBreed.image,
            uiBreed.image
        )
    }

    @Test
    fun `assert map Db Breed to UI FULL BreedUi is passing the right data`() = testScope.runTest {
        val dbBreed = BreedDb(
            uuid = "abys",
            name = "Abyssinian",
            origin = "Egypt",
            countryCode = "EG",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            lifeSpan = "14 - 15",
            weight = "3 - 5",
            image = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
        )
        val dbBreeds = listOf(dbBreed)

        val uiBreed = mapper.mapDbListToFullUi(dbBreeds)
            .first()

        assertEquals(
            "DB breed 'id' is not the same as the UI FULL breed 'id'",
            dbBreed.uuid,
            uiBreed.id
        )
        assertEquals(
            "DB breed 'name' is not the same as the UI FULL breed 'name'",
            dbBreed.name,
            uiBreed.name
        )
        assertEquals(
            "DB breed 'description' is not the same as the UI FULL breed 'description'",
            dbBreed.description,
            uiBreed.description
        )
        assertEquals(
            "DB breed 'countryCode' is not the same as the UI FULL breed 'countryFlag'",
            "https://countryflagsapi.com/png/${dbBreed.countryCode.lowercase()}",
            uiBreed.countryFlag
        )
        assertEquals(
            "DB breed 'image' is not the same as the UI FULL breed 'image'",
            dbBreed.image,
            uiBreed.image
        )
    }

    @Test
    fun `assert map UI Breed to UI FULL BreedUi is passing the right data`() = testScope.runTest {
        val uiBreed = BreedUi(
            id = "abys",
            name = "Abyssinian",
            countryFlag = "https://countryflagsapi.com/png/EG",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            image = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
        )
        val uiBreeds = listOf(uiBreed)

        val uiFullBreed = mapper.mapUiListToFullUi(uiBreeds)
            .first()

        assertEquals(
            "UI breed 'id' is not the same as the UI FULL breed 'id'",
            uiBreed.id,
            uiFullBreed.id
        )
        assertEquals(
            "UI breed 'name' is not the same as the UI FULL breed 'name'",
            uiBreed.name,
            uiFullBreed.name
        )
        assertEquals(
            "UI breed 'description' is not the same as the UI FULL breed 'description'",
            uiBreed.description,
            uiFullBreed.description
        )
        assertEquals(
            "UI breed 'countryCode' is not the same as the UI FULL breed 'countryFlag'",
            uiBreed.countryFlag,
            uiFullBreed.countryFlag
        )
        assertEquals(
            "UI breed 'image' is not the same as the UI FULL breed 'image'",
            uiBreed.image,
            uiFullBreed.image
        )
        assertEquals(
            "UI breed 'lifeSpan' is not the same (DEFAULT) as the UI FULL breed 'lifeSpan'",
            "---",
            uiFullBreed.lifeSpan
        )
        assertEquals(
            "UI breed 'weight' is not the same (DEFAULT) as the UI FULL breed 'weight'",
            "---",
            uiFullBreed.weight
        )
    }
}

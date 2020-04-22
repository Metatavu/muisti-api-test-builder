package fi.metatavu.muisti.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.ExhibitionFloorsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Exhibition
import fi.metatavu.muisti.api.client.models.ExhibitionFloor
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling exhibitionFloors
 */
class ExhibitionFloorTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<ExhibitionFloor, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new exhibition floor with default values
     *
     * @param exhibitionId
     * @return created exhibition floor
     */
    fun create(exhibitionId: UUID): ExhibitionFloor {
        return create(exhibitionId, ExhibitionFloor(name = "default floor"))
    }

    /**
     * Creates new exhibition floor with default values
     *
     * @param exhibition
     * @return created exhibition Floor
     */
    fun create(exhibition: Exhibition): ExhibitionFloor {
        return create(exhibitionId = exhibition.id!!)
    }

    /**
     * Creates new exhibition Floor
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created exhibition Floor
     */
    fun create(exhibitionId: UUID, payload: ExhibitionFloor): ExhibitionFloor {
        val result: ExhibitionFloor = this.getApi().createExhibitionFloor(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds exhibition Floor
     *
     * @param exhibitionId exhibition id
     * @param exhibitionFloorId exhibition Floor id
     * @return exhibition Floor
     */
    fun findExhibitionFloor(exhibitionId: UUID, exhibitionFloorId: UUID): ExhibitionFloor? {
        return api.findExhibitionFloor(exhibitionId, exhibitionFloorId)
    }

    /**
     * Lists exhibition Floors
     *
     * @param exhibitionId exhibition id
     * @return exhibition Floors
     */
    fun listExhibitionFloors(exhibitionId: UUID): Array<ExhibitionFloor> {
        return api.listExhibitionFloors(exhibitionId)
    }

    /**
     * Updates exhibition Floor
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated exhibition Floor
     */
    fun updateExhibitionFloor(exhibitionId: UUID, body: ExhibitionFloor): ExhibitionFloor? {
        return api.updateExhibitionFloor(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a exhibitionFloor from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionFloor exhibitionFloor to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionFloor: ExhibitionFloor) {
        delete(exhibitionId, exhibitionFloor.id!!)
    }

    /**
     * Deletes a exhibitionFloor from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionFloorId exhibitionFloor id to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionFloorId: UUID) {
        api.deleteExhibitionFloor(exhibitionId, exhibitionFloorId)
        removeCloseable { closable: Any ->
            if (closable !is ExhibitionFloor) {
                return@removeCloseable false
            }

            val closeableExhibitionFloor: ExhibitionFloor = closable
            closeableExhibitionFloor.id!!.equals(exhibitionFloorId)
        }
    }

    /**
     * Asserts exhibitionFloor count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     */
    fun assertCount(expected: Int, exhibitionId: UUID) {
        assertEquals(expected, api.listExhibitionFloors(exhibitionId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param exhibitionFloorId exhibitionFloor id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, exhibitionFloorId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, exhibitionFloorId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param exhibitionFloorId exhibitionFloor id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, exhibitionFloorId: UUID) {
        try {
            api.findExhibitionFloor(exhibitionId, exhibitionFloorId)
            fail(String.format("Expected find to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts list status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     */
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID) {
        try {
            api.listExhibitionFloors(exhibitionId)
            fail(String.format("Expected list to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts create fails with given status
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param payload payload
     */
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: ExhibitionFloor) {
        try {
            create(exhibitionId, payload)
            fail(String.format("Expected create to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts update fails with given status
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param body body
     */
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: ExhibitionFloor) {
        try {
            updateExhibitionFloor(exhibitionId, body)
            fail(String.format("Expected update to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts delete fails with given status
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param id id
     */
    fun assertDeleteFail(expectedStatus: Int, exhibitionId: UUID, id: UUID) {
        try {
            delete(exhibitionId, id)
            fail(String.format("Expected delete to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    override fun clean(exhibitionFloor: ExhibitionFloor) {
        this.getApi().deleteExhibitionFloor(exhibitionFloor.exhibitionId!!, exhibitionFloor.id!!)
    }

    override fun getApi(): ExhibitionFloorsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ExhibitionFloorsApi(testBuilder.settings.apiBasePath)
    }

}
package fi.metatavu.muisti.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.ExhibitionRoomsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Exhibition
import fi.metatavu.muisti.api.client.models.ExhibitionFloor
import fi.metatavu.muisti.api.client.models.ExhibitionRoom
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling exhibitionRooms
 */
class ExhibitionRoomTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<ExhibitionRoom, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new exhibition room with default values
     *
     * @param exhibitionId exhibition id
     * @param floorId floor id
     * @return created exhibition room
     */
    fun create(exhibitionId: UUID, floorId: UUID): ExhibitionRoom {
        return create(exhibitionId, ExhibitionRoom(name = "default room", floorId = floorId))
    }

    /**
     * Creates new exhibition room with default values
     *
     * @param exhibition exhibition
     * @param floor floor
     * @return created exhibition room
     */
    fun create(exhibition: Exhibition, floor: ExhibitionFloor): ExhibitionRoom {
        return create(exhibitionId = exhibition.id!!, floorId = floor.id!!)
    }

    /**
     * Creates new exhibition Room
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created exhibition Room
     */
    fun create(exhibitionId: UUID, payload: ExhibitionRoom): ExhibitionRoom {
        val result: ExhibitionRoom = this.getApi().createExhibitionRoom(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds exhibition Room
     *
     * @param exhibitionId exhibition id
     * @param exhibitionRoomId exhibition Room id
     * @return exhibition Room
     */
    fun findExhibitionRoom(exhibitionId: UUID, exhibitionRoomId: UUID): ExhibitionRoom? {
        return api.findExhibitionRoom(exhibitionId, exhibitionRoomId)
    }

    /**
     * Lists exhibition Rooms
     *
     * @param exhibitionId exhibition id
     * @param floorId filter by floor id
     * @return exhibition Rooms
     */
    fun listExhibitionRooms(exhibitionId: UUID, floorId: UUID?): Array<ExhibitionRoom> {
        return api.listExhibitionRooms(exhibitionId = exhibitionId, floorId = floorId)
    }

    /**
     * Updates exhibition Room
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated exhibition Room
     */
    fun updateExhibitionRoom(exhibitionId: UUID, body: ExhibitionRoom): ExhibitionRoom? {
        return api.updateExhibitionRoom(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a exhibitionRoom from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionRoom exhibitionRoom to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionRoom: ExhibitionRoom) {
        delete(exhibitionId, exhibitionRoom.id!!)
    }

    /**
     * Deletes a exhibitionRoom from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionRoomId exhibitionRoom id to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionRoomId: UUID) {
        api.deleteExhibitionRoom(exhibitionId, exhibitionRoomId)
        removeCloseable { closable: Any ->
            if (closable !is ExhibitionRoom) {
                return@removeCloseable false
            }

            val closeableExhibitionRoom: ExhibitionRoom = closable
            closeableExhibitionRoom.id!!.equals(exhibitionRoomId)
        }
    }

    /**
     * Asserts exhibitionRoom count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     * @param floorId filter by floor id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, floorId: UUID?) {
        assertEquals(expected, api.listExhibitionRooms(exhibitionId = exhibitionId, floorId = floorId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param exhibitionRoomId exhibitionRoom id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, exhibitionRoomId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, exhibitionRoomId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param exhibitionRoomId exhibitionRoom id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, exhibitionRoomId: UUID) {
        try {
            api.findExhibitionRoom(exhibitionId, exhibitionRoomId)
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
     * @param floorId filter by floor id
     */
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, floorId: UUID?) {
        try {
            api.listExhibitionRooms(exhibitionId = exhibitionId, floorId = floorId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: ExhibitionRoom) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: ExhibitionRoom) {
        try {
            updateExhibitionRoom(exhibitionId, body)
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

    override fun clean(exhibitionRoom: ExhibitionRoom) {
        this.getApi().deleteExhibitionRoom(exhibitionRoom.exhibitionId!!, exhibitionRoom.id!!)
    }

    override fun getApi(): ExhibitionRoomsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ExhibitionRoomsApi(testBuilder.settings.apiBasePath)
    }

}
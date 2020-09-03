package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.RfidAntennasApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Point
import fi.metatavu.muisti.api.client.models.RfidAntenna
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling RFID antenna
 */
class RfidAntennaTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<RfidAntenna, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates new RFID antenna using default values
     *
     * @param exhibitionId exhibition id
     * @param roomId room id
     * @return created RFID antenna
     */
    fun create(exhibitionId: UUID, roomId: UUID): RfidAntenna {
        val result: RfidAntenna = this.api.createRfidAntenna(exhibitionId, RfidAntenna(
            name = "Default",
            roomId = roomId,
            readerId = "readerid1234",
            antennaNumber = 1,
            location = Point(x = 1.0, y = 2.0),
            visitorSessionStartThreshold = 80,
            visitorSessionEndThreshold = 10
        ))

        addClosable(result)
        return result
    }

    /**
     * Creates new RFID antenna
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created RFID antenna
     */
    fun create(exhibitionId: UUID, payload: RfidAntenna): RfidAntenna {
        val result: RfidAntenna = this.api.createRfidAntenna(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds RFID antenna
     *
     * @param exhibitionId exhibition id
     * @param rfidAntennaId RFID antenna id
     * @return RFID antenna
     */
    fun findRfidAntenna(exhibitionId: UUID, rfidAntennaId: UUID): RfidAntenna? {
        return api.findRfidAntenna(exhibitionId, rfidAntennaId)
    }

    /**
     * Lists RFID antennas
     *
     * @param exhibitionId exhibition id
     * @param deviceGroupId device group id
     * @param roomId room id
     * @return RFID antennas
     */
    fun listRfidAntennas(exhibitionId: UUID, deviceGroupId: UUID?, roomId: UUID?): Array<RfidAntenna> {
        return api.listRfidAntennas(exhibitionId = exhibitionId, deviceGroupId = deviceGroupId, roomId = roomId)
    }

    /**
     * Updates RFID antenna
     *
     * @param exhibitionId exhibition id
     * @param payload update payload
     * @return updated RFID antenna
     */
    fun updateRfidAntenna(exhibitionId: UUID, payload: RfidAntenna): RfidAntenna? {
        return api.updateRfidAntenna(exhibitionId, payload.id!!, payload)
    }

    /**
     * Deletes an rfidAntenna from the API
     *
     * @param exhibitionId exhibition id
     * @param rfidAntenna rfidAntenna to be deleted
     */
    fun delete(exhibitionId: UUID, rfidAntenna: RfidAntenna) {
        delete(exhibitionId, rfidAntenna.id!!)
    }

    /**
     * Deletes an rfidAntenna from the API
     *
     * @param exhibitionId exhibition id
     * @param rfidAntennaId rfidAntenna id to be deleted
     */
    fun delete(exhibitionId: UUID, rfidAntennaId: UUID) {
        api.deleteRfidAntenna(exhibitionId, rfidAntennaId)
        removeCloseable { closable: Any ->
            if (closable !is RfidAntenna) {
                return@removeCloseable false
            }

            val closeableRfidAntenna: RfidAntenna = closable
            closeableRfidAntenna.id!!.equals(rfidAntennaId)
        }
    }

    /**
     * Asserts RFID antenna count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     * @param deviceGroupId device group id
     * @param roomId room id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, deviceGroupId: UUID?, roomId: UUID?) {
        assertEquals(expected, api.listRfidAntennas(exhibitionId = exhibitionId, deviceGroupId = deviceGroupId, roomId = roomId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param rfidAntennaId rfidAntenna id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, rfidAntennaId: UUID) {
        assertFindFail(expectedStatus, exhibitionId, rfidAntennaId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param rfidAntennaId rfidAntenna id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, rfidAntennaId: UUID) {
        try {
            api.findRfidAntenna(exhibitionId, rfidAntennaId)
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
     * @param deviceGroupId exhibition group id
     */
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, deviceGroupId: UUID?, roomId: UUID?) {
        try {
            api.listRfidAntennas(exhibitionId = exhibitionId, deviceGroupId = deviceGroupId, roomId = roomId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: RfidAntenna) {
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
     * @param payload payload
     */
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, payload: RfidAntenna) {
        try {
            updateRfidAntenna(exhibitionId, payload)
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

    override fun clean(rfidAntenna: RfidAntenna) {
        this.getApi().deleteRfidAntenna(rfidAntenna.exhibitionId!!, rfidAntenna.id!!)
    }

    override fun getApi(): RfidAntennasApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return RfidAntennasApi(testBuilder.settings.apiBasePath)
    }

}

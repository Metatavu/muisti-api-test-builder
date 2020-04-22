package fi.metatavu.muisti.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.ExhibitionDevicesApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.*
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling exhibitionDevices
 */
class ExhibitionDeviceTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<ExhibitionDevice, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new exhibition device using default values
     *
     * @param exhibitionId exhibition id
     * @param groupId group id
     * @param modelId model id
     * @return created exhibition device
     */
    fun create(exhibitionId: UUID, groupId: UUID, modelId: UUID): ExhibitionDevice {
        val result: ExhibitionDevice = this.api.createExhibitionDevice(exhibitionId, ExhibitionDevice(
            groupId = groupId,
            modelId = modelId,
            name = "Default",
            screenOrientation = ScreenOrientation.portrait
        ))

        addClosable(result)
        return result
    }

    /**
     * Creates new exhibition device using default values
     *
     * @param exhibition exhibition
     * @param group group
     * @param model model
     * @return created exhibition device
     */
    fun create(exhibition: Exhibition, group: ExhibitionDeviceGroup, model: DeviceModel): ExhibitionDevice {
        return create(
            exhibitionId = exhibition.id!!,
            modelId = model.id!!,
            groupId = group.id!!
        )
    }

    /**
     * Creates new exhibition Device
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created exhibition Device
     */
    fun create(exhibitionId: UUID, payload: ExhibitionDevice): ExhibitionDevice {
        val result: ExhibitionDevice = this.api.createExhibitionDevice(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds exhibition Device
     *
     * @param exhibitionId exhibition id
     * @param exhibitionDeviceId exhibition Device id
     * @return exhibition Device
     */
    fun findExhibitionDevice(exhibitionId: UUID, exhibitionDeviceId: UUID): ExhibitionDevice? {
        return api.findExhibitionDevice(exhibitionId, exhibitionDeviceId)
    }

    /**
     * Lists exhibition Devices
     *
     * @param exhibitionId exhibition id
     * @param exhibitionGroupId exhibition group id
     * @return exhibition Devices
     */
    fun listExhibitionDevices(exhibitionId: UUID, exhibitionGroupId: UUID?): Array<ExhibitionDevice> {
        return api.listExhibitionDevices(exhibitionId, exhibitionGroupId)
    }

    /**
     * Updates exhibition Device
     *
     * @param exhibitionId exhibition id
     * @param payload update payload
     * @return updated exhibition Device
     */
    fun updateExhibitionDevice(exhibitionId: UUID, payload: ExhibitionDevice): ExhibitionDevice? {
        return api.updateExhibitionDevice(exhibitionId, payload.id!!, payload)
    }

    /**
     * Deletes a exhibitionDevice from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionDevice exhibitionDevice to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionDevice: ExhibitionDevice) {
        delete(exhibitionId, exhibitionDevice.id!!)
    }

    /**
     * Deletes a exhibitionDevice from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionDeviceId exhibitionDevice id to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionDeviceId: UUID) {
        api.deleteExhibitionDevice(exhibitionId, exhibitionDeviceId)
        removeCloseable { closable: Any ->
            if (closable !is ExhibitionDevice) {
                return@removeCloseable false
            }

            val closeableExhibitionDevice: ExhibitionDevice = closable
            closeableExhibitionDevice.id!!.equals(exhibitionDeviceId)
        }
    }

    /**
     * Asserts exhibitionDevice count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     * @param exhibitionGroupId exhibition group id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, exhibitionGroupId: UUID?) {
        assertEquals(expected, api.listExhibitionDevices(exhibitionId, exhibitionGroupId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param exhibitionDeviceId exhibitionDevice id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, exhibitionDeviceId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, exhibitionDeviceId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param exhibitionDeviceId exhibitionDevice id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, exhibitionDeviceId: UUID) {
        try {
            api.findExhibitionDevice(exhibitionId, exhibitionDeviceId)
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
     * @param exhibitionGroupId exhibition group id
     */
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, exhibitionGroupId: UUID?) {
        try {
            api.listExhibitionDevices(exhibitionId, exhibitionGroupId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: ExhibitionDevice) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, payload: ExhibitionDevice) {
        try {
            updateExhibitionDevice(exhibitionId, payload)
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

    override fun clean(exhibitionDevice: ExhibitionDevice) {
        this.getApi().deleteExhibitionDevice(exhibitionDevice.exhibitionId!!, exhibitionDevice.id!!)
    }

    override fun getApi(): ExhibitionDevicesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ExhibitionDevicesApi(testBuilder.settings.apiBasePath)
    }

}
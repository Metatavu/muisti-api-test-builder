package fi.metatavu.muisti.api.test.functional.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.DeviceModelsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.DeviceModel
import fi.metatavu.muisti.api.client.models.DeviceModelCapabilities
import fi.metatavu.muisti.api.client.models.DeviceModelDimensions
import fi.metatavu.muisti.api.client.models.DeviceModelDisplayMetrics
import fi.metatavu.muisti.api.test.functional.TestBuilder
import fi.metatavu.muisti.api.test.functional.impl.ApiTestBuilderResource
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling deviceModels
 */
class DeviceModelTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<DeviceModel, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates new device model with default values
     *
     * @return created device model
     */
    fun create(): DeviceModel {
        return create(DeviceModel(
            manufacturer = "default manufacturer",
            model = "default model",
            dimensions = DeviceModelDimensions(),
            displayMetrics = DeviceModelDisplayMetrics(),
            capabilities = DeviceModelCapabilities( touch = true)
        ))
    }

    /**
     * Creates new device model
     *
     * @param payload payload
     * @return created device model
     */
    fun create(payload: DeviceModel): DeviceModel {
        val result: DeviceModel = this.getApi().createDeviceModel(payload)
        addClosable(result)
        return result
    }

    /**
     * Finds device model
     *
     * @param deviceModelId device model id
     * @return device model
     */
    fun findDeviceModel(deviceModelId: UUID): DeviceModel? {
        return api.findDeviceModel(deviceModelId)
    }

    /**
     * Lists device models
     *
     * @return device models
     */
    fun listDeviceModels(): Array<DeviceModel> {
        return api.listDeviceModels()
    }

    /**
     * Updates device model
     *
     * @param body update body
     * @return updated device model
     */
    fun updateDeviceModel(body: DeviceModel): DeviceModel? {
        return api.updateDeviceModel(body.id!!, body)
    }

    /**
     * Deletes a deviceModel from the API
     *
     * @param deviceModel deviceModel to be deleted
     */
    fun delete(deviceModel: DeviceModel) {
        delete(deviceModel.id!!)
    }

    /**
     * Deletes a deviceModel from the API
     *
     * @param deviceModelId deviceModel id to be deleted
     */
    fun delete(deviceModelId: UUID) {
        api.deleteDeviceModel(deviceModelId)
        removeCloseable { closable: Any ->
            if (closable !is DeviceModel) {
                return@removeCloseable false
            }

            val closeableDeviceModel: DeviceModel = closable
            closeableDeviceModel.id!!.equals(deviceModelId)
        }
    }

    /**
     * Asserts deviceModel count within the system
     *
     * @param expected expected count
     */
    fun assertCount(expected: Int) {
        assertEquals(expected, api.listDeviceModels().size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param deviceModelId deviceModel id
     */
    fun assertFindFailStatus(expectedStatus: Int, deviceModelId: UUID) {
        assertFindFailStatus(expectedStatus, deviceModelId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param deviceModelId deviceModel id
     */
    fun assertFindFail(expectedStatus: Int, deviceModelId: UUID) {
        try {
            api.findDeviceModel(deviceModelId)
            fail(String.format("Expected find to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts list status fails with given status code
     *
     * @param expectedStatus expected status
     */
    fun assertListFail(expectedStatus: Int) {
        try {
            api.listDeviceModels()
            fail(String.format("Expected list to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts create fails with given status
     *
     * @param expectedStatus expected status
     * @param payload payload
     * @return created device model
     */
    fun assertCreateFail(expectedStatus: Int, payload: DeviceModel) {
        try {
            create(payload)
            fail(String.format("Expected create to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts update fails with given status
     *
     * @param expectedStatus expected status
     * @param body body
     */
    fun assertUpdateFail(expectedStatus: Int, body: DeviceModel) {
        try {
            updateDeviceModel(body)
            fail(String.format("Expected update to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts delete fails with given status
     *
     * @param expectedStatus expected status
     * @param id id
     */
    fun assertDeleteFail(expectedStatus: Int, id: UUID) {
        try {
            delete(id)
            fail(String.format("Expected delete to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    override fun clean(deviceModel: DeviceModel) {
        this.getApi().deleteDeviceModel(deviceModel.id!!)
    }

    override fun getApi(): DeviceModelsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return DeviceModelsApi(testBuilder.settings.apiBasePath)
    }

}
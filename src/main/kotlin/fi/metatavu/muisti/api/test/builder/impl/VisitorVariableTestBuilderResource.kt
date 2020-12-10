package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.VisitorVariablesApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.VisitorVariable
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling visitorVariables
 */
class VisitorVariableTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<VisitorVariable, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates new visitor variable
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created visitor variable
     */
    fun create(exhibitionId: UUID, payload: VisitorVariable): VisitorVariable {
        val result: VisitorVariable = this.api.createVisitorVariable(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds visitor variable
     *
     * @param exhibitionId exhibition id
     * @param visitorVariableId visitor variable id
     * @return visitor variable
     */
    fun findVisitorVariable(exhibitionId: UUID, visitorVariableId: UUID): VisitorVariable? {
        return api.findVisitorVariable(exhibitionId, visitorVariableId)
    }

    /**
     * Lists visitor variables
     *
     * @param exhibitionId exhibition id
     * @param name name
     * @return visitor variables
     */
    fun listVisitorVariables(exhibitionId: UUID, name: String?): Array<VisitorVariable> {
        return api.listVisitorVariables(exhibitionId = exhibitionId, name = name)
    }

    /**
     * Updates visitor variable
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated visitor variable
     */
    fun updateVisitorVariable(exhibitionId: UUID, body: VisitorVariable): VisitorVariable? {
        return api.updateVisitorVariable(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a visitorVariable from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorVariable visitorVariable to be deleted
     */
    fun delete(exhibitionId: UUID, visitorVariable: VisitorVariable) {
        delete(exhibitionId, visitorVariable.id!!)
    }

    /**
     * Deletes a visitorVariable from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorVariableId visitorVariable id to be deleted
     */
    fun delete(exhibitionId: UUID, visitorVariableId: UUID) {
        api.deleteVisitorVariable(exhibitionId, visitorVariableId)
        removeCloseable { closable: Any ->
            if (closable !is VisitorVariable) {
                return@removeCloseable false
            }

            val closeableVisitorVariable: VisitorVariable = closable
            closeableVisitorVariable.id!! == visitorVariableId
        }
    }

    /**
     * Asserts visitorVariable count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     * @param name filter by tag id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, name: String?) {
        assertEquals(expected, api.listVisitorVariables(exhibitionId = exhibitionId, name = name).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param visitorVariableId visitorVariable id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, visitorVariableId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, visitorVariableId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param visitorVariableId visitorVariable id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, visitorVariableId: UUID) {
        try {
            api.findVisitorVariable(exhibitionId, visitorVariableId)
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
     * @param name filter by tag id
     */
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, name: String?) {
        try {
            api.listVisitorVariables(exhibitionId = exhibitionId, name = name)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: VisitorVariable) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: VisitorVariable) {
        try {
            updateVisitorVariable(exhibitionId, body)
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

    override fun clean(visitorVariable: VisitorVariable) {
        this.api.deleteVisitorVariable(visitorVariable.exhibitionId!!, visitorVariable.id!!)
    }

    override fun getApi(): VisitorVariablesApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return VisitorVariablesApi(testBuilder.settings.apiBasePath)
    }

}
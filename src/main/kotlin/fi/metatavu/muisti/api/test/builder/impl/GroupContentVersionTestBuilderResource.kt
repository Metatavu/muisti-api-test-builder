package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.GroupContentVersionsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.*
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling group content versions
 */
class GroupContentVersionTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<GroupContentVersion, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new GroupContentVersion
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created GroupContentVersion
     */
    fun create(exhibitionId: UUID, payload: GroupContentVersion): GroupContentVersion {
        val result: GroupContentVersion = this.api.createGroupContentVersion(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds GroupContentVersion
     *
     * @param exhibitionId exhibition id
     * @param groupContentVersionId GroupContentVersion id
     * @return GroupContentVersion
     */
    fun findGroupContentVersion(exhibitionId: UUID, groupContentVersionId: UUID): GroupContentVersion? {
        return api.findGroupContentVersion(exhibitionId, groupContentVersionId)
    }

    /**
     * Lists GroupContentVersions
     *
     * @param exhibitionId exhibition id
     * @return GroupContentVersions
     */
    fun listGroupContentVersions(exhibitionId: UUID, contentVersionId: UUID?, deviceGroupId: UUID?): Array<GroupContentVersion> {
        return api.listGroupContentVersions(exhibitionId = exhibitionId, contentVersionId = contentVersionId, deviceGroupId = deviceGroupId)
    }

    /**
     * Updates GroupContentVersion
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated GroupContentVersion
     */
    fun updateGroupContentVersion(exhibitionId: UUID, body: GroupContentVersion): GroupContentVersion? {
        return api.updateGroupContentVersion(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a groupContentVersion from the API
     *
     * @param exhibitionId exhibition id
     * @param groupContentVersion groupContentVersion to be deleted
     */
    fun delete(exhibitionId: UUID, groupContentVersion: GroupContentVersion) {
        delete(exhibitionId, groupContentVersion.id!!)
    }

    /**
     * Deletes a groupContentVersion from the API
     *
     * @param exhibitionId exhibition id
     * @param groupContentVersionId groupContentVersion id to be deleted
     */
    fun delete(exhibitionId: UUID, groupContentVersionId: UUID) {
        api.deleteGroupContentVersion(exhibitionId, groupContentVersionId)
        removeCloseable { closable: Any ->
            if (closable !is GroupContentVersion) {
                return@removeCloseable false
            }

            val closeableGroupContentVersion: GroupContentVersion = closable
            closeableGroupContentVersion.id!!.equals(groupContentVersionId)
        }
    }

    /**
     * Asserts groupContentVersion count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, contentVersionId: UUID?, deviceGroupId: UUID?) {
        assertEquals(expected, api.listGroupContentVersions(exhibitionId = exhibitionId, contentVersionId = contentVersionId, deviceGroupId = deviceGroupId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param groupContentVersionId groupContentVersion id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, groupContentVersionId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, groupContentVersionId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param groupContentVersionId exhibitionGroupContentVersion id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, groupContentVersionId: UUID) {
        try {
            api.findGroupContentVersion(exhibitionId, groupContentVersionId)
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
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, contentVersionId: UUID?, deviceGroupId: UUID?) {
        try {
            api.listGroupContentVersions(exhibitionId = exhibitionId, contentVersionId = contentVersionId, deviceGroupId = deviceGroupId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: GroupContentVersion) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: GroupContentVersion) {
        try {
            updateGroupContentVersion(exhibitionId, body)
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
     * @param groupContentVersionId group content version id
     */
    fun assertDeleteFail(expectedStatus: Int, exhibitionId: UUID, groupContentVersionId: UUID) {
        try {
            delete(exhibitionId, groupContentVersionId)
            fail(String.format("Expected delete to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    override fun clean(groupContentVersion: GroupContentVersion) {
        this.api.deleteGroupContentVersion(groupContentVersion.exhibitionId!!, groupContentVersion.id!!)
    }

    override fun getApi(): GroupContentVersionsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return GroupContentVersionsApi(testBuilder.settings.apiBasePath)
    }

}
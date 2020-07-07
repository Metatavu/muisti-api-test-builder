package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.ContentVersionsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Exhibition
import fi.metatavu.muisti.api.client.models.ContentVersion
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling content versions
 */
class ContentVersionTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<ContentVersion, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new content version with default values
     *
     * @param exhibitionId
     * @return created content version
     */
    fun create(exhibitionId: UUID): ContentVersion {
        val floorId = testBuilder.admin().exhibitionFloors().create(exhibitionId).id!!
        val roomId = testBuilder.admin().exhibitionRooms().create(exhibitionId, floorId).id!!
        return create(exhibitionId, ContentVersion(name = "default contentVersion", language = "FI", rooms = arrayOf<UUID>(roomId)))
    }

    /**
     * Creates new content version with default values
     *
     * @param exhibition
     * @return created content version
     */
    fun create(exhibition: Exhibition): ContentVersion {
        return create(exhibitionId = exhibition.id!!)
    }

    /**
     * Creates new ContentVersion
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created ContentVersion
     */
    fun create(exhibitionId: UUID, payload: ContentVersion): ContentVersion {
        val result: ContentVersion = this.api.createContentVersion(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds ContentVersion
     *
     * @param exhibitionId exhibition id
     * @param contentVersionId ContentVersion id
     * @return ContentVersion
     */
    fun findContentVersion(exhibitionId: UUID, contentVersionId: UUID): ContentVersion? {
        return api.findContentVersion(exhibitionId, contentVersionId)
    }

    /**
     * Lists ContentVersions
     *
     * @param exhibitionId exhibition id
     * @return ContentVersions
     */
    fun listContentVersions(exhibitionId: UUID, roomId: UUID?): Array<ContentVersion> {
        return api.listContentVersions(exhibitionId, roomId)
    }

    /**
     * Updates ContentVersion
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated ContentVersion
     */
    fun updateContentVersion(exhibitionId: UUID, body: ContentVersion): ContentVersion? {
        return api.updateContentVersion(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a contentVersion from the API
     *
     * @param exhibitionId exhibition id
     * @param contentVersion contentVersion to be deleted
     */
    fun delete(exhibitionId: UUID, contentVersion: ContentVersion) {
        delete(exhibitionId, contentVersion.id!!)
    }

    /**
     * Deletes a contentVersion from the API
     *
     * @param exhibitionId exhibition id
     * @param contentVersionId contentVersion id to be deleted
     */
    fun delete(exhibitionId: UUID, contentVersionId: UUID) {
        api.deleteContentVersion(exhibitionId, contentVersionId)
        removeCloseable { closable: Any ->
            if (closable !is ContentVersion) {
                return@removeCloseable false
            }

            val closeableContentVersion: ContentVersion = closable
            closeableContentVersion.id!!.equals(contentVersionId)
        }
    }

    /**
     * Asserts contentVersion count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, roomId: UUID?) {
        assertEquals(expected, api.listContentVersions(exhibitionId, roomId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param contentVersionId contentVersion id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, contentVersionId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, contentVersionId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param contentVersionId exhibitionContentVersion id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, contentVersionId: UUID) {
        try {
            api.findContentVersion(exhibitionId, contentVersionId)
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
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, roomId: UUID?) {
        try {
            api.listContentVersions(exhibitionId, roomId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: ContentVersion) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: ContentVersion) {
        try {
            updateContentVersion(exhibitionId, body)
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

    override fun clean(contentVersion: ContentVersion) {
        this.api.deleteContentVersion(contentVersion.exhibitionId!!, contentVersion.id!!)
    }

    override fun getApi(): ContentVersionsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ContentVersionsApi(testBuilder.settings.apiBasePath)
    }

}
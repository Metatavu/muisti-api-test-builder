package fi.metatavu.muisti.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.ExhibitionContentVersionsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Exhibition
import fi.metatavu.muisti.api.client.models.ExhibitionContentVersion
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling exhibition content versions
 */
class ExhibitionContentVersionTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<ExhibitionContentVersion, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new exhibition content version with default values
     *
     * @param exhibitionId
     * @return created exhibition content version
     */
    fun create(exhibitionId: UUID): ExhibitionContentVersion {
        return create(exhibitionId, ExhibitionContentVersion(name = "default contentVersion"))
    }

    /**
     * Creates new exhibition content version with default values
     *
     * @param exhibition
     * @return created exhibition content version
     */
    fun create(exhibition: Exhibition): ExhibitionContentVersion {
        return create(exhibitionId = exhibition.id!!)
    }

    /**
     * Creates new exhibition ContentVersion
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created exhibition ContentVersion
     */
    fun create(exhibitionId: UUID, payload: ExhibitionContentVersion): ExhibitionContentVersion {
        val result: ExhibitionContentVersion = this.getApi().createExhibitionContentVersion(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds exhibition ContentVersion
     *
     * @param exhibitionId exhibition id
     * @param exhibitionContentVersionId exhibition ContentVersion id
     * @return exhibition ContentVersion
     */
    fun findExhibitionContentVersion(exhibitionId: UUID, exhibitionContentVersionId: UUID): ExhibitionContentVersion? {
        return api.findExhibitionContentVersion(exhibitionId, exhibitionContentVersionId)
    }

    /**
     * Lists exhibition ContentVersions
     *
     * @param exhibitionId exhibition id
     * @return exhibition ContentVersions
     */
    fun listExhibitionContentVersions(exhibitionId: UUID): Array<ExhibitionContentVersion> {
        return api.listExhibitionContentVersions(exhibitionId)
    }

    /**
     * Updates exhibition ContentVersion
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated exhibition ContentVersion
     */
    fun updateExhibitionContentVersion(exhibitionId: UUID, body: ExhibitionContentVersion): ExhibitionContentVersion? {
        return api.updateExhibitionContentVersion(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a exhibitionContentVersion from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionContentVersion exhibitionContentVersion to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionContentVersion: ExhibitionContentVersion) {
        delete(exhibitionId, exhibitionContentVersion.id!!)
    }

    /**
     * Deletes a exhibitionContentVersion from the API
     *
     * @param exhibitionId exhibition id
     * @param exhibitionContentVersionId exhibitionContentVersion id to be deleted
     */
    fun delete(exhibitionId: UUID, exhibitionContentVersionId: UUID) {
        api.deleteExhibitionContentVersion(exhibitionId, exhibitionContentVersionId)
        removeCloseable { closable: Any ->
            if (closable !is ExhibitionContentVersion) {
                return@removeCloseable false
            }

            val closeableExhibitionContentVersion: ExhibitionContentVersion = closable
            closeableExhibitionContentVersion.id!!.equals(exhibitionContentVersionId)
        }
    }

    /**
     * Asserts exhibitionContentVersion count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     */
    fun assertCount(expected: Int, exhibitionId: UUID) {
        assertEquals(expected, api.listExhibitionContentVersions(exhibitionId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param exhibitionContentVersionId exhibitionContentVersion id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, exhibitionContentVersionId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, exhibitionContentVersionId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param exhibitionContentVersionId exhibitionContentVersion id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, exhibitionContentVersionId: UUID) {
        try {
            api.findExhibitionContentVersion(exhibitionId, exhibitionContentVersionId)
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
            api.listExhibitionContentVersions(exhibitionId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: ExhibitionContentVersion) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: ExhibitionContentVersion) {
        try {
            updateExhibitionContentVersion(exhibitionId, body)
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

    override fun clean(exhibitionContentVersion: ExhibitionContentVersion) {
        this.getApi().deleteExhibitionContentVersion(exhibitionContentVersion.exhibitionId!!, exhibitionContentVersion.id!!)
    }

    override fun getApi(): ExhibitionContentVersionsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ExhibitionContentVersionsApi(testBuilder.settings.apiBasePath)
    }

}
package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.VisitorSessionsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.VisitorSessionV2
import fi.metatavu.muisti.api.client.models.VisitorSessionState
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling visitorSessions
 */
class VisitorSessionV2TestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<VisitorSessionV2, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates new visitor session with default values
     *
     * @param exhibitionId
     * @return created visitor session
     */
    fun create(exhibitionId: UUID): VisitorSessionV2 {
        return create(exhibitionId, VisitorSessionV2(
            state = VisitorSessionState.aCTIVE,
            visitorIds = arrayOf(),
            variables = arrayOf(),
            language = "FI"
        ))
    }

    /**
     * Creates new visitor session
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created visitor session
     */
    fun create(exhibitionId: UUID, payload: VisitorSessionV2): VisitorSessionV2 {
        val result: VisitorSessionV2 = this.api.createVisitorSessionV2(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds visitor session
     *
     * @param exhibitionId exhibition id
     * @param visitorSessionId visitor session id
     * @return visitor session
     */
    fun findVisitorSession(exhibitionId: UUID, visitorSessionId: UUID): VisitorSessionV2? {
        return api.findVisitorSessionV2(exhibitionId, visitorSessionId)
    }

    /**
     * Lists visitor sessions
     *
     * @param exhibitionId exhibition id
     * @param tagId tag id
     * @param modifiedAfter modified after specified time
     * @return visitor sessions
     */
    fun listVisitorSessions(
        exhibitionId: UUID,
        tagId: String?,
        modifiedAfter: String?
    ): Array<VisitorSessionV2> {
        return api.listVisitorSessionsV2(
            exhibitionId = exhibitionId,
            tagId = tagId,
            modifiedAfter = modifiedAfter
        )
    }

    /**
     * Updates visitor session
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated visitor session
     */
    fun updateVisitorSession(exhibitionId: UUID, body: VisitorSessionV2): VisitorSessionV2 {
        return api.updateVisitorSessionV2(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a visitorSession from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorSession visitorSession to be deleted
     */
    fun delete(exhibitionId: UUID, visitorSession: VisitorSessionV2) {
        delete(exhibitionId, visitorSession.id!!)
    }

    /**
     * Deletes a visitorSession from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorSessionId visitorSession id to be deleted
     */
    fun delete(exhibitionId: UUID, visitorSessionId: UUID) {
        api.deleteVisitorSessionV2(exhibitionId, visitorSessionId)
        removeCloseable { closable: Any ->
            if (closable !is VisitorSessionV2) {
                return@removeCloseable false
            }

            val closeableVisitorSession: VisitorSessionV2 = closable
            closeableVisitorSession.id!! == visitorSessionId
        }
    }

    /**
     * Asserts visitorSession count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     * @param tagId filter by tag id
     * @param modifiedAfter modified after specified time
     */
    fun assertCount(expected: Int, exhibitionId: UUID, tagId: String?, modifiedAfter: String?) {
        assertEquals(expected, api.listVisitorSessionsV2(
            exhibitionId = exhibitionId,
            tagId = tagId,
            modifiedAfter = modifiedAfter
        ).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param visitorSessionId visitorSession id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, visitorSessionId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, visitorSessionId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param visitorSessionId visitorSession id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, visitorSessionId: UUID) {
        try {
            api.findVisitorSessionV2(exhibitionId, visitorSessionId)
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
     * @param tagId filter by tag id
     * @param modifiedAfter modified after specified time
     */
    fun assertListFail(
        expectedStatus: Int,
        exhibitionId: UUID,
        tagId: String?,
        modifiedAfter: String?
    ) {
        try {
            api.listVisitorSessionsV2(
                exhibitionId = exhibitionId,
                tagId = tagId,
                modifiedAfter = modifiedAfter
            )
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: VisitorSessionV2) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: VisitorSessionV2) {
        try {
            updateVisitorSession(exhibitionId, body)
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

    override fun clean(visitorSession: VisitorSessionV2) {
        this.api.deleteVisitorSessionV2(visitorSession.exhibitionId!!, visitorSession.id!!)
    }

    override fun getApi(): VisitorSessionsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return VisitorSessionsApi(testBuilder.settings.apiBasePath)
    }

}
package fi.metatavu.muisti.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.VisitorSessionsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.VisitorSession
import fi.metatavu.muisti.api.client.models.VisitorSessionState
import fi.metatavu.muisti.api.client.models.VisitorSessionUser
import fi.metatavu.muisti.api.client.models.VisitorSessionVariable
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling visitorSessions
 */
class VisitorSessionTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<VisitorSession, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new visitor session with default values
     *
     * @param exhibitionId
     * @return created visitor session
     */
    fun create(exhibitionId: UUID): VisitorSession {
        return create(exhibitionId, VisitorSessionState.aCTIVE, arrayOf<VisitorSessionUser>(), arrayOf<VisitorSessionVariable>())
    }

    /**
     * Creates new visitor session
     *
     * @param exhibitionId exhibition id
     * @param state state
     * @param users users
     * @param variables variables
     * @return created visitor session
     */
    fun create(exhibitionId: UUID, state: VisitorSessionState, users: Array<VisitorSessionUser>, variables: Array<VisitorSessionVariable>): VisitorSession {
        val payload = VisitorSession(state, users, null, exhibitionId, variables, exhibitionId)
        val result: VisitorSession = this.getApi().createVisitorSession(exhibitionId, payload)
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
    fun findVisitorSession(exhibitionId: UUID, visitorSessionId: UUID): VisitorSession? {
        return api.findVisitorSession(exhibitionId, visitorSessionId)
    }

    /**
     * Lists visitor sessions
     *
     * @param exhibitionId exhibition id
     * @return visitor sessions
     */
    fun listVisitorSessions(exhibitionId: UUID): Array<VisitorSession> {
        return api.listVisitorSessions(exhibitionId)
    }

    /**
     * Updates visitor session
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated visitor session
     */
    fun updateVisitorSession(exhibitionId: UUID, body: VisitorSession): VisitorSession? {
        return api.updateVisitorSession(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a visitorSession from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorSession visitorSession to be deleted
     */
    fun delete(exhibitionId: UUID, visitorSession: VisitorSession) {
        delete(exhibitionId, visitorSession.id!!)
    }

    /**
     * Deletes a visitorSession from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorSessionId visitorSession id to be deleted
     */
    fun delete(exhibitionId: UUID, visitorSessionId: UUID) {
        api.deleteVisitorSession(exhibitionId, visitorSessionId)
        removeCloseable { closable: Any ->
            if (closable !is VisitorSession) {
                return@removeCloseable false
            }

            val closeableVisitorSession: VisitorSession = closable
            closeableVisitorSession.id!!.equals(visitorSessionId)
        }
    }

    /**
     * Asserts visitorSession count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     */
    fun assertCount(expected: Int, exhibitionId: UUID) {
        assertEquals(expected, api.listVisitorSessions(exhibitionId).size)
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
            api.findVisitorSession(exhibitionId, visitorSessionId)
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
            api.listVisitorSessions(exhibitionId)
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
     * @param state update state
     * @param users update users
     * @param variables update variables
     */
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, state: VisitorSessionState, users: Array<VisitorSessionUser>, variables: Array<VisitorSessionVariable>) {
        try {
            create(exhibitionId, state, users, variables)
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: VisitorSession) {
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

    override fun clean(visitorSession: VisitorSession) {
        this.getApi().deleteVisitorSession(visitorSession.exhibitionId!!, visitorSession.id!!)
    }

    override fun getApi(): VisitorSessionsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return VisitorSessionsApi(testBuilder.settings.apiBasePath)
    }

}
package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.VisitorsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Visitor
import fi.metatavu.muisti.api.client.models.VisitorTag
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling visitors
 */
class VisitorTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<Visitor, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates new visitor session with default values
     *
     * @param exhibitionId
     * @return created visitor session
     */
    fun create(exhibitionId: UUID): Visitor {
        return create(exhibitionId, Visitor(
            email = "fake@exmaple.com",
            tagId = "faketag",
            language = "fi"
        ))
    }

    /**
     * Creates new visitor session
     *
     * @param exhibitionId exhibition id
     * @param payload payload
     * @return created visitor session
     */
    fun create(exhibitionId: UUID, payload: Visitor): Visitor {
        val result: Visitor = this.api.createVisitor(exhibitionId, payload)
        addClosable(result)
        return result
    }

    /**
     * Finds visitor session
     *
     * @param exhibitionId exhibition id
     * @param visitorId visitor session id
     * @return visitor session
     */
    fun findVisitor(exhibitionId: UUID, visitorId: UUID): Visitor? {
        return api.findVisitor(exhibitionId, visitorId)
    }

    /**
     * Finds visitor tag
     *
     * @param exhibitionId exhibition id
     * @param tagId tag id
     * @return visitor tag
     */
    fun findVisitorTag(exhibitionId: UUID, tagId: String): VisitorTag? {
        return api.findVisitorTag(exhibitionId = exhibitionId, tagId = tagId)
    }

    /**
     * Lists visitor sessions
     *
     * @param exhibitionId exhibition id
     * @param tagId tag id
     * @return visitor sessions
     */
    fun listVisitors(exhibitionId: UUID, tagId: String?): Array<Visitor> {
        return api.listVisitors(exhibitionId = exhibitionId, tagId = tagId)
    }

    /**
     * Updates visitor session
     *
     * @param exhibitionId exhibition id
     * @param body update body
     * @return updated visitor session
     */
    fun updateVisitor(exhibitionId: UUID, body: Visitor): Visitor? {
        return api.updateVisitor(exhibitionId, body.id!!, body)
    }

    /**
     * Deletes a visitor from the API
     *
     * @param exhibitionId exhibition id
     * @param visitor visitor to be deleted
     */
    fun delete(exhibitionId: UUID, visitor: Visitor) {
        delete(exhibitionId, visitor.id!!)
    }

    /**
     * Deletes a visitor from the API
     *
     * @param exhibitionId exhibition id
     * @param visitorId visitor id to be deleted
     */
    fun delete(exhibitionId: UUID, visitorId: UUID) {
        api.deleteVisitor(exhibitionId, visitorId)
        removeCloseable { closable: Any ->
            if (closable !is Visitor) {
                return@removeCloseable false
            }

            val closeableVisitor: Visitor = closable
            closeableVisitor.id!! == visitorId
        }
    }

    /**
     * Asserts visitor count within the system
     *
     * @param expected expected count
     * @param exhibitionId exhibition id
     * @param tagId filter by tag id
     */
    fun assertCount(expected: Int, exhibitionId: UUID, tagId: String?) {
        assertEquals(expected, api.listVisitors(exhibitionId = exhibitionId, tagId = tagId).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     * @param visitorId visitor id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID, visitorId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId, visitorId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param visitorId visitor id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID, visitorId: UUID) {
        try {
            api.findVisitor(exhibitionId, visitorId)
            fail(String.format("Expected find to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     * @param tagId tag id
     */
    fun assertFindVisitorFail(expectedStatus: Int, exhibitionId: UUID, tagId: String) {
        try {
            api.findVisitorTag(exhibitionId = exhibitionId, tagId = tagId)
            fail(String.format("Expected tag find to fail with message %d", expectedStatus))
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
     */
    fun assertListFail(expectedStatus: Int, exhibitionId: UUID, tagId: String?) {
        try {
            api.listVisitors(exhibitionId = exhibitionId, tagId = tagId)
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
    fun assertCreateFail(expectedStatus: Int, exhibitionId: UUID, payload: Visitor) {
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
    fun assertUpdateFail(expectedStatus: Int, exhibitionId: UUID, body: Visitor) {
        try {
            updateVisitor(exhibitionId, body)
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

    override fun clean(visitor: Visitor) {
        this.api.deleteVisitor(visitor.exhibitionId!!, visitor.id!!)
    }

    override fun getApi(): VisitorsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return VisitorsApi(testBuilder.settings.apiBasePath)
    }

}
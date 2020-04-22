package fi.metatavu.muisti.api.test.functional.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.ExhibitionsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Exhibition
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling exhibitions
 */
class ExhibitionsTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<Exhibition, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new exhibition with default values
     *
     * @return created exhibition
     */
    fun create(): Exhibition {
        return create("default exhibition")
    }

    /**
     * Creates exhibition
     *
     * @param name exhibition name
     * @return created exhibition
     */
    fun create(name: String): Exhibition {
        val payload = Exhibition(name)
        val result: Exhibition = this.getApi().createExhibition(payload)
        addClosable(result)
        return result
    }

    /**
     * Finds an exhibition
     *
     * @param exhibitionId exhibition id
     * @return found exhibition
     */
    fun findExhibition(exhibitionId: UUID): Exhibition? {
        return api.findExhibition(exhibitionId)
    }

    /**
     * Lists exhibitions
     *
     * @return found exhibitions
     */
    fun listExhibitions(): Array<Exhibition> {
        return api.listExhibitions()
    }

    /**
     * Updates a exhibition into the API
     *
     * @param body body payload
     */
    fun updateExhibition(body: Exhibition): Exhibition? {
        return api.updateExhibition(body.id!!, body)
    }

    /**
     * Deletes a exhibition from the API
     *
     * @param exhibition exhibition to be deleted
     */
    fun delete(exhibition: Exhibition) {
        delete(exhibition.id!!)
    }

    /**
     * Deletes a exhibition from the API
     *
     * @param exhibitionId exhibition id to be deleted
     */
    fun delete(exhibitionId: UUID) {
        api.deleteExhibition(exhibitionId)
        removeCloseable { closable: Any ->
            if (closable !is Exhibition) {
                return@removeCloseable false
            }

            val closeableExhibition: Exhibition = closable
            closeableExhibition.id!!.equals(exhibitionId)
        }
    }

    /**
     * Asserts exhibition count within the system
     */
    fun assertCount(expected: Int) {
        assertEquals(expected, api.listExhibitions().size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param exhibitionId exhibition id
     */
    fun assertFindFailStatus(expectedStatus: Int, exhibitionId: UUID) {
        assertFindFailStatus(expectedStatus, exhibitionId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param exhibitionId exhibition id
     */
    fun assertFindFail(expectedStatus: Int, exhibitionId: UUID) {
        try {
            api.findExhibition(exhibitionId)
            fail(String.format("Expected find to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts create fails with given status
     *
     * @param expectedStatus expected status
     * @param name name
     */
    fun assertCreateFail(expectedStatus: Int, name: String) {
        try {
            create(name)
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
    fun assertUpdateFail(expectedStatus: Int, body: Exhibition) {
        try {
            updateExhibition(body)
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

    override fun clean(exhibition: Exhibition) {
        this.getApi().deleteExhibition(exhibition.id!!)
    }

    override fun getApi(): ExhibitionsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return ExhibitionsApi(testBuilder.settings.apiBasePath)
    }

}
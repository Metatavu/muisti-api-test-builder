package fi.metatavu.muisti.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.SubLayoutsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.*
import fi.metatavu.muisti.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling sub layouts
 *
 * @author Jari Nykänen
 */
class SubLayoutTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<SubLayout, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates new sub layout with default values
     *
     * @return created sub layout
     */
    fun create(): SubLayout {
        val createdProperties = arrayOf(PageLayoutViewProperty("name", "true", PageLayoutViewPropertyType.boolean))
        val createdChildren = arrayOf(PageLayoutView("childid", PageLayoutWidgetType.button, arrayOf(), arrayOf()))
        val createdData = PageLayoutView("rootid", PageLayoutWidgetType.frameLayout, createdProperties, createdChildren)

        return create(SubLayout(
            name = "created name",
            data = createdData
        ))
    }

    /**
     * Creates new sub layout
     *
     * @param payload payload
     * @return created sub layout
     */
    fun create(payload: SubLayout): SubLayout {
        val result: SubLayout = this.api.createSubLayout(payload)
        addClosable(result)
        return result
    }

    /**
     * Finds sub layout
     *
     * @param subLayoutId sub layout
     * @return sub layout
     */
    fun findSubLayout(subLayoutId: UUID): SubLayout? {
        return api.findSubLayout(subLayoutId)
    }
    
    /**
     * Lists sub layouts
     *
     * @return sub layouts
     */
    fun listSubLayouts(): Array<SubLayout> {
        return api.listSubLayouts()
    }

    /**
     * Updates sub layout
     *
     * @param body update body
     * @return updated sub layout
     */
    fun updateSubLayout(body: SubLayout): SubLayout? {
        return api.updateSubLayout(body.id!!, body)
    }

    /**
     * Deletes a sub layout from the API
     *
     * @param subLayout sub layout to be deleted
     */
    fun delete(subLayout: SubLayout) {
        delete(subLayout.id!!)
    }

    /**
     * Deletes a sub layout from the API
     *
     * @param subLayoutId sub layout id to be deleted
     */
    fun delete(subLayoutId: UUID) {
        api.deleteSubLayout(subLayoutId)
        removeCloseable { closable: Any ->
            if (closable !is SubLayout) {
                return@removeCloseable false
            }

            val closeableSubLayout: SubLayout = closable
            closeableSubLayout.id!! == subLayoutId
        }
    }

    /**
     * Asserts sub layout count within the system
     *
     * @param expected expected count
     */
    fun assertCount(expected: Int) {
        assertEquals(expected, api.listSubLayouts().size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param subLayoutId subLayout id
     */
    fun assertFindFailStatus(expectedStatus: Int, subLayoutId: UUID) {
        assertFindFailStatus(expectedStatus, subLayoutId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param subLayoutId subLayout id
     */
    fun assertFindFail(expectedStatus: Int, subLayoutId: UUID) {
        try {
            api.findSubLayout(subLayoutId)
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
            api.listSubLayouts()
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
     */
    fun assertCreateFail(expectedStatus: Int, payload: SubLayout) {
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
    fun assertUpdateFail(expectedStatus: Int, body: SubLayout) {
        try {
            updateSubLayout(body)
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

    /**
     * Clean resources
     *
     * @param subLayout sub layout
     */
    override fun clean(subLayout: SubLayout) {
        this.api.deleteSubLayout(subLayout.id!!)
    }

    /**
     * Get sub layouts API
     *
     * @return sub layouts API
     */
    override fun getApi(): SubLayoutsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return SubLayoutsApi(testBuilder.settings.apiBasePath)
    }

}
package fi.metatavu.muisti.api.test.functional.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.muisti.api.client.apis.PageLayoutsApi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.*
import fi.metatavu.muisti.api.test.functional.TestBuilder
import fi.metatavu.muisti.api.test.functional.impl.ApiTestBuilderResource
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Test builder resource for handling pageLayouts
 */
class PageLayoutTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<PageLayout, ApiClient?>(testBuilder, apiClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates new exhibition page layout with default values
     *
     * @param deviceModel device model that is required for the PageLayout
     * @return created page layout
     */
    fun create(deviceModel: DeviceModel): PageLayout {
        val createdModelId = deviceModel.id!!
        val createdProperties = arrayOf(PageLayoutViewProperty("name", "true", PageLayoutViewPropertyType.boolean))
        val createdChildren = arrayOf(PageLayoutView("childid", "child", arrayOf(), arrayOf()))
        val createdData = PageLayoutView("rootid", "created widget", createdProperties, createdChildren)

        return create(PageLayout(
            name = "created name",
            data = createdData,
            thumbnailUrl = "http://example.com/thumbnail.png",
            screenOrientation = ScreenOrientation.portrait,
            modelId = createdModelId
        ))
    }

    /**
     * Creates new exhibition page layout
     *
     * @param payload payload
     * @return created exhibition page layout
     */
    fun create(payload: PageLayout): PageLayout {
        val result: PageLayout = this.getApi().createPageLayout(payload)
        addClosable(result)
        return result
    }

    /**
     * Finds exhibition page layout
     *
     * @param pageLayoutId exhibition page layout
     * @return exhibition page layout
     */
    fun findPageLayout(pageLayoutId: UUID): PageLayout? {
        return api.findPageLayout(pageLayoutId)
    }

    /**
     * Lists exhibition page layouts
     *
     * @return exhibition page layouts
     */
    fun listPageLayouts(deviceModelId: UUID?, screenOrientation: String?): Array<PageLayout> {
        return api.listPageLayouts(deviceModelId, screenOrientation)
    }


    /**
     * Lists exhibition page layouts
     *
     * @return exhibition page layouts
     */
    fun listPageLayouts(): Array<PageLayout> {
        return api.listPageLayouts(null, null)
    }

    /**
     * Updates exhibition page layout
     *
     * @param body update body
     * @return updated exhibition page layout
     */
    fun updatePageLayout(body: PageLayout): PageLayout? {
        return api.updatePageLayout(body.id!!, body)
    }

    /**
     * Deletes a page layout from the API
     *
     * @param pageLayout page layout to be deleted
     */
    fun delete(pageLayout: PageLayout) {
        delete(pageLayout.id!!)
    }

    /**
     * Deletes a page layout from the API
     *
     * @param pageLayoutId page layout id to be deleted
     */
    fun delete(pageLayoutId: UUID) {
        api.deletePageLayout(pageLayoutId)
        removeCloseable { closable: Any ->
            if (closable !is PageLayout) {
                return@removeCloseable false
            }

            val closeablePageLayout: PageLayout = closable
            closeablePageLayout.id!!.equals(pageLayoutId)
        }
    }

    /**
     * Asserts page layout count within the system
     *
     * @param expected expected count
     */
    fun assertCount(expected: Int) {
        assertEquals(expected, api.listPageLayouts(null, null).size)
    }

    /**
     * Asserts find fails with given status
     *
     * @param expectedStatus expected status code
     * @param pageLayoutId pageLayout id
     */
    fun assertFindFailStatus(expectedStatus: Int, pageLayoutId: UUID) {
        assertFindFailStatus(expectedStatus, pageLayoutId)
    }

    /**
     * Asserts find status fails with given status code
     *
     * @param expectedStatus expected status
     * @param pageLayoutId pageLayout id
     */
    fun assertFindFail(expectedStatus: Int, pageLayoutId: UUID) {
        try {
            api.findPageLayout(pageLayoutId)
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
    fun assertListFail(expectedStatus: Int, deviceModelId: UUID?, screenOrientation: String?) {
        try {
            api.listPageLayouts(deviceModelId, screenOrientation)
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
    fun assertCreateFail(expectedStatus: Int, payload: PageLayout) {
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
    fun assertUpdateFail(expectedStatus: Int, body: PageLayout) {
        try {
            updatePageLayout(body)
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

    override fun clean(pageLayout: PageLayout) {
        this.getApi().deletePageLayout(pageLayout.id!!)
    }

    override fun getApi(): PageLayoutsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return PageLayoutsApi(testBuilder.settings.apiBasePath)
    }

}
package fi.metatavu.muisti.api.test.functional.impl

import com.squareup.moshi.Moshi
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.client.infrastructure.ClientException
import fi.metatavu.muisti.api.client.models.Error
import fi.metatavu.muisti.api.test.functional.TestBuilder
import org.junit.Assert

/**
 * Abstract base class for API test resource builders
 */
abstract class ApiTestBuilderResource<T, A>(protected val testBuilder: TestBuilder, private val apiClient: ApiClient) : fi.metatavu.jaxrs.test.functional.builder.AbstractApiTestBuilderResource<T, A, ApiClient>(testBuilder) {

    /**
     * Returns API client
     *
     * @return API client
     */
    override fun getApiClient(): ApiClient {
        return apiClient
    }

    /**
     * Asserts that client exception has expected status code
     *
     * @param expectedStatus expected status code
     * @param e client exception
     */
    protected fun assertClientExceptionStatus(expectedStatus: Int, e: ClientException) {
        Assert.assertEquals(expectedStatus, getClientExceptionError(e)?.code)
    }

    /**
     * Returns an error response from client exception
     *
     * @param e client exception
     * @return an error response
     */
    protected fun getClientExceptionError(e: ClientException): Error? {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Error::class.java)
        val message = e.message ?: return null
        return jsonAdapter.fromJson(message)
    }
}
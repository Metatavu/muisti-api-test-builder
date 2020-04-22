package fi.metatavu.muisti.api.test.functional

import fi.metatavu.jaxrs.test.functional.builder.AbstractTestBuilder
import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.jaxrs.test.functional.builder.auth.AuthorizedTestBuilderAuthentication
import fi.metatavu.jaxrs.test.functional.builder.auth.KeycloakAccessTokenProvider
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.test.builder.Settings
import fi.metatavu.muisti.api.test.functional.auth.TestBuilderAuthentication
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * TestBuilder implementation
 *
 * @author Antti Leppä
 */
open class TestBuilder(val settings: Settings): AbstractTestBuilder<ApiClient> () {

    private val logger = LoggerFactory.getLogger(javaClass)

    private var admin: TestBuilderAuthentication? = null

    override fun createTestBuilderAuthentication(testBuilder: AbstractTestBuilder<ApiClient>, accessTokenProvider: AccessTokenProvider): AuthorizedTestBuilderAuthentication<ApiClient> {
        return TestBuilderAuthentication(this, accessTokenProvider)
    }

    /**
     * Returns admin authenticated authentication resource
     *
     * @return admin authenticated authentication resource
     * @throws IOException
     */
    @kotlin.jvm.Throws(IOException::class)
    fun admin(): TestBuilderAuthentication {
        if (admin == null) {
            val authServerUrl = settings.keycloakHost
            val realm = settings.keycloakRealm
            val clientId = settings.keycloakClientId
            val adminUser = settings.keycloakAdminUser
            val adminPassword = settings.keycloakAdminPass
            val clientSecret = settings.keycloakClientSecret

            admin = TestBuilderAuthentication(this, KeycloakAccessTokenProvider(authServerUrl, realm, clientId, adminUser, adminPassword, clientSecret))
        }

        return admin!!
    }

}
package fi.metatavu.muisti.api.test.builder

interface Settings {

    val keycloakHost: String

    /**
     * Returns API service base path
     */
    val apiBasePath: String

    /**
     * Returns API service base path
     */
    val filesBasePath: String

    /**
     * Returns Keycloak realm
     */
    val keycloakRealm: String

    /**
     * Returns Keycloak client id
     */
    val keycloakClientId: String

    /**
     * Returns Keycloak client secret
     */
    val keycloakClientSecret: String?

    /**
     * Returns Keycloak admin user
     */
    val keycloakAdminUser: String

    /**
     * Returns Keycloak admin password
     */
    val keycloakAdminPass: String
    
}
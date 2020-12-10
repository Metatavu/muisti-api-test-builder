package fi.metatavu.muisti.api.test.builder.auth

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.jaxrs.test.functional.builder.auth.AuthorizedTestBuilderAuthentication
import fi.metatavu.muisti.api.client.infrastructure.ApiClient
import fi.metatavu.muisti.api.test.builder.TestBuilder
import fi.metatavu.muisti.api.test.builder.builder.impl.DeviceModelTestBuilderResource
import fi.metatavu.muisti.api.test.builder.impl.*
import java.io.IOException

/**
 * Test builder authentication
 *
 * @author Antti Leppä
 * @author Jari Nykänen
 *
 * Constructor
 *
 * @param testBuilder test builder instance
 * @param accessTokenProvider access token provider
 */
class TestBuilderAuthentication(private val testBuilder: TestBuilder, accessTokenProvider: AccessTokenProvider) : AuthorizedTestBuilderAuthentication<ApiClient>(testBuilder, accessTokenProvider) {

  private var accessTokenProvider: AccessTokenProvider? = accessTokenProvider
  private var exhibitions: ExhibitionsTestBuilderResource? = null
  private var visitorSessions: VisitorSessionTestBuilderResource? = null
  private var visitors: VisitorTestBuilderResource? = null
  private var exhibitionRooms: ExhibitionRoomTestBuilderResource? = null
  private var exhibitionFloors: ExhibitionFloorTestBuilderResource? = null
  private var exhibitionDeviceGroups: ExhibitionDeviceGroupTestBuilderResource? = null
  private var deviceModels: DeviceModelTestBuilderResource? = null
  private var exhibitionDevices: ExhibitionDeviceTestBuilderResource? = null
  private var rfidAntennas: RfidAntennaTestBuilderResource? = null
  private var pageLayouts: PageLayoutTestBuilderResource? = null
  private var subLayouts: SubLayoutTestBuilderResource? = null
  private var exhibitionPages: ExhibitionPageTestBuilderResource? = null
  private var storedFiles: StoredFilesTestBuilderResource? = null
  private var contentVersions: ContentVersionTestBuilderResource? = null
  private var groupContentVersions: GroupContentVersionTestBuilderResource? = null
  private var visitorVariables: VisitorVariableTestBuilderResource? = null

  /**
   * Returns test builder resource for exhibitions
   *
   * @return test builder resource for exhibitions
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun exhibitions(): ExhibitionsTestBuilderResource {
    if (exhibitions == null) {
      exhibitions = ExhibitionsTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return exhibitions!!
  }

  /**
   * Returns test builder resource for visitorSessions
   *
   * @return test builder resource for visitorSessions
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun visitorSessions(): VisitorSessionTestBuilderResource {
    if (visitorSessions == null) {
      visitorSessions = VisitorSessionTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return visitorSessions!!
  }

  /**
   * Returns test builder resource for visitorVariables
   *
   * @return test builder resource for visitorVariables
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun visitorVariables(): VisitorVariableTestBuilderResource {
    if (visitorVariables == null) {
      visitorVariables = VisitorVariableTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return visitorVariables!!
  }

  /**
   * Returns test builder resource for visitors
   *
   * @return test builder resource for visitors
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun visitors(): VisitorTestBuilderResource {
    if (visitors == null) {
      visitors = VisitorTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return visitors!!
  }

  /**
   * Returns test builder resource for exhibitionRooms
   *
   * @return test builder resource for exhibitionRooms
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun exhibitionRooms(): ExhibitionRoomTestBuilderResource {
    if (exhibitionRooms == null) {
      exhibitionRooms = ExhibitionRoomTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return exhibitionRooms!!
  }

  /**
   * Returns test builder resource for exhibitionFloors
   *
   * @return test builder resource for exhibitionFloors
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun exhibitionFloors(): ExhibitionFloorTestBuilderResource {
    if (exhibitionFloors == null) {
      exhibitionFloors = ExhibitionFloorTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return exhibitionFloors!!
  }

  /**
   * Returns test builder resource for exhibitionDevices
   *
   * @return test builder resource for exhibitionDevices
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun exhibitionDevices(): ExhibitionDeviceTestBuilderResource {
    if (exhibitionDevices == null) {
      exhibitionDevices = ExhibitionDeviceTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return exhibitionDevices!!
  }

  /**
   * Returns test builder resource for RFID antennas
   *
   * @return test builder resource for RFID antennas
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun rfidAntennas(): RfidAntennaTestBuilderResource {
    if (rfidAntennas == null) {
      rfidAntennas = RfidAntennaTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return rfidAntennas!!
  }

  /**
   * Returns test builder resource for exhibitionGroups
   *
   * @return test builder resource for exhibitionGroups
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun exhibitionDeviceGroups(): ExhibitionDeviceGroupTestBuilderResource {
    if (exhibitionDeviceGroups == null) {
      exhibitionDeviceGroups = ExhibitionDeviceGroupTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return exhibitionDeviceGroups!!
  }

  /**
   * Returns test builder resource for exhibitionDevices
   *
   * @return test builder resource for exhibitionDevices
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun deviceModels(): DeviceModelTestBuilderResource {
    if (deviceModels == null) {
      deviceModels = DeviceModelTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return deviceModels!!
  }

  /**
   * Returns test builder resource for pageLayouts
   *
   * @return test builder resource for pageLayouts
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun pageLayouts(): PageLayoutTestBuilderResource {
    if (pageLayouts == null) {
      pageLayouts = PageLayoutTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return pageLayouts!!
  }

  /**
   * Returns test builder resource for sub layouts
   *
   * @return test builder resource for sub layouts
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun subLayouts(): SubLayoutTestBuilderResource {
    if (subLayouts == null) {
      subLayouts = SubLayoutTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return subLayouts!!
  }

  /**
   * Returns test builder resource for exhibitionPages
   *
   * @return test builder resource for exhibitionPages
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun exhibitionPages(): ExhibitionPageTestBuilderResource {
    if (exhibitionPages == null) {
      exhibitionPages = ExhibitionPageTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return exhibitionPages!!
  }

  /**
   * Returns test builder resource for content versions
   *
   * @return test builder resource for content versions
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun contentVersions(): ContentVersionTestBuilderResource {
    if (contentVersions == null) {
      contentVersions = ContentVersionTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return contentVersions!!
  }

  /**
   * Returns test builder resource for exhibitionGroups
   *
   * @return test builder resource for content versions
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun groupContentVersions(): GroupContentVersionTestBuilderResource {
    if (groupContentVersions == null) {
      groupContentVersions = GroupContentVersionTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return groupContentVersions!!
  }

  /**
   * Returns test builder resource for files
   *
   * @return test builder resource for files
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun files(): StoredFilesTestBuilderResource {
    if (storedFiles == null) {
      storedFiles = StoredFilesTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return storedFiles!!
  }

  /**
   * Creates a API client
   *
   * @param accessToken access token
   * @return API client
   */
  override fun createClient(accessToken: String): ApiClient {
    val result = ApiClient(testBuilder.settings.apiBasePath)
    ApiClient.accessToken = accessToken
    return result
  }

}
package fi.metatavu.muisti.api.test.functional

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import fi.metatavu.muisti.api.client.models.*
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONException
import org.junit.Assert
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.JSONCompareResult
import org.skyscreamer.jsonassert.comparator.CustomComparator

import java.io.IOException

/**
 * Abstract base class for functional tests
 *
 * @author Antti Leppä
 */
abstract class AbstractFunctionalTest {

    /**
     * Creates a default room and all required resources into given exhibition
     *
     * @param testBuilder test builder instance
     * @param exhibition exhibition
     * @return created room
     */
    protected fun createDefaultRoom(testBuilder: TestBuilder, exhibition: Exhibition): ExhibitionRoom {
        val floor = testBuilder.admin().exhibitionFloors().create(exhibition)
        return testBuilder.admin().exhibitionRooms().create(exhibition = exhibition, floor = floor)
    }

    /**
     * Creates a default device group and all required resources into given exhibition
     *
     * @param testBuilder test builder instance
     * @param exhibition exhibition
     * @return created device group
     */
    protected fun createDefaultDeviceGroup(testBuilder: TestBuilder, exhibition: Exhibition): ExhibitionDeviceGroup {
        val room = createDefaultRoom(testBuilder, exhibition)
        return testBuilder.admin().exhibitionDeviceGroups().create(exhibition = exhibition, room = room)
    }

    /**
     * Creates a default device and all required resources into given exhibition
     *
     * @param testBuilder test builder instance
     * @param exhibition exhibition
     * @return created device
     */
    protected fun createDefaultDevice(testBuilder: TestBuilder, exhibition: Exhibition): ExhibitionDevice {
        val group = createDefaultDeviceGroup(testBuilder, exhibition)
        val model = testBuilder.admin().deviceModels().create()
        return testBuilder.admin().exhibitionDevices().create(exhibition = exhibition, model = model, group = group)
    }

    /**
     * Creates a default page and all required resources into given exhibition
     *
     * @param testBuilder test builder instance
     * @param exhibition exhibition
     * @return created page
     */
    protected fun createDefaultPage(testBuilder: TestBuilder, exhibition: Exhibition): ExhibitionPage {
        val layout = testBuilder.admin().pageLayouts().create(testBuilder.admin().deviceModels().create())
        val contentVersion = testBuilder.admin().exhibitionContentVersions().create(exhibition)
        val device = createDefaultDevice(testBuilder, exhibition)
        return testBuilder.admin().exhibitionPages().create(
                exhibition = exhibition,
                layout = layout,
                contentVersion = contentVersion,
                device = device
        )
    }

    /**
     * Calculates contents md5 from a resource
     *
     * @param resourceName resource name
     * @return resource contents md5
     * @throws IOException thrown when file reading fails
     */
    @Throws(IOException::class)
    protected open fun getResourceMd5(resourceName: String?): String? {
        val classLoader = javaClass.classLoader
        classLoader.getResourceAsStream(resourceName).use { fileStream -> return DigestUtils.md5Hex(fileStream) }
    }

    /**
     * Asserts that actual object equals expected object when both are serialized into JSON
     *
     * @param expected expected
     * @param actual actual
     * @throws JSONException thrown when JSON serialization error occurs
     * @throws IOException thrown when IO Exception occurs
     */
    @Throws(IOException::class, JSONException::class)
    fun assertJsonsEqual(expected: Any?, actual: Any?) {
        val compareResult: JSONCompareResult? = jsonCompare(expected, actual)
        Assert.assertTrue(compareResult?.message, compareResult?.passed()?: false)
    }

    /**
     * Compares objects as serialized JSONs
     *
     * @param expected expected
     * @param actual actual
     * @return comparison result
     * @throws JSONException
     * @throws JsonProcessingException
     */
    @Throws(JSONException::class, JsonProcessingException::class)
    private fun jsonCompare(expected: Any?, actual: Any?): JSONCompareResult? {
        val customComparator = CustomComparator(JSONCompareMode.LENIENT)
        return JSONCompare.compareJSON(toJSONString(expected), toJSONString(actual), customComparator)
    }

    /**
     * Serializes an object into JSON
     *
     * @param object object
     * @return JSON string
     * @throws JsonProcessingException
     */
    @Throws(JsonProcessingException::class)
    private fun toJSONString(`object`: Any?): String? {
        return if (`object` == null) {
            null
        } else getObjectMapper().writeValueAsString(`object`)
    }

    /**
     * Returns object mapper with default modules and settings
     *
     * @return object mapper
     */
    private fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        return objectMapper
    }
}

package com.example.MVCSpring6

import com.example.MVCSpring6.dto.Address
import com.example.MVCSpring6.service.AddressRepository
import io.mockk.MockKAnnotations
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
internal class BookRESTControllerTest {
    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    private val headers: MultiValueMap<String, String> = LinkedMultiValueMap()

    init {
        MockKAnnotations.init(this)
    }

    @Before
    fun authInit() {
        headers.add("Cookie", "auth=1")
    }

    @Test
    fun testList() {
        val result1 = testRestTemplate.exchange(
            "/api/list",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            AddressRepository::class.java
        )
        println(result1)
        assertEquals(addressRepository, result1.body)
    }

    @Test
    fun testListQuery() {
        val result1 = testRestTemplate.exchange(
            "/api/list?query=Kukuevo",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            AddressRepository::class.java
        )
        println(result1.body)
//        var ads : ConcurrentHashMap<Int, Address> = ConcurrentHashMap(20, 10F, 130)
//            val temp = addressRepository.findAddressByQuery("Kukuevo")
//            temp.forEach{
//                ads[it.key]=it.value
//            }
//        assertEquals(ads, result1.body)
    }

    @Test
    fun testAdd(){
        val objEmp = "testAddressNew"
        val result1 = testRestTemplate.exchange(
            "/api/add",
            HttpMethod.POST,
            HttpEntity<Any>(objEmp,headers),
            AddressRepository::class.java
        )
        assertTrue(result1.statusCode.value()==200)
    }
    @Test
    fun testDelete(){
        val result1 = testRestTemplate.exchange(
            "/api/1/delete",
            HttpMethod.DELETE,
            HttpEntity<Any>(headers),
            AddressRepository::class.java
        )
        assertTrue(result1.statusCode.value()==200)
    }
    @Test
    fun testView(){
        val result1 = testRestTemplate.exchange(
            "/api/1/view",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            Address::class.java
        )
        assertEquals(Address("Kukuevo"),result1.body)
    }

    @Test
    fun testEdit(){
        val objEmp = "newEditedAddress"
        val result1 = testRestTemplate.exchange(
            "/api/1/edit",
            HttpMethod.PUT,
            HttpEntity<Any>(objEmp,headers),
            AddressRepository::class.java
        )
        assertTrue(result1.statusCode.value()==200)
    }


    @Test
    fun noAuthTest(){
        headers.add("Cookie","auth=fdsfsgsg")
        val result1 = testRestTemplate.exchange(
            "/api/list",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            String::class.java
        )
        var expected : String? = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <form method=\"post\">\n" +
                "        Username: <input type=\"text\" name=\"username\"/> <br/>\n" +
                "        Password: <input type=\"password\" name=\"password\"/> <br/>\n" +
                "        <input type=\"submit\" />\n" +
                "    </form>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n"
        var actual = result1.body?.replace("\n","")?.replace("\r","")
        expected = expected?.replace("\n","")?.replace("\r","")

        assertEquals(expected,actual)
    }
}
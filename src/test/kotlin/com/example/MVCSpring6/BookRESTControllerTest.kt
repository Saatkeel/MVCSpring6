package com.example.MVCSpring6

import com.example.MVCSpring6.dto.Address
import com.example.MVCSpring6.service.AddressRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
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


    fun getCookieForUser(username: String?, password: String?, loginUrl: String?): String? {
        val form: MultiValueMap<String, String> = LinkedMultiValueMap()
        form["username"] = username
        form["password"] = password
        val loginResponse = testRestTemplate.postForEntity(
            loginUrl,
            HttpEntity(form, HttpHeaders()),
            String::class.java
        )
        return loginResponse.headers["Set-Cookie"]!![0]
    }
//    @Test
//    fun whenGetUser_thenCorrect() {
//        val urlTest = "/api/list"
//        val cookie = getCookieForUser("admin", "admin", "/login")
//        val headers = HttpHeaders()
//        headers.add("Cookie", cookie)
//        val responseFromSecuredEndPoint = testRestTemplate.exchange(
//            urlTest, HttpMethod.GET, HttpEntity<Any>(headers),
//            String::class.java
//        )
//        println(responseFromSecuredEndPoint.body.toString())
//        println(responseFromSecuredEndPoint.statusCodeValue)
//    }

    @Before
    fun authInit() {
        addressRepository.getAddresses().put(1, Address("Kukuevo"))
        addressRepository.getAddresses().put(2, Address("Ulica pushkina"))
        addressRepository.getAddresses().put(3, Address("Dom Kolotushkina"))
    }

    @After
    fun resetDb() {
        addressRepository.deleteAll()
    }

    @Test
    fun testList() {
        val urlTest = "/api/list"
        val cookie = getCookieForUser("admin", "admin", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            AddressRepository::class.java
        )

        assertEquals(addressRepository.getAddresses()[0], result1.body!!.getAddresses()[0])
        assertEquals(addressRepository.getAddresses()[1], result1.body!!.getAddresses()[1])
        assertEquals(addressRepository.getAddresses()[2], result1.body!!.getAddresses()[2])
    }

    @Test
    fun testAdd() {
        val urlTest = "/api/add"
        val cookie = getCookieForUser("admin", "admin", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val objEmp = "testAddressNew"
        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.POST,
            HttpEntity<Any>(objEmp, headers),
            AddressRepository::class.java
        )

        assertTrue(result1.statusCode.value() == 200)
        assertEquals(Address("testAddressNew"), addressRepository.getAddresses()[4])
    }

    @Test
    fun testView() {
        val urlTest = "/api/1/view"
        val cookie = getCookieForUser("admin", "admin", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            Address::class.java
        )

        assertTrue(result1.statusCode.value() == 200)
        assertEquals(Address("Kukuevo"), result1.body)
    }

    @Test
    fun testEdit() {
        val urlTest = "/api/1/edit"
        val cookie = getCookieForUser("admin", "admin", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val objEmp = "newEditedAddress"
        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.PUT,
            HttpEntity<Any>(objEmp, headers),
            AddressRepository::class.java
        )

        assertTrue(result1.statusCode.value() == 200)
        assertTrue(addressRepository.getAddresses()[1]!!.address == "newEditedAddress")
    }

    @Test
    fun testDelete() {
        val urlTest = "/api/1/delete"
        val cookie = getCookieForUser("admin", "admin", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.DELETE,
            HttpEntity<Any>(headers),
            AddressRepository::class.java
        )

        assertTrue(result1.statusCodeValue == 200)
    }

    @Test
    fun testDeleteByApi() {
        val urlTest = "/api/1/delete"
        val cookie = getCookieForUser("api", "api", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.DELETE,
            HttpEntity<Any>(headers),
            AddressRepository::class.java
        )

        assertTrue(result1.statusCodeValue == 403)
    }

    @Test
    fun testListQuery() {
        val urlTest = "/api/list?query=Kukuevo"
        val cookie = getCookieForUser("admin", "admin", "/login")
        val headers = HttpHeaders()
        headers.add("Cookie", cookie)

        val result1 = testRestTemplate.exchange(
            urlTest,
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            String::class.java
        )
        assertTrue(result1.body.toString().contains("Kukuevo"))
    }
}
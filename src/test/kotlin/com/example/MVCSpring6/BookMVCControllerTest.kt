package com.example.MVCSpring6

import com.example.MVCSpring6.dto.Address
import com.example.MVCSpring6.filters.AuthFilter
import com.example.MVCSpring6.service.AddressRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.concurrent.ConcurrentHashMap

internal class BookMVCControllerTest {
    @MockK
    private lateinit var addressRepository: AddressRepository

    @InjectMockKs
    private lateinit var bookMVCController: BookMVCController

    @Autowired
    private lateinit var authFilter: AuthFilter

    init {
        MockKAnnotations.init(this)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun `setup`() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookMVCController)
            .build()
    }

    @Test
    fun `GET ListLoading`() {
        val book = ConcurrentHashMap<Int, Address>()
        book[1] = Address("test1")
        book[2] = Address("Testanika 2")
        every { addressRepository.getAddresses() } returns book
        mockMvc.get("/app/list") {

        }.andExpect {
            status().isOk
            view().name("listForm")
            model().attribute("items", book)
        }
    }

    @Test
    fun `GET ListLoadingWithQuery`() {
        val book = ConcurrentHashMap<Int, Address>()
        book[1] = Address("test1")
        every { addressRepository.findAddressByQuery("test1") } returns book
        mockMvc.get("/app/list?query=test1") {

        }.andExpect {
            status().isOk
            view().name("listForm")
            model().attribute("items", book)
        }
    }

    @Test
    fun `GET ListView`() {
        val book = ConcurrentHashMap<Int, Address>()
        book[1] = Address("hahahahaha")
        book[2] = Address("ohoohohohoho")
        book[3] = Address("xxvxcvzvzxb")
        book[4] = Address("afdafwfara")

        every { addressRepository.getAddresses() } returns book
        mockMvc.get("/app/4/view") {

        }.andExpect {
            status().isOk
            view().name("listForm")
            model().attribute("items", book[4])
        }
    }

    @Test
    fun `GET Add View`() {
        mockMvc.get("/app/add") {

        }.andExpect {
            status().isOk
            view().name("addForm")
        }
    }

    @Test
    fun `POST Add View`() {
        every { addressRepository.addAddress("newAddress") } returns true
        mockMvc.post("/app/add?address=newAddress") {

        }.andExpect {
            status().isOk
            view().name("addForm")
            model().attribute("action", "Успешно добавлен новый адрес")
        }
    }

    @Test
    fun `POST Add View error`() {
        every { addressRepository.addAddress("newAddress") } returns false
        mockMvc.post("/app/add?address=newAddress") {

        }.andExpect {
            status().isOk
            view().name("addForm")
            model().attribute("action", "Что-то пошло не так :(")
        }
    }

    @Test
    fun `GET Edit  View`() {
        mockMvc.get("/app/1/edit") {

        }.andExpect {
            status().isOk
            view().name("editForm")
            model().attribute("id", "1")
        }
    }

    @Test
    fun `POST Edit View`() {
        val id = 3
        val testAddress = "testAddress"
        every { addressRepository.updateAddress(testAddress, id) } returns true
        mockMvc.post("/app/$id/edit?address=$testAddress") {

        }
            .andExpect {
                status().isOk
                view().name("editForm")
                model().attribute("action", "Запись с номером $id изменена на $testAddress")
                model().attribute("id", id.toString())
            }
    }

    @Test
    fun `POST Edit View error`() {
        val id = 3
        val testAddress = "testAddress"
        every { addressRepository.updateAddress(testAddress, id) } returns false
        mockMvc.post("/app/$id/edit?address=$testAddress") {

        }.andExpect {
            status().isOk
            view().name("editForm")
            model().attribute("action", "Что-то пошло не так")
        }
    }

    @Test
    fun `POST Delete View`() {
        val id = 3
        every { addressRepository.deleteAddress(3) } returns true
        mockMvc.post("/app/$id/delete") {

        }.andExpect {
            status().isOk
            view().name("deleteForm")
            model().attribute("action", "Запись с номером $id успешно удалена")

        }
    }

    @Test
    fun `POST Delete View error`() {
        val id = 3
        every { addressRepository.deleteAddress(3) } returns false
        mockMvc.post("/app/$id/delete") {

        }.andExpect {
            status().isOk
            view().name("deleteForm")
            model().attribute("action", "Что-то пошло не так")
        }
    }


}
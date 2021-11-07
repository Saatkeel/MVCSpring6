package com.example.MVCSpring6.controller

import com.example.MVCSpring6.dto.Address
import com.example.MVCSpring6.service.AddressRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap


@RestController
@RequestMapping("/api")
//@EnableGlobalMethodSecurity(prePostEnabled=true)
internal class BookRESTController(@Autowired var addressRepository: AddressRepository) {

    @GetMapping("/list", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAddresses(@RequestParam(required = false) query: String?): ResponseEntity<ConcurrentHashMap<Int, Address>> {
        var ads: ConcurrentHashMap<Int, Address> = ConcurrentHashMap(20, 10F, 130)
        if (query != null) {
            val temp = addressRepository.findAddressByQuery(query)
            temp.forEach {
                ads[it.key] = it.value
            }
        } else ads = addressRepository.getAddresses()

        return ResponseEntity(ads, HttpStatus.OK)
    }

    @PostMapping("/add", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addAddress(@RequestBody address: String) {
        addressRepository.addAddress(address)
    }

    @GetMapping("/{id}/view", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAddress(@PathVariable("id") id: Int): ResponseEntity<Address> {
        val ad = addressRepository.getAddresses()[id]
        return ResponseEntity(ad, HttpStatus.OK)
    }

    @PutMapping("/{id}/edit", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateAddress(@PathVariable("id") id: Int, @RequestBody address: String) {
        addressRepository.updateAddress(address, id)
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}/delete", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteAddress(@PathVariable("id") id: Int) {
        addressRepository.deleteAddress(id)
    }
}
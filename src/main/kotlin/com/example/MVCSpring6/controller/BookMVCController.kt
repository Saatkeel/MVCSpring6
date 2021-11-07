package com.example.MVCSpring6.controller

import com.example.MVCSpring6.service.AddressRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/app")
class BookMVCController(@Autowired var addressRepository: AddressRepository) {

    @GetMapping("/list")
    fun getAddressList(
        model: Model,
        @RequestParam(required = false) query: String?
    ): String? {

        if (query == null) {
            val address = addressRepository.getAddresses()
            model.addAttribute("items", address)
        } else
            model.addAttribute("items", addressRepository.findAddressByQuery(query))
        return "listForm"
    }

    @GetMapping("/{id}/view")
    fun getAddressById(
        model: Model,
        @PathVariable id: String
    ): String? {
        val address = addressRepository.getAddresses()[id.toInt()]
        model.addAttribute("items", address)
        return "listForm"
    }

    @GetMapping("/add")
    fun addAddressGet(
        model: Model
    ): String? {
        return "addForm"
    }

    @PostMapping("/add")
    fun addAddressPost(
        model: Model,
        @RequestParam(required = true) address: String?
    ): String? {
        if (addressRepository.addAddress(address))
            model.addAttribute("action", "Успешно добавлен новый адрес")
        else
            model.addAttribute("action", "Что-то пошло не так :(")
        return "addForm"
    }

    @GetMapping("/{id}/edit")
    fun editAddressGet(
        model: Model,
        @PathVariable id: String
    ): String? {
        model.addAttribute("id", id)
        return "editForm"
    }

    @PostMapping("/{id}/edit")
    fun editAddress(
        model: Model,
        @RequestParam(required = true) address: String?,
        @PathVariable id: String
    ): String? {
        model.addAttribute("id", id)
        var result = "Что-то пошло не так"
        if (addressRepository.updateAddress(address, id.toInt()))
            result = "Запись с номером $id изменена на $address"

        model.addAttribute("action", result)
        return "editForm"
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/{id}/delete")
    fun deleteAddress(
        model: Model,
        @PathVariable id: String
    ): String? {
        var result = "Что-то пошло не так"
        if (addressRepository.deleteAddress(id.toInt()))
            result = "Запись с номером $id успешно удалена"
        model.addAttribute("action", result)
        return "deleteForm"
    }

}
package com.example.MVCSpring6.service

import com.example.MVCSpring6.dto.Address
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class AddressRepository {
    private val addressBook = ConcurrentHashMap<Int, Address>()
    private var nextId = 4

    init {
        addressBook[1] = Address("Kukuevo")
        addressBook[2] = Address("Ulica pushkina")
        addressBook[3] = Address("Dom Kolotushkina")
    }

    public fun getAddresses() : ConcurrentHashMap<Int, Address> = addressBook

    public fun findAddressByQuery(query : String) : Map<Int, Address>{
        return addressBook.filter { it.value.address.contains(query) }
    }

    public fun addAddress(address: String?) : Boolean{
        if(address!=null && address != "") {
            addressBook[nextId] = Address(address)
            nextId++
            return true
        }
        return false
    }

    public fun updateAddress(address: String?, id: Int) : Boolean {
        if(addressBook.containsKey(id) && address != null) {
                addressBook[id] = Address(address)
            return true
        }
        return false
    }

    public fun deleteAddress(id : Int) : Boolean {
        if(addressBook.containsKey(id)){
            addressBook.remove(id)
            return true
        }
            return false
    }

    public override fun toString(): String {
        var result = ""
        addressBook.forEach{
            result+= "Address number ${it.key}:${it.value.address}\n"
        }
        return result
    }

    public override fun equals(other: Any?): Boolean     =
        (other is AddressRepository)
            && addressBook == other.addressBook

    public fun deleteAll(){
        addressBook.remove(3)
        addressBook.remove(2)
        addressBook.remove(1)
    }
}
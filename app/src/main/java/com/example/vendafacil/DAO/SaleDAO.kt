package com.example.vendafacil.DAO

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vendafacil.helpers.CallbackAsync
import com.example.vendafacil.models.Sale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter
import java.util.*

object SaleDAO {

    fun buscarSale(callback: CallbackAsync<MutableList<Sale>>, faturado: Boolean){
        var dbReference = FirebaseFirestore.getInstance().collection("Sale").whereEqualTo("UserId", FirebaseAuth.getInstance().currentUser?.uid)
        dbReference.whereEqualTo("faturado", faturado).get()
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = mutableListOf<Sale>()
                GlobalScope.async {
                    for (document in task.result!!) {
                        val sale = Sale()
                        sale.idSale = document.id;
                        sale.name = document.data["nome"].toString()
                        sale.obs = document.data["observacao"].toString()
                        sale.valueSale = document.data["valor"].toString().toDouble()
                        sale.dataSale = document.data["data_criacao"].toString()
                        list.add(sale)
                    }

                    callback.onComplete(list)
                }
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun buscarSaleValorMensal(callback: CallbackAsync<MutableList<Sale>>, faturado: Boolean){
//
//        val calendar = Calendar.getInstance()
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1)
//
//        val calendar2 = Calendar.getInstance()
//        calendar.set(calendar2.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 31)
//
//
//        var dbReference = FirebaseFirestore.getInstance().collection("Sale")
//            .whereEqualTo("UserId", FirebaseAuth.getInstance().currentUser?.uid)
//
//        dbReference.whereEqualTo("faturado", faturado).get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val list = mutableListOf<Sale>()
//                    GlobalScope.async {
//                        for (document in task.result!!) {
//                            val sale = Sale()
//                            if(Calendar.parse(document.data["data_criacao"].toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy").)
//                            list.add(sale)
//                        }
//
//                        callback.onComplete(list)
//                    }
//                }
//            }
//    }


    fun updateSaleFaturado(idDocument: String, faturar: Boolean){
        FirebaseFirestore.getInstance().collection("Sale").document(idDocument).set(hashMapOf("faturado" to faturar), SetOptions.merge())
    }

    fun deleteSale(idDocument: String){
        FirebaseFirestore.getInstance().collection("Sale").document(idDocument).delete()
    }
}
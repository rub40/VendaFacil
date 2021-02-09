package com.example.vendafacil.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.vendafacil.BR
import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
class Sale() : BaseObservable(), Parcelable {

    @Bindable
    var name: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Bindable
    var idSale: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.idSale)
        }

    @Bindable
    var valueSale: Double = 0.0
        set(value){
            field = value
            notifyPropertyChanged(BR.valueSale)
        }

    @Bindable
    var dataSale: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.dataSale)
        }

    @Bindable
    var obs: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.obs)
        }

    constructor(parcel: android.os.Parcel) : this() {

    }

    override fun writeToParcel(dest: android.os.Parcel?, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sale> {
        override fun createFromParcel(parcel: android.os.Parcel): Sale {
            return Sale(parcel)
        }

        override fun newArray(size: Int): Array<Sale?> {
            return arrayOfNulls(size)
        }
    }


}
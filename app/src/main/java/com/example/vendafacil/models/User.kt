package com.example.vendafacil.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.vendafacil.BR
import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
class User() : BaseObservable(), Parcelable {

    @Bindable
    var name: String = ""
    set(value){
        field = value
        notifyPropertyChanged(BR.name)
    }

    @Bindable
    var surname: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.surname)
        }

    @Bindable
    var email: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.email)
        }

    @Bindable
    var password: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.password)
        }

    @Bindable
    var phone: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.phone)
        }



    constructor(parcel: android.os.Parcel) : this() {
    }

    override fun writeToParcel(p0: android.os.Parcel?, p1: Int) {

    }

    override fun describeContents(): Int {
        return 0;
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: android.os.Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

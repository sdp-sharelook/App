package com.github.sdpsharelook
import android.graphics.Bitmap
import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.reflect.KClass

class WordF(
    val source: String?,
    val sourceLanguage: String?,
    val target: String?,
    val targetLanguage: String?,
    val location: Location?,
    val savedDate: Date?,
    val picture: Bitmap?,
)
//    : Parcelable {
//
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readParcelable(Location::class.java.classLoader),
//        parcel.readParcelable(Date::class, java.classloader),
//        parcel.readParcelable(Bitmap::class.java.classLoader)
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(source)
//        parcel.writeString(target)
//        parcel.writeParcelable(location, flags)
//        parcel.writeParcelable(picture, flags)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<WordF> {
//        override fun createFromParcel(parcel: Parcel): WordF {
//            return WordF(parcel)
//        }
//
//        override fun newArray(size: Int): Array<WordF?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
//
//private fun Parcel.readParcelable(kClass: KClass<Date>, classloader: Any) {
//
//}

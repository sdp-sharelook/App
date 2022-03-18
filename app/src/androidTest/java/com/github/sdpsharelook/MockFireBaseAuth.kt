package com.github.sdpsharelook

import android.app.Activity
import android.net.Uri
import android.os.Parcel
import com.github.sdpsharelook.authorization.UserConstants.TEST_USER_EMAIL
import com.github.sdpsharelook.authorization.UserConstants.TEST_USER_PASS
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.concurrent.Executor

class MockFireBaseApp
class MockFireBaseAuth(p0: FirebaseApp) : FirebaseAuth(p0) {
    val email = "";
    val password = "";
    override fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        if (email != "" && password != "") return mockAuthResponse()
        val mr = mockAuthResponse()
        mr.fail()
        return mr

    }

    override fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        if (email != TEST_USER_EMAIL && password != TEST_USER_PASS) {
            return mockAuthResponse()
        } else {
            val mr = mockAuthResponse()
            mr.fail()
            return mr
        }
    }

}

class mockAuthResponse : Task<AuthResult>() {
    private var hasFailed = false
    fun fail() {
        hasFailed = true;
    }

    override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(
        p0: Activity,
        p1: OnSuccessListener<in AuthResult>
    ): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(
        p0: Executor,
        p1: OnSuccessListener<in AuthResult>
    ): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun getException(): Exception? {
        TODO("Not yet implemented")
    }

    override fun getResult(): AuthResult {
        TODO("Not yet implemented")
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isComplete(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isSuccessful(): Boolean {
        TODO("Not yet implemented")
    }

}

class authResult : AuthResult {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun getAdditionalUserInfo(): AdditionalUserInfo? {
        TODO("Not yet implemented")
    }

    override fun getCredential(): AuthCredential? {
        TODO("Not yet implemented")
    }

    override fun getUser(): FirebaseUser? {
        return fakeUser;
    }

}

object fakeUser : FirebaseUser() {
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun getPhotoUrl(): Uri? {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String? {
        return null
    }

    override fun getEmail(): String? {
        return TEST_USER_EMAIL
    }

    override fun getPhoneNumber(): String? {
        TODO("Not yet implemented")
    }

    override fun getProviderId(): String {
        TODO("Not yet implemented")
    }

    override fun getUid(): String {
        TODO("Not yet implemented")
    }

    override fun isEmailVerified(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMetadata(): FirebaseUserMetadata? {
        TODO("Not yet implemented")
    }

    override fun getMultiFactor(): MultiFactor {
        TODO("Not yet implemented")
    }

    override fun getTenantId(): String? {
        TODO("Not yet implemented")
    }

    override fun getProviderData(): MutableList<out UserInfo> {
        TODO("Not yet implemented")
    }

    override fun isAnonymous(): Boolean {
        TODO("Not yet implemented")
    }

    override fun zza(): FirebaseApp {
        TODO("Not yet implemented")
    }

    override fun zzb(): FirebaseUser {
        TODO("Not yet implemented")
    }

    override fun zzc(p0: MutableList<out UserInfo>): FirebaseUser {
        TODO("Not yet implemented")
    }

    override fun zzd(): com.google.android.gms.internal.`firebase-auth-api`.zzwq {
        TODO("Not yet implemented")
    }

    override fun zze(): String {
        TODO("Not yet implemented")
    }

    override fun zzf(): String {
        TODO("Not yet implemented")
    }

    override fun zzg(): MutableList<String>? {
        TODO("Not yet implemented")
    }

    override fun zzh(p0: com.google.android.gms.internal.`firebase-auth-api`.zzwq) {
        TODO("Not yet implemented")
    }

    override fun zzi(p0: MutableList<MultiFactorInfo>) {
        TODO("Not yet implemented")
    }

}
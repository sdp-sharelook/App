package com.github.sdpsharelook

import com.github.sdpsharelook.authorization.AuthProvider
import com.github.sdpsharelook.authorization.TestAuth
import com.github.sdpsharelook.di.AuthBindsModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AuthBindsModule::class]
)
abstract class FakeAuthModuleUnitTests {
    @Binds
    @Singleton
    abstract fun provideAuthProvider(r: TestAuth): AuthProvider
}
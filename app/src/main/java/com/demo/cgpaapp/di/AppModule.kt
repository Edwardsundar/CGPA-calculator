package com.demo.cgpaapp.di

import com.demo.cgpaapp.data.CGPARepositoryImp
import com.demo.cgpaapp.domain.CGPARepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCGPARepository(): CGPARepository{
        return CGPARepositoryImp()
    }

}
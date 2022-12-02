package com.manriquetavi.di

import com.manriquetavi.data.repository.UserDataSourceImpl
import com.manriquetavi.domain.repository.UserDataSource
import com.manriquetavi.util.Constants.DATABASE_NAME
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        KMongo
            .createClient(System.getenv("MONGODB_URI"))
            .coroutine
            .getDatabase(DATABASE_NAME)
    }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }
}
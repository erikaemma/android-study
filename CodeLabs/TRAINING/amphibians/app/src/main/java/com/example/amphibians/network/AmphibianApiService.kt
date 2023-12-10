/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amphibians.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// 存储API的基准网址
private const val BASE_URL = "https://developer.android.com/courses/pathways/android-basics-kotlin-unit-4-pathway-2/"

// 使用Kotlin适配器工厂构建Moshi对象，Retrofit将用其解析JSON
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// 使用Moshi转换器构建Retrofit实例
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AmphibianApiService {
    // 获取两栖动物列表的方法
    @GET("android-basics-kotlin-unit-4-pathway-2-project-api.json")
    suspend fun getAmphibians(): List<Amphibian>
}

// 创建一个AmphibianApi对象，用于公开使用AmphibianApiService接口的延迟初始化Retrofit服务
object AmphibianApi {
    val retrofitService: AmphibianApiService by lazy {
        retrofit.create(AmphibianApiService::class.java)
    }
}
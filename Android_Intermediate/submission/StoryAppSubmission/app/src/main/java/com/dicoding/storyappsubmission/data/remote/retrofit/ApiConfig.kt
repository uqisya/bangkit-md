package com.dicoding.storyappsubmission.data.remote.retrofit

import com.dicoding.storyappsubmission.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private fun createLoggingInterceptor(): Interceptor {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            return loggingInterceptor
        }

        private fun createOkHttpClient(interceptors: List<Interceptor>): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(createLoggingInterceptor())
                interceptors.forEach { itemInterceptor ->
                    addInterceptor(itemInterceptor)
                }
            }.build()
        }

        private fun createRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        fun getApiService(): ApiService {
            val client = createOkHttpClient(emptyList())
            val retrofit = createRetrofit(client)

            return retrofit.create(ApiService::class.java)
        }

        fun getApiService(userAuthToken: String): ApiService {
            // tambah interceptor untuk auth
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer $userAuthToken"
                    )  // dapetin $userAuthToken nya dari injection
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = createOkHttpClient(listOf(authInterceptor))
            val retrofit = createRetrofit(client)

            return retrofit.create(ApiService::class.java)
        }
    }
}
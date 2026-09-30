package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    // Supported modern models per gemini-api guidelines
    const val PRIMARY_MODEL = "gemini-2.5-flash"
    const val FALLBACK_MODEL = "gemini-3.1-flash-lite-preview"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    class QuotaExceededException(message: String) : Exception(message)

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun getApiKey(customKey: String?): String {
        if (!customKey.isNullOrBlank()) return customKey.trim()
        val buildKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }
        return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
    }

    suspend fun generateContent(
        prompt: String,
        apiKey: String,
        systemInstruction: String? = null,
        modelName: String = PRIMARY_MODEL
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("No Gemini API key available"))
        }

        try {
            val rootJson = JSONObject()
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()

            val textPart = JSONObject().put("text", prompt)
            partsArray.put(textPart)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            rootJson.put("contents", contentsArray)

            // Optional system instruction
            if (!systemInstruction.isNullOrBlank()) {
                val sysContent = JSONObject()
                val sysParts = JSONArray()
                sysParts.put(JSONObject().put("text", systemInstruction))
                sysContent.put("parts", sysParts)
                rootJson.put("systemInstruction", sysContent)
            }

            // Generation config
            val genConfig = JSONObject()
            genConfig.put("temperature", 0.4)
            genConfig.put("topP", 0.95)
            genConfig.put("responseMimeType", "application/json")
            rootJson.put("generationConfig", genConfig)

            val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
            val url = "$BASE_URL/$modelName:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini API error ($modelName): ${response.code} $responseBody")
                val isQuotaExhausted = response.code == 429 ||
                    responseBody.contains("RESOURCE_EXHAUSTED", ignoreCase = true) ||
                    responseBody.contains("quota", ignoreCase = true)

                if (isQuotaExhausted) {
                    Log.w(TAG, "Gemini Quota Exceeded (HTTP ${response.code} / RESOURCE_EXHAUSTED).")
                    return@withContext Result.failure(QuotaExceededException("Quota exceeded: $responseBody"))
                }

                if (modelName == PRIMARY_MODEL) {
                    Log.i(TAG, "Retrying with fallback model $FALLBACK_MODEL")
                    return@withContext generateContent(prompt, apiKey, systemInstruction, FALLBACK_MODEL)
                }
                return@withContext Result.failure(Exception("HTTP ${response.code}: $responseBody"))
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) {
                return@withContext Result.failure(Exception("Empty candidates in response"))
            }

            val candidate = candidates.getJSONObject(0)
            val content = candidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (text.isNullOrBlank()) {
                return@withContext Result.failure(Exception("Empty text in Gemini response candidate"))
            }

            Result.success(text)
        } catch (e: Exception) {
            Log.e(TAG, "Failed calling Gemini API", e)
            Result.failure(e)
        }
    }
}

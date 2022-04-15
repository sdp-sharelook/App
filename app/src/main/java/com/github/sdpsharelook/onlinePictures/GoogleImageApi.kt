package com.github.sdpsharelook.onlinePictures

import android.os.Build
import com.github.sdpsharelook.language.Language
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// custom search api :


/**
 * Implements the GoogleImageApi.
 * Try it here https://developers.google.com/custom-search/v1/reference/rest/v1/cse/list
 */
class GoogleImageApi {
    companion object {
        private val api_endpoint = "https://www.googleapis.com/customsearch/v1"

        // key = AIzaSyCZAkH_vbNiB8QeyFs9yFp6AUR2eyW0uus
        private val api_key = "AIzaSyCZAkH_vbNiB8QeyFs9yFp6AUR2eyW0uus"

        // custom search engine : 82269d15153934931 (https://cse.google.com/cse?cx=82269d15153934931)
        private val cx = "82269d15153934931"
        private val validCharsURL =
            (('A'..'Z') + ('a'..'z') + ('0'..'9')).toSet() + setOf('%', '~', '_', '.')

        private fun sanitizeURL(url: String) =
            url.map {
                if (validCharsURL.contains(it)) it
                else "%${it.code.toString(16)}"
            }.joinToString("")

        /**
         * must be launched on Dispatchers.IO
         */
        suspend fun search(
            keyword: String,
            language: Language,
        ): List<OnlinePicture>? {
            val parameter = mutableMapOf(
                "q" to sanitizeURL(keyword),
                "num" to "10",
                "start" to "0",
                "key" to api_key,
                "searchType" to "image",
                "filetype" to "png",
                "imgSize" to "MEDIUM",
                "cx" to cx,
                //"safe" to "active",
                //"imgType" to "clipart"
            )
            language.locale?.let { parameter += mapOf("hl" to language.tag) }
            try {
                val params = parameter.map { "${it.key}=${it.value}" }.joinToString("&")
                val url = URL("$api_endpoint?$params")
                println(url)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.addRequestProperty("Content-Type", "application/json")
                val s =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        BufferedReader(InputStreamReader(connection.inputStream)).lines().toArray()
                            .joinToString("\n")
                    else
                        null

                val jsonResults = JSONTokener(s).nextValue() as JSONObject
                val jsonItems = jsonResults.getJSONArray("items")
                return (0..jsonItems.length() - 1).map {
                    jsonItems.getJSONObject(it) as JSONObject
                }.map {
                    val thumbnailLink = it.getJSONObject("image").getString("thumbnailLink")
                    val title = it.getString("title")
                    val link = it.getString("link")
                    OnlinePicture(thumbnailLink, link, title)
                }.toList()
            } catch (e: Throwable) {
                return null
            }
        }

    }

    enum class Size(apiString: String) {

    }
}
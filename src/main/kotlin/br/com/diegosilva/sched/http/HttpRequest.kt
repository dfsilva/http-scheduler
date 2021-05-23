package br.com.diegosilva.sched.http

import java.io.DataOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


object HttpRequest {

    fun request(
        urlStr: String,
        method: String,
        params: Map<String, Any>? = null,
        body: String? = null,
        header: Map<String, String>? = null
    ): String {

        val url = URL(urlStr)

        var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.doOutput = true
        connection.doInput = true
        connection.instanceFollowRedirects = false
        connection.requestMethod = method

        header?.let {
            it.entries.forEach { (key, value) -> connection.setRequestProperty(key, value) }
        }

        connection.useCaches = false
        connection.connect()

        params?.let {
            val wr = DataOutputStream(connection.outputStream)
            wr.writeBytes(createParams(it))
            wr.flush()
            wr.close()
        }

        body?.let {
            val bodyBytes = it.toByteArray(charset("UTF-8"))
            val os: OutputStream = connection.outputStream
            os.write(bodyBytes)
            os.close()
        }

        return if (connection.responseCode === HttpURLConnection.HTTP_OK
            || connection.responseCode === HttpURLConnection.HTTP_CREATED
        ) {
            connection.inputStream.bufferedReader().readText()
        } else {
            connection.inputStream?.let {
                throw RuntimeException(it.bufferedReader().readText())
            }
            throw RuntimeException("Error calling service $urlStr code ${connection.responseCode}")
        }
    }

    private fun createParams(paramsMap: Map<String, Any>): String {
        return "?" + paramsMap.entries
            .map { "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value.toString(), "UTF-8")}" }
            .reduce { acc, str -> "$acc&$str" }
    }
}
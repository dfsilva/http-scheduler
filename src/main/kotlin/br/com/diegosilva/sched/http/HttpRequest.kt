package br.com.diegosilva.sched.http

import java.io.*
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


object HttpRequest {

    fun request(urlStr: String,
                method: String,
                params: Map<String, Any>? = null,
                body: String? = null,
                header: Map<String, String>? = null): String {

        val url = URL(urlStr)

        var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.doOutput = true
        connection.doInput = true
        connection.instanceFollowRedirects = false
        connection.requestMethod = method

        header?.entries?.forEach { (key, value)-> connection.setRequestProperty(key, value)}

        connection.useCaches = false
        connection.connect()

        params?.let {
            val wr = DataOutputStream(connection.outputStream)
            wr.writeBytes(createParams(it))
            wr.flush()
            wr.close()
        }

        if (body != null) {
            val bodyBytes = body.toByteArray(charset("UTF-8"))
            val os: OutputStream = connection.outputStream
            os.write(bodyBytes)
            os.close()
        }

        return if (connection.responseCode === HttpURLConnection.HTTP_OK
                || connection.responseCode === HttpURLConnection.HTTP_CREATED) {
            getStringFromInputStream(connection.inputStream)
        } else {
            throw RuntimeException(getStringFromInputStream(connection.inputStream))
        }
    }

    private fun createParams(paramsMap: Map<String, Any>): String {
        return "?" + paramsMap.entries
                .map { "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value.toString(), "UTF-8")}" }
                .reduce { acc, str -> "$acc&$str" }
    }

    private fun getStringFromInputStream(stream: InputStream): String {
        var br: BufferedReader? = null
        val sb = StringBuilder()
        var line = ""
        try {
            if (stream != null) {
                br = BufferedReader(InputStreamReader(stream))
                while (br?.readLine() != null) {
                    sb.append(line)
                }
            }
        } catch (e: IOException) {
            throw e
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    throw e
                }
            }
        }
        return sb.toString()
    }
}
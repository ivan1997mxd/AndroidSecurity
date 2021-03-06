package com.tongche.androidsecurity.server

import android.os.AsyncTask
import android.os.FileUtils
import android.util.Base64
import android.util.Log
import com.tongche.androidsecurity.Tags
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Server : AsyncTask<String?, Void?, String?>() {
    private var asyncResponse: AsyncResponse? = null
    fun setOnAsyncResponse(asyncResponse: AsyncResponse?) {
        this.asyncResponse = asyncResponse
    }

    companion object {
        /*
        The IP Addresses stored in URL_STRING are temporary.
        When a proper server is set up, the IP Addresses will be changed to the server address.
     */
        private const val URL_STRING = "http://192.168.1.71:5000/upload" // To save test data
//        private const val URL_STRING = "http://192.168.1.99:5000/find_location" // To find test data

        private const val METHOD = "PUT" // Server is set to respond only to PUT requests.
    }

    /**
     * This method is specifically used by the WifiReceiver object.
     * Because the WifiReceiver only needs to send a JSONObject and no other data,
     * this method provides that signature.
     *
     * @param jObject the JSON object to be sent to the server.
     */
    // TODO: Generalize this in the future?
    fun execute(jObject: JSONObject, file_string: String) {
        super.execute(URL_STRING, jObject.toString(), file_string)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.i(Tags.SERVER, "Response: $result")
        if (result != null) {
            asyncResponse?.onDataReceivedSuccess(result)
        } else {
            asyncResponse?.onDataReceivedFailed()
        }
    }

    override fun doInBackground(vararg params: String?): String {
        Log.i(
            Tags.SERVER,
            "Starting to send a request to " + URL_STRING
        )
        val stringBuilder = StringBuilder()
//        Log.i(Tags.SERVER, "PutData=" + params.get(1).toString())
        Log.i(Tags.SERVER, "File=" + params.get(2).toString())
        var conn: HttpURLConnection? = null
        try {
            conn = URL(params.get(0)).openConnection() as HttpURLConnection
            conn.requestMethod = METHOD
            conn.doOutput = true
            val put_data = params.get(1)
            val file_name = params.get(2)
            val file = File(file_name)
            val pathToFile: Path = Paths.get(file_name)
            System.out.println(pathToFile.toAbsolutePath())
            val bytes: ByteArray = Files.readAllBytes(file.toPath())
            val file_data = Base64.encode(bytes,0)
            val wr = DataOutputStream(conn.outputStream)
            wr.writeBytes("PutData=$put_data")
            wr.flush()
            wr.close()
            val `in` = conn.inputStream
            val inputStreamReader = InputStreamReader(`in`)
            var inputStreamData = inputStreamReader.read()
            while (inputStreamData != -1) {
                val current = inputStreamData.toChar()
                inputStreamData = inputStreamReader.read()
                stringBuilder.append(current)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            conn?.disconnect()
        }
        Log.i(Tags.SERVER, "Request sent, and response received.")
        return stringBuilder.toString()
    }


}

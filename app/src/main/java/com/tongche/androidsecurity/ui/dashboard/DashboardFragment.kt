package com.tongche.androidsecurity.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mongodb.ConnectionString
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.genericvpnpolicy.NpaDbService


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val PROFILE_NAME = "testing"
    private val DEVICE_ADMIN_ADD_RESULT_ENABLE = 1
    private val KPE_LICENSE_KEY = "KLM06-0IEHL-GN7OE-RPLDA-2ZZJX-YRLGI"
    private var npaDbService = NpaDbService label@{
        return@label ConnectionString("mongodb://192.168.1.71:27017")
    }
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        val root: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    private fun pickFile() {
//        Intent(Intent.ACTION_GET_CONTENT).also {
//            it.type = "*/*"
//            startForResult.launch(Intent.createChooser(it, "Choose File to Upload"))
//        }
//    }
//
//    var startForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val file_uri = result.data?.data
//                val files = MediaStore.Files()
//                val url_name = getFileMetaData(requireContext(), file_uri!!)
//
//                file_name = "/Download/" + url_name!!.displayName.toString()
//                Log.e("filename: ", file_name)
//            }
//        }
//
//    private fun sendToServer() {
//        val sendObject = JSONObject()
//        try {
//            sendObject.put("Time", System.currentTimeMillis())
//            sendObject.put("Device_ID", "Samsung S20")
//            val file = File(file_name)
//            sendObject.put("File", file_name)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        val server = Server()
//        server.execute(sendObject, file_name)
//        server.setOnAsyncResponse(object : AsyncResponse {
//            override fun onDataReceivedFailed() {
//                Log.i(Tags.SERVER, "Response receive failed")
//            }
//
//            override fun onDataReceivedSuccess(result: String?) {
//                Log.i(Tags.SERVER, "Response received$result")
//            }
//        })
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    class FileMetaData {
//        var displayName: String? = null
//        var size: Long = 0
//        var mimeType: String? = null
//        var path: String? = null
//        override fun toString(): String {
//            return "name : $displayName ; size : $size ; path : $path ; mime : $mimeType"
//        }
//    }
//
//
//    fun getFileMetaData(context: Context, uri: Uri): FileMetaData? {
//        val fileMetaData = FileMetaData()
//        return if ("file".equals(uri.scheme, ignoreCase = true)) {
//            val file = File(uri.path)
//            fileMetaData.displayName = file.name
//            fileMetaData.size = file.length()
//            fileMetaData.path = file.path
//            fileMetaData
//        } else {
//            val contentResolver: ContentResolver = context.getContentResolver()
//            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
//            fileMetaData.mimeType = contentResolver.getType(uri)
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    val sizeIndex: Int = cursor.getColumnIndex(OpenableColumns.SIZE)
//                    fileMetaData.displayName =
//                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                    if (!cursor.isNull(sizeIndex)) fileMetaData.size =
//                        cursor.getLong(sizeIndex) else fileMetaData.size = -1
//                    try {
////                        val uri_list = uri.toString().split("/").subList(0,4)
////                        val path_string = uri_list.joinToString("/") + "/" + fileMetaData.displayName
////                        val path_string = "/document/" + fileMetaData.displayName
//                        fileMetaData.path = uri.path.toString()
////                        fileMetaData.path = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
//                    } catch (e: Exception) {
//                        // DO NOTHING, _data does not exist
//                    }
//                    return fileMetaData
//                }
//            } catch (e: Exception) {
//                Log.e(Tags.ERROR, e.toString())
//            } finally {
//                if (cursor != null) cursor.close()
//            }
//            null
//        }
//    }
}


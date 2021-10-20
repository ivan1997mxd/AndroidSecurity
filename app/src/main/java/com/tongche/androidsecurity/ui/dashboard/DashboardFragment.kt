package com.tongche.androidsecurity.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.content.ContentResolver
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tongche.androidsecurity.R
import com.tongche.androidsecurity.Tags
import com.tongche.androidsecurity.databinding.FragmentDashboardBinding
import com.tongche.androidsecurity.server.AsyncResponse
import com.tongche.androidsecurity.server.Server
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private var selectedImageUri: Uri? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val upload_button = view.findViewById<Button>(R.id.upload_button)
        upload_button.setOnClickListener { sendToServer() }
        val file_select = view.findViewById<ImageView>(R.id.image_view)
        file_select.setOnClickListener { pickFile() }
    }

    private fun pickFile() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "*/*"
            startForResult.launch(Intent.createChooser(it, "Choose File to Upload"))
        }
    }

    var startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                Log.e("data",selectedImageUri.toString())
            }
        }

    private fun sendToServer(){
        Log.e(Tags.SERVER, selectedImageUri.toString())
        val sendObject = JSONObject()
        try {
            sendObject.put("Time", System.currentTimeMillis())
            sendObject.put("Device_ID", "Samsung S20")
            val file = File(selectedImageUri.toString())
            sendObject.put("File", file.name)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val server = Server()
        server.execute(sendObject)
        server.setOnAsyncResponse(object : AsyncResponse{
            override fun onDataReceivedFailed() {
                Log.i(Tags.SERVER, "Response receive failed")
            }

            override fun onDataReceivedSuccess(result: String?) {
                Log.i(Tags.SERVER, "Response received$result")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
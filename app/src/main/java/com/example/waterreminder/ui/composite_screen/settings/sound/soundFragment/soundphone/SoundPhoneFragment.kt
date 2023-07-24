package com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundphone

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.content.ContentResolver
import android.content.Context
import android.content.IntentFilter
import android.database.Cursor
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterreminder.R
import com.example.waterreminder.database.sound.SoundEntity
import com.example.waterreminder.databinding.LayoutSoundPhoneBinding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SOUND
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class SoundPhoneFragment : Fragment() {
    private val viewModel:SoundPhoneViewModel by viewModels()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var adapter: SoundPhoneAdapter


    private lateinit var binding: LayoutSoundPhoneBinding

    companion object {
        const val REQUEST_CODE_PICK_MP3 = 1001
        const val LEGACY_STORAGE_PERMISSION_REQUEST_CODE = 1002
    }

    private val galleryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_MEDIA_MOUNTED || intent?.action == Intent.ACTION_MEDIA_SCANNER_SCAN_FILE) {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutSoundPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setListener()
        observe()
    }


    private fun initAdapter() {


        val sounds = mutableListOf(
            SoundEntity("More","1",123,1),
            SoundEntity("Classics", "2",R.raw.classic,1),
            SoundEntity("Drop echos","3",R.raw.drop_echo,1),
            SoundEntity("Water drops","4",R.raw.water_drop,1),
            SoundEntity("Water flows", "5",R.raw.water_flow,1)
        )

        for (sound in sounds) viewModel.insertSound(sound)


    }

    private fun observe(){
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_MEDIA_MOUNTED)
            addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        }
        requireContext().registerReceiver(galleryReceiver, filter)


        val layoutManager = LinearLayoutManager(requireContext())
        binding.rclSoundApp.layoutManager = layoutManager
        binding.rclSoundApp.adapter = adapter

        viewModel.getSound()
        viewModel.listSound.observe(viewLifecycleOwner){ it ->
            adapter.submitList(it)
            
            val selected =  viewModel.listSound.value?.find {it.isCheck && it.isCheckRaw == 0}
            if (selected != null) {
                preferenceHelper.setString(PREF_SOUND,selected.file)
            }
            val selected2 =  viewModel.listSound.value?.find {it.isCheck && it.isCheckRaw == 1}
            if (selected2 != null) {
                preferenceHelper.setString(PREF_SOUND,selected2.rawId.toString())
            }

        }

       

    }


    private fun setListener() {
        adapter.clickListener.onItemClickMore = { soundPhone ->
            for (item in adapter.currentList) {
                item.isCheck = item.name == soundPhone.name
                viewModel.updateSound(item)
            }
            adapter.notifyDataSetChanged()
            if(soundPhone.name == "More"){
                requestPermission()
            }
        }
        adapter.clickListener.onItemClick = { soundPhone ->
            for (item in adapter.currentList) {
                item.isCheck = item.name == soundPhone.name
                viewModel.updateSound(item)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storageActivityResultLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        } else {

            requestPermissions(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE),
                LEGACY_STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private val storageActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/mpeg" // Chỉ hiển thị các tệp mp3
                startActivityForResult(intent, REQUEST_CODE_PICK_MP3)

            } else {
                showPermissionAlertDialog()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_MP3 && resultCode == Activity.RESULT_OK && data != null) {
            // Lấy Uri của file mp3 từ Intent result
            val uri: Uri? = data.data
            if (uri != null) {

                val contentResolver = requireContext().contentResolver
                val randomId = Random.nextInt(1000)
                val name = getFileName(contentResolver, uri)
                val filePath = getRealPathFromURI(uri)
                val newSound = name?.let { SoundEntity(it, filePath.toString(), randomId, 0, true) }
                    if (newSound != null) {
                        viewModel.insertSound(newSound)
                    }
                }
            }
        }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
        var name: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    name = it.getString(displayNameIndex)
                }
            }
        }
        return name
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null
        try {
            inputStream = requireContext().contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val fileName = getFileName(requireContext().contentResolver, uri)
                val outputFile = File(requireContext().filesDir, fileName) // Lưu vào thư mục ứng dụng
                filePath = outputFile.absolutePath

                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(4 * 1024) // 4KB buffer
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return filePath
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LEGACY_STORAGE_PERMISSION_REQUEST_CODE){
            val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

            if(write && read){
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/mpeg" // Chỉ hiển thị các tệp mp3
                startActivityForResult(intent, REQUEST_CODE_PICK_MP3)
            }else{
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun showPermissionAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
        alertDialogBuilder.setMessage("Please grant storage permission so the app can use your audio files")
        alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
            // cấp quyền qua setting
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


}




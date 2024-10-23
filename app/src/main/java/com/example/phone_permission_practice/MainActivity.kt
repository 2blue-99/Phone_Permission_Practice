package com.example.phone_permission_practice

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val CONTACTS_PERMISSION = android.Manifest.permission.READ_CONTACTS

class MainActivity : AppCompatActivity() {

    private lateinit var startActivityForResultHelper: StartActivityForResultHelper

    // 네이티브 연락처창 펴서 선택할 수 있음
    private val contactPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.e("TAG", "onCreate: ${result.data?.data}")
        if (result.resultCode == Activity.RESULT_OK) {
            val contactUri: Uri? = result.data?.data
            contactUri?.let { uri ->
                // 연락처 URI에서 데이터를 가져옴
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        // 전화번호를 가져오기 위한 인덱스
                        val nameIndex =
                            it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        val numberIndex =
                            it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                        val contactName = it.getString(nameIndex) // 연락처 이름
                        val contactNumber = it.getString(numberIndex) // 연락처 전화번호
                        // 전화번호를 처리하거나 UI에 표시
                        Log.e("TAG", "name : $contactName / num : $contactNumber")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if(!checkPermission(CONTACTS_PERMISSION)){
            requestPermission(CONTACTS_PERMISSION)
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.myBtn).setOnClickListener {
//            pickNumber()
            getContactsList()
        }
    }

    private fun pickNumber() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        Log.e("TAG", "start", )
        contactPickerLauncher.launch(intent)
    }

    private fun checkPermission(permission :String): Boolean {
        val nowState = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        Log.e("TAG", "nowState: $nowState")
        return nowState
    }

    private fun requestPermission(permission: String){
        requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 1000)
    }

    private fun getContactsList(){
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        val list = ArrayList<String>()
        contacts?.let {
            while (it.moveToNext()) {
                val name = contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                list.add("name : $name / number : $number")
            }
        }
        contacts?.close()
        Log.e("TAG", "getContactsList: ${list.size}", )
        list.forEach { println(it) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("TAG", "onRequestPermissionsResult: ", )
//        Log.e("TAG", "onRequestPermissionsResult: $requestCode | permissions : ${permissions[0]} | grantResults : ${grantResults[0]}", )

        if(checkPermission(CONTACTS_PERMISSION)){
            Log.e("TAG", "수락")
        }else{
            Log.e("TAG", "취소")
        }
    }
}
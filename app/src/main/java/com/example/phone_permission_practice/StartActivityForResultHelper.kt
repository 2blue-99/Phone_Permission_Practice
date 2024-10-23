package com.example.phone_permission_practice

import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.ActivityResultLauncher

class StartActivityForResultHelper(
    private val pickContact: ActivityResultLauncher<Intent>,
    private val getContact: ActivityResultLauncher<Intent>
) {
    fun launchPickContact(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        Log.e("TAG", "start", )
        pickContact.launch(intent)
    }

    fun launchGetContact(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        getContact.launch(intent)
    }
}
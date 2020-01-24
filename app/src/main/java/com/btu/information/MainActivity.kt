package com.btu.information

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        saveBtn.setOnClickListener {
            val n: String = inputName.text.toString()
            val p: String = inputPhone.text.toString()
            val a: String = inputAddress.text.toString()

            if (TextUtils.isEmpty(n)) {
                Toast.makeText(this, "Empty Data!", Toast.LENGTH_LONG).show()

            } else {
                contactInfo(n, p, a)
            }


        }

        clearBtn.setOnClickListener {
            val n: String = ""
            val p: String = ""
            val a: String = ""

            contactInfo(n, p, a)

            inputName.setText("")
            inputPhone.setText("")
            inputAddress.setText("")

        }

    }


    private fun init() {

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("UserInfo")

        addUserInfoChangeListener()
    }

    private fun contactInfo(name: String, phone: String?, address: String) {
        val userInfo = UserInfo(name, phone, address)
        db.child(auth.currentUser?.uid!!).setValue(userInfo)
    }

    private fun addUserInfoChangeListener() {
        db.child(auth.currentUser?.uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userInfo: UserInfo = p0.getValue(UserInfo::class.java) ?: return

                    showName.text = userInfo.name
                    showPhone.text = userInfo.mobile ?: ""
                    showAddress.text = userInfo.address


                    inputName.setText("")
                    inputPhone.setText("")
                    inputAddress.setText("")

                }
            })


//        clearBtn.setOnClickListener {
//
//            db.child(auth.currentUser?.uid!!)
//                .addValueEventListener(object : ValueEventListener {
//
//                    override fun onCancelled(p0: DatabaseError) {
//
//                    }
//
//                    override fun onDataChange(p0: DataSnapshot) {
//                        val userInfo: UserInfo = p0.getValue(UserInfo::class.java) ?: return
//
//                        showName.text = userInfo.name
//                        showPhone.text = userInfo.mobile ?: ""
//                        showAddress.text = userInfo.address
//
//
//                        showName.text = ""
//                        showAddress.text = ""
//                        showPhone.text = ""
//
//
//
//                    }
//                })
//
//
//
//
//
//        }


        backBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        changePasswordBtn.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


}



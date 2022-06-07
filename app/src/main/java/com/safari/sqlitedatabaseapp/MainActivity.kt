package com.safari.sqlitedatabaseapp

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.sql.SQLData

class MainActivity : AppCompatActivity() {
    var welcome:TextView ?=null
    var signIn:TextView ?=null
    var nameEt:EditText ?=null
    var emailEt:EditText ?=null
    var idNoEt:EditText ?=null
    var addBtn:Button ?=null
    var viewBtn:Button ?=null
    var deleteBtn:Button ?=null
    var db:SQLiteDatabase ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        welcome = findViewById(R.id.welcome)
        signIn = findViewById(R.id.signin)
        nameEt = findViewById(R.id.name)
        emailEt = findViewById(R.id.email)
        idNoEt = findViewById(R.id.idNo)
        addBtn = findViewById(R.id.addBtn)
        viewBtn = findViewById(R.id.viewBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        db = openOrCreateDatabase("Database_system", MODE_PRIVATE, null)
        db!!.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR,arafa VARCHAR,kitambulisho VARCHAR)")
        addBtn!!.setOnClickListener {
            //receive data from the user
            var name = nameEt!!.text.toString()
            var email = emailEt!!.text.toString()
            var idNo = idNoEt!!.text.toString()
            //check if user is submitting empty details
            if (name.isEmpty()) {
                nameEt!!.setError("Please fill this field")
                nameEt!!.requestFocus()
            } else if (email.isEmpty()) {
                emailEt!!.setError("Please fill this field")
                emailEt!!.requestFocus()
            } else if (idNo.isEmpty()) {
                idNoEt!!.setError("Please fill this field")
                idNoEt!!.requestFocus()
            } else {
                db!!.execSQL("INSERT INTO users VALUES('" + name + "','" + email + "','" + idNo + "')")
                display("success", "Data saved successfully")
            }
        }

        //set on click listner on the view button
    //use a cursor to select data fromt the user
        viewBtn!!.setOnClickListener {
            var cursor=db!!.rawQuery("SELECT * FROM users",null)
            //check if theres any record in the db
            if (cursor.count==0){
                display("No data found","Sorry your db is empty")
            }
            else{
                //use buffer to append the records
                var buffer=StringBuffer()
                while (cursor.moveToNext()){
                    buffer.append(cursor.getString(0)+"\n")
                    buffer.append(cursor.getString(1)+"\n")
                    buffer.append(cursor.getString(2)+"\n\n")
                }
                display("USERS",buffer.toString())
            }
        }
        deleteBtn!!.setOnClickListener {
            //use the id from the user
            var idNo=idNoEt!!.text.toString().trim()
            //check if id no is empty
            if (idNo.isEmpty()){
                idNoEt!!.setError("Please fill this input")
                idNoEt!!.requestFocus()
            }
            else{
                //use the cursor to select the user with the given id
                var cursor=db!!.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNo+"'",null)
                //check if the record is available in the db
                if (cursor.count==0){
                    display("No record found","Sorry we did not find the user")
                }
                else{
                    //delete the record
                    db!!.execSQL("DELETE FROM users WHERE kitambulisho='"+idNo+"'")
                    display("SUCCESS","user deleted successfully")
                }
            }
        }
    }
    fun display(title:String,message:String){
        var alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.create().show()
    }
}

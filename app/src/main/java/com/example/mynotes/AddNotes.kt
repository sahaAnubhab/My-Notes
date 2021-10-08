package com.example.mynotes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {
    val dbTable="Notes"
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        val bundle : Bundle = intent.extras!!
        id=bundle.getInt("ID",0)
        if (id!=0) {
            editTitle.setText(bundle.getString("name"))
            editDescription.setText(bundle.getString("description"))
        }

    }

    fun buAdd(view: View){
        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put("Title", editTitle.text.toString())
        values.put("Description", editDescription.text.toString())

        if (id==0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Note cannot be added", Toast.LENGTH_SHORT).show()
            }
            finish()
        }else{
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values,"ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Note cannot be updated", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

}
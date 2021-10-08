package com.example.mynotes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Dummy Data
//        listNotes.add(Note(1, "Hello World 1", "This is the description 1"))
//        listNotes.add(Note(2, "Hello World 2", "This is the description 2"))
//        listNotes.add(Note(3, "Hello World 3", "This is the description 3"))
        var myNotesAdapter = MyNoteAdapter(this,listNotes)
        myNotesList.adapter = myNotesAdapter

//        Load from DB
        var dbManager = DbManager(this)
        loadQuery("%")

    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    fun loadQuery(title: String){
        var dbManager = DbManager(this)
        val projection = arrayOf("ID", "Title", "Description")
        val selectArgs = arrayOf(title)
        val cursor= dbManager.query(projection, "Title like?", selectArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()){
            do {
                val ID =cursor.getInt(cursor.getColumnIndex("ID"))
                val Title =cursor.getString(cursor.getColumnIndex("Title"))
                val Description =cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))
            }while (cursor.moveToNext())
        }

        var myNotesAdapter= MyNoteAdapter(this, listNotes)
        myNotesList.adapter=myNotesAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQuery("%$newText%")
                return false
            }


        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.addNote -> {
//                Go to the add page.
                val intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }



    inner class MyNoteAdapter(var context: Context,var listNotes: ArrayList<Note>) : BaseAdapter() {
        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.item, null)
            var note = listNotes[position]
            myView.tvTitle.text = note.noteName
            myView.content.text = note.noteDescription
            myView.imgDelete.setOnClickListener{
                var dbManager = DbManager(this.context)
                val selectionArgs = arrayOf(note.noteID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
//              notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()
            }
            myView.imgUpdate.setOnClickListener{
                goToUpdate(note)
            }
            return myView
        }
    }

    fun goToUpdate(note: Note){
        val intent= Intent(this@MainActivity, AddNotes::class.java)
        intent.putExtra("ID", note.noteID)
        intent.putExtra("name", note.noteName)
        intent.putExtra("description", note.noteDescription)
        startActivity(intent)
    }

}
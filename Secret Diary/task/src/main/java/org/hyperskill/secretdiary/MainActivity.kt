package org.hyperskill.secretdiary
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.secretdiary.databinding.ActivityMainBinding
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var notesList = mutableListOf<String>()
    private lateinit var notesString: String
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("PREF_DIARY", Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notesString = sharedPreferences.getString("KEY_DIARY_TEXT", "")!!
        notesString.toNotesList()
        binding.tvDiary.text = notesString

        binding.btnSave.setOnClickListener {
            val text = binding.etNewWriting.text.toString()
            binding.etNewWriting.setText("")
            if (text.trim().isEmpty()) {
                Toast.makeText(this@MainActivity, "Empty or blank input cannot be saved", Toast.LENGTH_SHORT).show()
            } else {
                notesList.add(0, createNote(text))
                changeNotesList()
            }
        }

        binding.btnUndo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Remove last note")
                .setMessage(getString(R.string.undo_message))
                .setPositiveButton("Yes") { _, _ ->
                    if (notesList.isNotEmpty()) {
                        notesList.removeFirst()
                        changeNotesList()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNote(text: String): String {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return "$time\n$text"

    }

    private fun changeNotesList() {
        notesString = notesList.joinToString("\n\n")
        sharedPreferences.edit().putString("KEY_DIARY_TEXT", notesString).apply()
        binding.tvDiary.text = notesString
    }

    private fun String.toNotesList() {
        if (this.isNotEmpty()) {
            val array = this.split("\n\n")
            notesList = array.toMutableList()
        }
    }
}
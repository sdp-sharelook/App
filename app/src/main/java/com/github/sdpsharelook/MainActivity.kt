package com.github.sdpsharelook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.Section.SectionActivity
import com.github.sdpsharelook.authorization.LoginActivity
import com.github.sdpsharelook.language.LanguageSelectionDialog


const val EXTRA_MESSAGE = "com.github.sdpsharelook.NAME"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun greet(view: View) {
        val editText = findViewById<EditText>(R.id.edit_text_name)
        val name = editText?.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, name)
        }
        startActivity(intent)
    }


    fun sectionActivity(view: View) =
        startActivity(Intent(this, SectionActivity::class.java))

    fun translatorActivity(view: View) =
        startActivity(Intent(this, TranslateActivity::class.java))

    fun loginActivity(view: View) =
        startActivity(Intent(this, LoginActivity::class.java))

    fun languagesPopup(view: View) =
        LanguageSelectionDialog.selectLanguage(this)


}

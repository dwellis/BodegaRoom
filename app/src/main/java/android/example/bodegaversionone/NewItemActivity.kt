package android.example.bodegaversionone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewItemActivity : AppCompatActivity() {

    private lateinit var editItemView : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        editItemView = findViewById(R.id.edit_item)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editItemView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val item = editItemView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, item)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        // TODO: needs to be updated to correct location
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
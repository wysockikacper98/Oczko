package app.oczko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //switch to next view
        val startButton: Button = findViewById(R.id.startButton)
        val playerName: EditText = findViewById(R.id.playerNameEditText)
        startButton.setOnClickListener {
            val intent = Intent(applicationContext, GameActivity::class.java)
            if (playerName.text.isEmpty()) {
                intent.putExtra("playerName", "Player")
            }else{
                intent.putExtra("playerName", playerName.text)
            }
            startActivity(intent)
        }
    }
}
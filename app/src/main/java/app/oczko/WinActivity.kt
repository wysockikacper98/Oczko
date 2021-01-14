package app.oczko

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.system.exitProcess

class WinActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        // did player win
        val winOrLose: TextView = findViewById(R.id.winOrLoseTextView)
        winOrLose.text = "${intent.getCharSequenceExtra("whoWin")}"

        // getting score data
        val finalScoreBanker: TextView = findViewById(R.id.finalScoreBanker)
        val finalScorePlayer: TextView = findViewById(R.id.finalScorePlayer)
        finalScoreBanker.text = "${intent.getCharSequenceExtra("bankerScore")}"
        finalScorePlayer.text = "${intent.getCharSequenceExtra("playerScore")}"

        // go to Main Menu or Close app
        val mainMenuButton: Button = findViewById(R.id.mainMenuButton)

        mainMenuButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent);
        }



    }
}
package app.oczko

import android.content.Intent
import android.graphics.drawable.Animatable2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.size
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity() {

    companion object {
        const val width: Int = 300
        const val height: Int = width * 2
        const val gameLength: Int = 5
        var countCards: Int = 0
        var cardShuffled = Cards.cardShuffled
        var bankerPoints = arrayListOf<Int>()
        var playerPoints = arrayListOf<Int>()
        var bankerScore: Int = 0
        var playerScore: Int = 0

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Buttons
        val passButton: Button = findViewById(R.id.passButton)
        val addCardButton: Button = findViewById(R.id.addPlayerCardButton)


        //TODO implementacja usuwania ostatniej karty w bankierze
//        val cardLayout: LinearLayout = findViewById(R.id.bankerCardLinearLayout)
//        cardLayout.removeViewAt(cardLayout.size - 1)


        val playerCardLayout: LinearLayout = findViewById(R.id.playerCardLinearLayout)

        val playerPointTextView: TextView = findViewById(R.id.playerPointsTextView)

        val bankerScoreTextView: TextView = findViewById(R.id.bankerScoreTextView)
        val playerScoreTextView: TextView = findViewById(R.id.playerScoreTextView)

        bankerScoreTextView.text = bankerScore.toString()
        playerScoreTextView.text = playerScore.toString()


        //Czyszczenie planszy do gry
        clearBoard()


        //pass Button
        passButton.setOnClickListener {
            addCardButton.visibility = View.INVISIBLE
            passButton.visibility = View.INVISIBLE
            //NATYCHMIASTOWA wygrana bankiera gracz dobrał za dużo kart
            if (playerPointTextView.text.toString().toInt() > 21) {
                bankerWin()
            } else {
                //fizyka gry bankiera
                bankerPlay()
//                while (bankerPlay2())
                //porównywanie wyników i wybranie wygranego
                whoWins()
            }
        }


        addCardButton.setOnClickListener {
            addCard(playerCardLayout, playerPoints)
            playerPointTextView.text = playerPoints.sum().toString()
            if (playerPointTextView.text.toString().toInt() >= 21) {
                addCardButton.visibility = View.INVISIBLE
                passButton.visibility = View.INVISIBLE
                passButton.performClick()
            }
        }

    }

    private fun whoWins() {
        val playerScore: Int = playerPoints.sum()
        val bankerScore: Int = bankerPoints.sum()
        //gracz w tym przypadku może mieć tylko 21 lub mniej pkt
        //wygrany przy dwóch asach zostały rozwiazane w innym miejscu kodu
        when {
            bankerScore > 21 -> {
                playerWin()
            }
            playerScore > bankerScore -> {
                playerWin()
            }
            playerScore == bankerScore -> {
                bankerWin()
            }
            else -> {//wybrana gdy bankier będzie miał więcej punktów do gracza
                bankerWin()
            }
        }

    }

    private fun bankerWin() {
        Toast.makeText(applicationContext, "LOST", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            bankerScore++
            if (bankerScore == gameLength) {
                gameOver(false)
            }
            clearBoard()

        }, 2000)
    }

    /**
     * Mechanika gry bankiera
     * Okrywa kartę
     * Jeśli ma 2 AS'y czyli 22 punkty to wygrywa
     * Jeśli
     */
    private fun bankerPlay() {
        val bankerPointTextView: TextView = findViewById(R.id.bankerPointsTextView)
        val bankerCardLayout: LinearLayout = findViewById(R.id.bankerCardLinearLayout)
        //w pierwszej kolejności "odkrywamy" kartę
        bankerCardLayout.removeViewAt(bankerCardLayout.size - 1)
        addCard(bankerCardLayout, bankerPoints)
        bankerPointTextView.text = bankerPoints.sum().toString()

        if (bankerPoints.sum() == 22) {
            // wygrana poprzez posiadanie dwóch Asów
            bankerWin()
        }


        while (bankerPoints.sum() < 17) {
            addCard(bankerCardLayout, bankerPoints)
            bankerPointTextView.text = bankerPoints.sum().toString()

        }

    }

    /**
     * Funkcja inicjowana w przypadku wygranej gracz
     */
    private fun playerWin() {
        Toast.makeText(applicationContext, "WIN", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            playerScore++
            if (playerScore == gameLength) {
                gameOver(true)
            }
            clearBoard()
        }, 2000)
    }

    /**
     * Fukckja kończy grę i przechodzi do ekranu końcowego
     * @param didPlayerWin True if player WIN, else False
     */
    private fun gameOver(didPlayerWin: Boolean) {
        val text: String
        if (didPlayerWin) {
            text = "Congratulations You WIN"
        } else {
            text = "Nice try! Good luck next time"
        }
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, WinActivity::class.java))
        }, 2000)
    }

    /**
     * Funkcja resetująca grę od nowa
     */
    private fun clearBoard() {
        //tasowanie kart
        for (i in 0..51) {
            cardShuffled.add(i)
        }
        cardShuffled.shuffle()
        countCards = 0
        //czyszczenie 'ręki' gracza i bankiera
        bankerPoints.clear()
        playerPoints.clear()

        //usuwanie wyświetlanych kart
        val bankerCardLayout: LinearLayout = findViewById(R.id.bankerCardLinearLayout)
        val playerCardLayout: LinearLayout = findViewById(R.id.playerCardLinearLayout)
        bankerCardLayout.removeAllViews()
        playerCardLayout.removeAllViews()
        //włącznie buttonów
        findViewById<Button>(R.id.passButton).visibility = View.VISIBLE
        findViewById<Button>(R.id.addPlayerCardButton).visibility = View.VISIBLE
        findViewById<TextView>(R.id.bankerScoreTextView).text = bankerScore.toString()
        findViewById<TextView>(R.id.playerScoreTextView).text = playerScore.toString()
        firstCardDeal(bankerCardLayout, playerCardLayout)
    }


    private fun firstCardDeal(bankerCardLayout: LinearLayout, playerCardLayout: LinearLayout) {
        val bankerPointTextView: TextView = findViewById(R.id.bankerPointsTextView)
        val playerPointTextView: TextView = findViewById(R.id.playerPointsTextView)

        Handler(Looper.getMainLooper()).postDelayed({
            addCard(bankerCardLayout, bankerPoints)
            bankerPointTextView.text = bankerPoints.sum().toString()

            Handler(Looper.getMainLooper()).postDelayed({
                addCard(playerCardLayout, playerPoints)
                playerPointTextView.text = playerPoints.sum().toString()

                Handler(Looper.getMainLooper()).postDelayed({
                    //druga karta bankiera odwrócona
                    addBackCard(bankerCardLayout)

                    Handler(Looper.getMainLooper()).postDelayed({
                        addCard(playerCardLayout, playerPoints)
                        playerPointTextView.text = playerPoints.sum().toString()
                        if (playerPoints.sum() == 22) {
                            playerWin()
                        } else {
                            findViewById<Button>(R.id.passButton).visibility = View.VISIBLE
                            findViewById<Button>(R.id.addPlayerCardButton).visibility = View.VISIBLE
                        }
                    }, 500)

                }, 500)

            }, 500)

        }, 500)


    }

    private fun addBackCard(cardLayout: LinearLayout) {
        val imageView = ImageView(applicationContext)
        imageView.setImageResource(R.drawable.blue_back)
        addView(imageView, cardLayout)
    }

    /**
     * Dodaje pierwszą kartę ze stodu posorotwanych kart do widoku cardLayout,
     * @param cardLayout Layout do którego zostanie dodana karta
     * @param points Lista do której zostanie dodany punkt za kartę
     */
    private fun addCard(cardLayout: LinearLayout, points: ArrayList<Int>) {
        //pobranie widoku karty z wierzchu
        val imageView = ImageView(applicationContext)
        imageView.setImageResource(Cards.cardList[cardShuffled[countCards]])
        addView(imageView, cardLayout)


        //dodawanie punktów za dodaną kartę
        updatePoints(cardShuffled[countCards], points)

        countCards++
    }

    /**
     * Funkjca updatująca pole typu TextView przechowująca liczbę PKTÓW
     * @param pickedCardID: Numer ID karty pobrany z pola Card.[countCards]
     */
    private fun updatePoints(pickedCardID: Int, points: ArrayList<Int>) {
        when (pickedCardID) {
            //karty numeryczne
            in 0..8 -> {
                points.add(pickedCardID + 2)
            }
            in 13..21 -> {
                points.add(pickedCardID - 11)
            }
            in 26..34 -> {
                points.add(pickedCardID - 24)
            }
            in 39..47 -> {
                points.add(pickedCardID - 37)
            }
            //figury
            in 9..11, in 22..24, in 35..37, in 48..50 -> {
                points.add(10)
            }
            //AS
            12, 25, 38, 51 -> {
                points.add(11)
            }
            else -> {
                print("ERROR w liczeniu kart")
            }
        }

        //Najkorzystniejsze punkty dla gracza
        //wykluczamy opcję 2 razy AS
        if (points.sum() != 22 || points.size != 2) {
            if (points.sum() > 21) {//wykluczmay opcję 21 punktów które wygrywają
                for (current in points.indices) {
                    if (points[current] == 11) {
                        points[current] = 1
                    }
                }
            }
        }

    }


    //dodawanie karty/obrazu do konkretnego widoku
    /**
     * Dodawanie obrazu (karty) do wybranego widoku
     * @param imageView obraz który dodajemy
     * @param layout LinearLayout do którego dodawany jest obraz
     */
    private fun addView(imageView: ImageView, layout: LinearLayout) {
        //wybieranie parametrów dla wyświeltanej karty
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height)
        layoutParams.setMargins(0, 0, 0, 0)
        //ładowanie parametrów do karty oraz dodawanie jej do widoku
        imageView.layoutParams = layoutParams
        imageView.animation = AnimationUtils.loadAnimation(this, R.anim.entry_animation)
        layout.addView(imageView)
    }


}


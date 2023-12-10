package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * ViewModel containing the app data and methods to process the data.
 */
class GameViewModel : ViewModel() {

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    // 得分
    private var _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    /**
     * Increases the game score if the player's word is correct.
     * （计算得分）
     */
    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    /**
     * Returns true if the player word is correct.
     * Increases the score accordingly.
     * （判断玩家猜出的单词是否正确）
     */
    fun isUserWordCorrect(playerWord: String): Boolean {
        return if(playerWord.equals(currentWord, true)) {
            increaseScore()
            true
        } else {
            false
        }
    }

    // 单词数
    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    // 当前乱序词
    private val _currentScrambledWord = MutableLiveData<String>()
    /*
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord
    */
    // 无障碍阅读版本
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if(it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    // 存储游戏中所用单词的列表，以避免重复
    private var wordsList: MutableList<String> = mutableListOf()
    // 存储玩家正在尝试理顺的单词
    private lateinit var currentWord: String

    /*
     * Updates currentWord and currentScrambledWord with the next word.
     * （获取下一个单词）
     */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        if(wordsList.contains(currentWord)) {
            getNextWord()
        }

        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while(String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        _currentScrambledWord.value = String(tempWord)
        _currentWordCount.value = (_currentWordCount.value)?.inc() //i=i?+1
        wordsList.add(currentWord)
    }

    /*
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if(_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    /**
     * Re-initializes the game data to restart the game.
     * （重置应用数据）
     */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

}
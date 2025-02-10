package screens

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.KeyType
import models.enums.CoinName
import utils.ScreenManager

class ConverterScreen: Screen() {
    private val graphics = ScreenManager.graphics
    private val screen = ScreenManager.screen
    private val coinsList = CoinName.entries.toTypedArray()
    private var coinPosition = 0

    override suspend fun run() {
        doConversion()
        MenuStack.goBack()
    }

    private fun doConversion() {
        ScreenManager.clearScreen()

        val aCoin = selectCoinFromMenu() ?: return
        val aCoinAmount = getAmountFromUser()

    }

    private fun selectCoinFromMenu(): CoinName? {
        var aCoinSelected = false

        while (!aCoinSelected) {
            graphics.putString(12, 9, " ".repeat(30))
            graphics.putString(10, 10, " ".repeat(30))
            graphics.putString(12, 11, " ".repeat(30))
            ScreenManager.refreshScreen()
            graphics.foregroundColor = TextColor.ANSI.WHITE
            graphics.putString(12, 9, "${coinsList.getOrNull(coinPosition - 1) ?: ""}")
            graphics.foregroundColor = TextColor.ANSI.WHITE_BRIGHT
            graphics.putString(10, 10, "> ${coinsList[coinPosition]} <")
            graphics.foregroundColor = TextColor.ANSI.WHITE
            graphics.putString(12, 11, "${coinsList.getOrNull(coinPosition + 1) ?: ""}")
            ScreenManager.refreshScreen()
            aCoinSelected = evaluateOption() ?: return null
        }

        val selectedCoin = coinsList[coinPosition]
        coinPosition = 0

        return selectedCoin
    }

    private fun evaluateOption(): Boolean? {
        val inputKey = screen.readInput()
        var isSelected = false

        when (inputKey.keyType) {
            KeyType.ArrowUp -> if (coinPosition > 0) coinPosition--
            KeyType.ArrowDown -> if (coinPosition < coinsList.size - 1) coinPosition++
            KeyType.Enter -> isSelected = true
            KeyType.Escape -> return null
            else -> {}
        }
        return isSelected
    }

    private fun getAmountFromUser(): Double? {
        return 0.0
    }
}
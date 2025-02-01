package lang

import models.enums.CountryCode

object Strings {
    val username = mapOf<CountryCode, String>(
        CountryCode.ENG to "Username: ",
        CountryCode.ES to "Usuario: ",
        CountryCode.UA to "Користувач: ",
    )

    val password = mapOf<CountryCode, String>(
        CountryCode.ENG to "Password: ",
        CountryCode.ES to "Contraseña: ",
        CountryCode.UA to "Пароль: ",
    )

    val invalidOption = mapOf<CountryCode, String>(
        CountryCode.ENG to "Invalid Option",
    )
    val failedLogin = mapOf<CountryCode, String>(
        CountryCode.ENG to "Invalid username or password. Try again or type \\exit to close login screen.",
    )

}
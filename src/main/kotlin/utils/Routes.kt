package utils

class Routes private constructor() {
    val usersFile = "src/main/data/users"
    companion object {
        val instance: Routes by lazy()
        { Routes() }
    }
}

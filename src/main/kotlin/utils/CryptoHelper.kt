package utils

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import java.util.Base64

object CryptoHelper {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"


    private val keyBytes = Base64.getDecoder().decode("KHj/gzmYwXLlTb/NrjO7AbaJlvAMTG1wggwVXNvNpjM=")
    private val secretKey: SecretKey = SecretKeySpec(keyBytes, "AES")

    fun encrypt(data: String): String {
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))

        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(iv + encryptedBytes)
    }

    fun decrypt(encryptedData: String): String {
        val dataBytes = Base64.getDecoder().decode(encryptedData)
        val ivSpec = IvParameterSpec(dataBytes.copyOfRange(0, 16))
        val encryptedBytes = dataBytes.copyOfRange(16, dataBytes.size)

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }
}
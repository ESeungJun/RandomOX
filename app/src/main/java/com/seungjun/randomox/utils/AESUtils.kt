package com.seungjun.randomox.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Base64

import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.Key
import java.security.NoSuchAlgorithmException

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES 암호화를 위한 지원 클래스
 * Created by SeungJun on 2017-07-19.
 *
 * 16자리의 키값을 입력하여 객체를 생성한다.
 * @param context
 * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
 *
 **/
class AESUtils @Throws(Exception::class) constructor(context: Context) {

    private val iv: String?
    private val keySpec: Key

    init {

        val key = getBase64Text(context)

        iv = key
        val keyBytes = ByteArray(16)
        val b = key!!.toByteArray(charset("UTF-8"))

        var len = b.size
        if (len > keyBytes.size) {
            len = keyBytes.size
        }
        System.arraycopy(b, 0, keyBytes, 0, len)
        val keySpec = SecretKeySpec(keyBytes, "AES")

        this.keySpec = keySpec
    }

    /**
     * AES256 으로 암호화 한다.
     * @param str 암호화할 문자열
     * @return 암호화 된 문자열
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    @Throws(NoSuchAlgorithmException::class, GeneralSecurityException::class, UnsupportedEncodingException::class)
    fun encrypt(str: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv!!.toByteArray()))
        val encrypted = c.doFinal(str.toByteArray(charset("UTF-8")))
        return String(Base64.encode(encrypted, Base64.DEFAULT))
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     * @param str 복호화할 문자열
     * @return 복호화 된 문자열
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    @Throws(NoSuchAlgorithmException::class, GeneralSecurityException::class, UnsupportedEncodingException::class)
    fun decrypt(str: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv!!.toByteArray(charset("UTF-8"))))
        val byteStr = Base64.decode(str.toByteArray(), Base64.DEFAULT)
        return String(c.doFinal(byteStr), charset("UTF-8"))
    }

    /**
     * Base64 암호화 파일 복호화
     * @param context
     * @return 복호화된 문자
     */
    private fun getBase64Text(context: Context): String? {

        var am: AssetManager? = context.resources.assets

        var `is`: InputStream? = null

        // 읽어들인 문자열이 담길 변수
        var result: String? = null

        try {
            `is` = am!!.open("base64.txt")
            val size = `is`!!.available()

            if (size > 0) {
                val data = ByteArray(size)
                `is`.read(data)
                result = String(Base64.decode(data, Base64.DEFAULT))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                    `is` = null
                } catch (e: Exception) {
                }

            }
        }

        am = null
        return result

    }
}


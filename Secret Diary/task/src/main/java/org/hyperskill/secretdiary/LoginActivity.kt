package org.hyperskill.secretdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.hyperskill.secretdiary.databinding.ActivityLoginBinding
import org.hyperskill.secretdiary.databinding.ActivityMainBinding

const val PASSWORD = "1234"


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val text = binding.etPin.text.toString()

            if (text != PASSWORD) {
                binding.etPin.error = getString(R.string.wrong_pin)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}
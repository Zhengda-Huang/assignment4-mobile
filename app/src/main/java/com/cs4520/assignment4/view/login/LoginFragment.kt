import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cs4520.assignment4.databinding.FragmentLoginBinding
import java.util.logging.Logger

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val logger = Logger.getLogger("MyLogger")

    interface OnLoginSuccessListener {
        fun onLoginSuccess()
    }

    private var loginSuccessListener: OnLoginSuccessListener? = null

    fun setOnLoginSuccessListener(listener: OnLoginSuccessListener) {
        loginSuccessListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logger.info("On view created is called")

        binding.apply {
            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (username == "admin" && password == "admin") {
                    usernameEditText.text.clear()
                    passwordEditText.text.clear()
                    logger.info("Login Successful")
                    loginSuccessListener?.onLoginSuccess()
                } else {
                    Toast.makeText(context, "Incorrect Username/Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

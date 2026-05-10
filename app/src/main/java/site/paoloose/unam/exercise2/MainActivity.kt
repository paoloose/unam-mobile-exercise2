package site.paoloose.unam.exercise2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import site.paoloose.unam.exercise2.ui.Exercise2App
import site.paoloose.unam.exercise2.ui.theme.Exercise2Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        // Keep the native splash screen visible only until the first compose frame is drawn
        // We use false here so it hides as fast as physically possible
        splashScreen.setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Exercise2Theme {
                Exercise2App()
            }
        }
    }
}

package volio.b1.foflow.ui.language

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.R

class LanguageActivity : AppCompatActivity() {
    var isShowOnlyScreen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_language)
        hideNavigationBar()
        addFragment()
    }

    fun addFragment() {
        isShowOnlyScreen =
            intent?.getBooleanExtra(LanguageActivity.isShowOnlyScreen, false) ?: false

        FOFlowManager.config?.language?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flAddFragment, it).commit()
        } ?: run {
            FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
            finish()
        }
    }

    private fun hideNavigationBar() {
        WindowCompat.getInsetsController(window, findViewById(R.id.layoutRoot)).let {
            it.hide(WindowInsetsCompat.Type.navigationBars())
            it.hide(WindowInsetsCompat.Type.statusBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRoot)) { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }
    }

    companion object {
        val idScreen = "language"
        val isShowOnlyScreen = "isShowOnlyScreen"
    }
}
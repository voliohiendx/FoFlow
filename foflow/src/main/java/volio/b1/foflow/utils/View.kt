package volio.b1.foflow.utils

import android.view.View

fun View.setPreventDoubleClick(debounceTime: Long = 300, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            if (System.currentTimeMillis() - lastClickTime < debounceTime) return
            action.invoke()
            lastClickTime = System.currentTimeMillis()
        }
    })
}

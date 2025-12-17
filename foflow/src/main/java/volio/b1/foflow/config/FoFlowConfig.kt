package volio.b1.foflow.config

import androidx.fragment.app.Fragment


data class FoFlowConfig(
    var language: () -> Fragment,
    var onboarding: () -> Fragment,
) {
    class Builder {
        private lateinit var language: () -> Fragment
        private lateinit var onboarding: () -> Fragment

        fun language(config: () -> Fragment) = apply { this.language = config }
        fun onboarding(config: () -> Fragment) = apply { this.onboarding = config }

        fun build() = FoFlowConfig(language, onboarding)
    }
}

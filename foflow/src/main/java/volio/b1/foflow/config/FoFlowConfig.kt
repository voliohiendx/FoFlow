package volio.b1.foflow.config

data class FoFlowConfig(
    val language: LanguageConfig,
    val onboarding: OnboardingConfig
){
    class Builder {
        private lateinit var language: LanguageConfig
        private lateinit var onboarding: OnboardingConfig

        fun language(config: LanguageConfig) = apply { this.language = config }
        fun onboarding(config: OnboardingConfig) = apply { this.onboarding = config }

        fun build() = FoFlowConfig(language, onboarding)
    }
}

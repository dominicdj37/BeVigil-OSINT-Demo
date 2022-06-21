package com.example.bevigilosintdemo.api.core

object ApiConstants {

    /**
     * get complete url string including the base url
     */
    fun getRequestUrlFor(url: String) = APIGlobal.getBaseURL() + url

    fun String?.formatUrl(vararg args: Any?): String {
        if (!this.isNullOrEmpty()) {
            return this.format(*args)
        }
        return ""
    }

    const val TOKEN_KEY = "X-Access-Token"

    const val SETTINGS = "settings"
    const val SECRETS = "secrets"
    const val SEARCH_HISTORY = "searchHistory"
    const val PACKAGE_NAMES = "packageNames"

    const val ASSETS = "%s/all-assets/"
    const val APPS = "%s/apps/"
}
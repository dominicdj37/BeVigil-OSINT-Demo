package com.example.bevigilosintdemo.core

import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.utils.ResourceUtils.getColorResource

object Constants {

    const val ASSET_TYPE_KEY = "asset_type_key"
    const val PACKAGE_NAME_KEY = "package_name_key"
    const val SEARCH_DOMAIN = "search_mode_domain"
    const val COUNT_TAG = "{count}"
    const val NAME_TAG = "{name}"
}

object AssetKeyType {
    const val EMAIL = "email"
    const val FILENAME = "filename"
    const val FILE_PATH = "file_path"
    const val HOST = "host"
    const val REST_API = "rest_api"
    const val URL = "url"
    const val RELATIVE_ENDPOINT = "relative_endpoint"
    const val IP_ADDRESS_DISCLOSURE = "IP Address disclosure"
    const val IP_URL = "ip_url"
    const val AMAZON_ELB_URL = "Amazon ELB URL"
    const val AWS_URL = "AWS URL"
    const val CLOUDFRONT_URL = "CloudFront URL"
    const val FIREBASE_URL = "Firebase URL"

    fun getColorForAssetKey(key: String): Int? {
        return when(key) {
            EMAIL -> getColorResource(R.color.holo_blue_bright)
            FILENAME -> getColorResource(R.color.holo_blue_dark)
            FILE_PATH -> getColorResource(R.color.holo_blue_light)
            HOST -> getColorResource(R.color.holo_green_dark)
            REST_API -> getColorResource(R.color.holo_green_light)
            URL -> getColorResource(R.color.holo_orange_dark)
            RELATIVE_ENDPOINT -> getColorResource(R.color.holo_orange_light)
            IP_ADDRESS_DISCLOSURE -> getColorResource(R.color.holo_purple)
            IP_URL -> getColorResource(R.color.holo_red_dark)
            AMAZON_ELB_URL -> getColorResource(R.color.holo_red_light)
            AWS_URL -> getColorResource(R.color.MediumSlateBlue)
            CLOUDFRONT_URL -> getColorResource(R.color.MediumSpringGreen)
            FIREBASE_URL -> getColorResource(R.color.MediumSeaGreen)
            else -> getColorResource(R.color.black)
        }
    }
}

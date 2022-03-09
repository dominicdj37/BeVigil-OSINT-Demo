package com.example.bevigilosintdemo.api.model

import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.core.AssetKeyType
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AssetModel {

    @SerializedName("package_id") @Expose
    var packageID: String? = null

    @SerializedName("assets") @Expose
    var assets: LinkedHashMap<String,ArrayList<String>> = linkedMapOf()

    @SerializedName("detail") @Expose
    var detail: String? = null

    var apiCalled = false

    fun getKeyAt(index: Int) = assets.keys.elementAt(index)
    fun getAssetAt(index: Int) = assets.values.elementAt(index)
    fun isEmpty() = assets.isEmpty()
    fun getAssetsCount() = assets.count()
    fun getAssetCountAt(key: String) = assets[key]?.count() ?: 0

    fun getTitleStringForAsset(assetKey: String): String {
        var titleString = when(assetKey) {
            AssetKeyType.EMAIL -> getStringResource(R.string.asset_title_email)
            AssetKeyType.FILENAME -> getStringResource(R.string.asset_title_filename)
            AssetKeyType.FILE_PATH -> getStringResource(R.string.asset_title_file_path)
            AssetKeyType.HOST -> getStringResource(R.string.asset_title_host)
            AssetKeyType.REST_API -> getStringResource(R.string.asset_title_rest_api)
            AssetKeyType.URL -> getStringResource(R.string.asset_title_url)
            AssetKeyType.RELATIVE_ENDPOINT -> getStringResource(R.string.asset_title_relative_endpoint)
            AssetKeyType.IP_ADDRESS_DISCLOSURE -> getStringResource(R.string.asset_title_ip_address_disclosure)
            AssetKeyType.IP_URL -> getStringResource(R.string.asset_title_ip_url)
            AssetKeyType.AMAZON_ELB_URL -> getStringResource(R.string.asset_title_amazon_elb_url)
            AssetKeyType.AWS_URL -> getStringResource(R.string.asset_title_aws_url)
            AssetKeyType.CLOUDFRONT_URL -> getStringResource(R.string.asset_title_cloudFront_url)
            AssetKeyType.FIREBASE_URL -> getStringResource(R.string.asset_title_firebase_url)
            else -> assetKey
        }

        val assetCount = assets[assetKey]?.size ?: 0
        if(assetCount > 1) {
            titleString = titleString.plus("s")
        }
        return titleString.replace("{count}", assetCount.toString())
    }

    fun getAssetListString(assetKey: String): String {
        var assetsListString = ""
        assets[assetKey]?.let { asset ->
            asset.forEach { assetString ->
                assetsListString = assetsListString.plus(assetString).plus("\n")
            }
            return assetsListString
        }
        return ""
    }

}
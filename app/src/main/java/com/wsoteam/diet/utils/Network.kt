package com.wsoteam.diet.utils

import android.content.Context
import android.net.*
import android.net.Network
import android.util.Log


class Network {

    companion object{
         fun hasNetwork(context: Context?) {
            if (context == null) return

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)

                    Log.d("kkk", "onLost")
                }

                override fun onUnavailable() {
                    super.onUnavailable()

                    Log.d("kkk", "onUnavailable")
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)

                    Log.d("kkk", "onLosing")
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                    Log.d("kkk", "onAvailable")
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    super.onBlockedStatusChanged(network, blocked)

                    Log.d("kkk", "onBlockedStatusChanged")
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)

                    Log.d("kkk", "onCapabilitiesChanged")
                }

                override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties)

                    Log.d("kkk", "onLinkPropertiesChanged")
                }
            }

            val connectivityManager =
                    context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder()
                    .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }
}
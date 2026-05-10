package site.paoloose.unam.exercise2.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class NetworkMonitor(context: Context) {
    // Use application context to avoid memory leaks
    private val appContext = context.applicationContext

    private val connectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkAvailableFlow: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Initial state
        val currentNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        trySend(hasInternet)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

    val isConnected: Flow<Boolean> = networkAvailableFlow.flatMapLatest { isNetworkAvailable ->
        if (isNetworkAvailable) {
            flow {
                while (true) {
                    val isReachable = pingGoogleDNS()
                    emit(isReachable)
                    // Increase ping rate (decrease delay) if the last ping failed
                    if (isReachable) {
                        delay(5000L) // Normal rate: 5 seconds
                    } else {
                        delay(2000L) // Increased rate: 2 seconds
                    }
                }
            }
        } else {
            flowOf(false)
        }
    }.distinctUntilChanged().flowOn(Dispatchers.IO)


    private suspend fun pingGoogleDNS(): Boolean = withContext(Dispatchers.IO) {
        try {
            val socket = Socket()
            // Connect to Google DNS port 53 with a timeout of 1500ms
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}

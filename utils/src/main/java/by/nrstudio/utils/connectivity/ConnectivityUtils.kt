package by.nrstudio.utils.connectivity

interface ConnectivityUtils {
    /**
     * @return TRUE if client is connected to Wife or Cell data.
     */
    fun isNetworkAvailable(): Boolean
}

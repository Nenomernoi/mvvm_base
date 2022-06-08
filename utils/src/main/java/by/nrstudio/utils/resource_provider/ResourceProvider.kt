package by.nrstudio.utils.resource_provider

interface ResourceProvider {
    fun getString(resourceId: Int): String
    fun getString(resourceId: Int, vararg args: Any): String
}

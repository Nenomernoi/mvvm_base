package by.nrstudio.basemodulesmvi.util.resource_provider

import android.content.Context
import by.nrstudio.utils.resource_provider.ResourceProvider

class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {

    override fun getString(resourceId: Int): String = context.getString(resourceId)

    override fun getString(
        resourceId: Int,
        vararg args: Any
    ): String {
        return if (args.isNotEmpty()) {
            context.resources.getString(resourceId, *args)
        } else {
            context.resources.getString(resourceId)
        }
    }
}

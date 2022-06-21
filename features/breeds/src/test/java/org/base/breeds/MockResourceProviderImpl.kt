package org.base.breeds

import org.base.utils.resource_provider.ResourceProvider

class MockResourceProviderImpl : ResourceProvider {
    override fun getString(resourceId: Int): String = ""

    override fun getString(resourceId: Int, vararg args: Any): String {
        return if (args[0] is String) {
            (args[0] as String)
        } else ""
    }
}

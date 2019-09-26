package org.mainsoft.base.util

import com.bumptech.glide.load.Key

import java.nio.ByteBuffer
import java.security.MessageDigest

class ImageSignature(private val currentVersion: String) : Key {

    override fun equals(o: Any?): Boolean {
        if (o is ImageSignature) {
            val other = o as ImageSignature?
            return currentVersion == other?.currentVersion?:false
        }
        return false
    }

    override fun hashCode(): Int {
        return currentVersion.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ByteBuffer.allocate(Integer.SIZE).putInt(currentVersion.hashCode()).array())
    }
}

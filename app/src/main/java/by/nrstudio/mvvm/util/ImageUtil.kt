package by.nrstudio.mvvm.util

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

class ImageSignature(private val currentVersion: String) : Key {

	override fun equals(key: Any?): Boolean {
		if (key is ImageSignature) {
			return currentVersion == key.currentVersion
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
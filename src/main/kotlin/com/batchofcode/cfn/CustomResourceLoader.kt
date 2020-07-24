package com.batchofcode.cfn

import com.batchofcode.cfn.exception.InvalidCustomResourceException

object CustomResourceLoader {
    fun load(customResourceClassName: String): CustomResource {
        val loadedHandler = javaClass.classLoader.loadClass(customResourceClassName)
        if (loadedHandler.interfaces.any { it.name == CustomResource::class.qualifiedName }) {
            return loadedHandler.getDeclaredConstructor().newInstance() as CustomResource
        }
        else {
            throw InvalidCustomResourceException("$customResourceClassName must implement CustomResource interface!")
        }
    }
}
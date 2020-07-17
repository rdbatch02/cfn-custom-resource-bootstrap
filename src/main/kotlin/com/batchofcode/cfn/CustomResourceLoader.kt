package com.batchofcode.cfn

import com.batchofcode.cfn.exception.InvalidCustomResourceException

object CustomResourceLoader {
    fun load(customResourceClassName: String): CustomResource {
        return when (val handlerInstance = javaClass.classLoader.loadClass(customResourceClassName)) {
            is CustomResource -> handlerInstance
            else -> throw InvalidCustomResourceException("$customResourceClassName must implement CustomResource interface!")
        }
    }
}
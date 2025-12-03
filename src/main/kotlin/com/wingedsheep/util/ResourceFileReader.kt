package com.wingedsheep.util

import java.nio.charset.StandardCharsets

class ResourceFileReader {

    fun readLinesFromResource(path: String): List<String> =
        object {}.javaClass.getResourceAsStream(path)
            ?.bufferedReader()
            ?.readLines()
            ?: error("Resource not found: $path")
}
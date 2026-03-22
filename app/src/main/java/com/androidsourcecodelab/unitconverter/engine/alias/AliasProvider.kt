package com.androidsourcecodelab.unitconverter.engine.alias


interface AliasProvider {
    fun resolve(input: String): String?

    // NEW → allows multi-token matching
    fun resolve(tokens: List<String>, index: Int): Pair<String, Int>? {
        return null
    }
}
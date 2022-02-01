package com.github.tomsfernandez.jsonld.lang

import com.intellij.icons.AllIcons
import com.intellij.json.JsonLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class JsonLdFileType : LanguageFileType(JsonLanguage.INSTANCE) {

    companion object {
        val INSTANCE = JsonLdFileType()
        val DEFAULT_EXTENSION = "jsonld"
    }

    override fun getName(): String {
        return "JSON-LD"
    }

    override fun getDescription(): String {
        return "JSON-LD"
    }

    override fun getDefaultExtension(): String {
        return DEFAULT_EXTENSION
    }

    override fun getIcon(): Icon {
        return AllIcons.FileTypes.Json
    }
}

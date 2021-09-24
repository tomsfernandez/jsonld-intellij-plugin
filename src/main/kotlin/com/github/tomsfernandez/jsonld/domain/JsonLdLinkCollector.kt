package com.github.tomsfernandez.jsonld.domain

import com.intellij.json.psi.*
import com.intellij.psi.PsiElement

class JsonLdLinkCollector : JsonElementVisitor() {

    private val collector = mutableListOf<JsonValue>()

    fun collectLinksIn(element: PsiElement): List<PsiElement> {
        element.accept(this)
        return collector
    }

    override fun visitObject(o: JsonObject) {
        o.propertyList.forEach { x -> x.accept(this) }
    }

    override fun visitElement(o: JsonElement) {
        when(o) {
            is JsonObject -> visitObject(o)
            is JsonArray -> visitArray(o)
            else -> {} // nothing
        }
    }

    override fun visitArray(o: JsonArray) {
        o.valueList.forEach { x -> x.accept(this) }
    }

    override fun visitProperty(property: JsonProperty) {
        val propValue = property.value
        if (property.name == "@id" && propValue is JsonLiteral) collector.add(propValue)
    }
}

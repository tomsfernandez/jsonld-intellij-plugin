package com.github.tomsfernandez.jsonld.domain

import com.intellij.json.psi.impl.JsonArrayImpl
import com.intellij.json.psi.impl.JsonObjectImpl
import com.intellij.json.psi.impl.JsonPropertyImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

// Only works for flattened version
object JsonLd {

    fun buildIdToElementMap(file: PsiFile): Map<String, PsiElement> {
        val graphNodes = findGraphArray(file)?.children ?: return emptyMap()
        return graphNodes.mapNotNull { element ->
            val id = findIdPropertyValue(element) ?: return@mapNotNull null
            (id to element)
        }.toMap()
    }

    private fun findGraphArray(file: PsiFile): PsiElement? {
        val objectNode = findObjectNode(file)
        if (objectNode !is JsonObjectImpl) return null

        val graphEntry = objectNode.findProperty("@graph")
        val possibleGraphArrayValue = graphEntry?.value
        if (possibleGraphArrayValue !is JsonArrayImpl) return null
        return possibleGraphArrayValue
    }

    private fun findObjectNode(file: PsiFile): PsiElement? {
        return file.children.find { elem ->
            when (elem) {
                is JsonObjectImpl -> true
                else -> false
            }
        }
    }

    private fun findIdPropertyValue(elem: PsiElement): String? {
        val idProp = elem.children.find { x ->
            when (x) {
                is JsonPropertyImpl -> x.name == "@id"
                else -> false
            }
        }
        return idProp?.children?.get(1)?.text
    }
}

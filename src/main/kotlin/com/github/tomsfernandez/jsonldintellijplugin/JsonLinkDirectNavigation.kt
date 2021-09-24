package com.github.tomsfernandez.jsonldintellijplugin

import com.intellij.json.JsonElementTypes.STRING_LITERAL
import com.intellij.json.psi.impl.JsonArrayImpl
import com.intellij.json.psi.impl.JsonObjectImpl
import com.intellij.json.psi.impl.JsonPropertyImpl
import com.intellij.navigation.DirectNavigationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType

class JsonLinkDirectNavigation : DirectNavigationProvider {

    override fun getNavigationElement(element: PsiElement): PsiElement? {
        if (isNotString(element)) return null
        else if (!fileIsJsonLd(element.containingFile)) return null
        val graphArrayNode = findGraphArray(element.containingFile)
        return graphArrayNode?.children?.find { elem ->
            findIdPropertyValue(elem) == element.text
        }
    }

    private fun fileIsJsonLd(file: PsiFile): Boolean {
        return file.name.endsWith("jsonld")
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

    private fun isNotString(element: PsiElement) = element.elementType != STRING_LITERAL

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

package com.github.tomsfernandez.jsonld.extensions

import com.github.tomsfernandez.jsonld.common.fileIsJsonLd
import com.github.tomsfernandez.jsonld.common.isStringLiteral
import com.github.tomsfernandez.jsonld.services.JsonLdElementIndexService
import com.intellij.json.psi.JsonProperty
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parents

class LinkTargetNotFoundAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val shouldAnnotate = isStringLiteral(element) && fileIsJsonLd(element.containingFile)
        if (!shouldAnnotate) return

        val possibleProperty = element.parent

        if (possibleProperty !is JsonProperty) return
        else if (possibleProperty.name != "@id") return
        else if (isTopLevelElementId(element)) return

        val elements = getCachedJson(element)
        val linkTarget = elements?.get(possibleProperty.value?.text)
        if (linkTarget == null) {
            holder
                .newAnnotation(HighlightSeverity.WEAK_WARNING, "Link target doesn't exist in local json-ld")
                .range(element)
                .create()
        }
    }

    private fun isTopLevelElementId(element: PsiElement) = element.parents.toList().size <= 6

    private fun getCachedJson(element: PsiElement) = service<JsonLdElementIndexService>().get(element.containingFile.virtualFile.path)
}

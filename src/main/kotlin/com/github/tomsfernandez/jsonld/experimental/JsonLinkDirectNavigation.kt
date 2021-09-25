package com.github.tomsfernandez.jsonld.experimental

import com.github.tomsfernandez.jsonld.common.fileIsJsonLd
import com.github.tomsfernandez.jsonld.common.isStringLiteral
import com.github.tomsfernandez.jsonld.domain.JsonLd
import com.github.tomsfernandez.jsonld.services.JsonLdElementIndexService
import com.intellij.navigation.DirectNavigationProvider
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement

class JsonLinkDirectNavigation : DirectNavigationProvider {

    override fun getNavigationElement(element: PsiElement): PsiElement? {
        if (!isStringLiteral(element)) return null
        else if (!fileIsJsonLd(element.containingFile)) return null
        return service<JsonLdElementIndexService>().get(element.containingFile.virtualFile.path)?.get(element.text)
    }
}

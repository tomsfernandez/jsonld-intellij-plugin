package com.github.tomsfernandez.jsonld.common

import com.intellij.json.JsonElementTypes
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType

fun fileIsJsonLd(file: PsiFile): Boolean {
    return file.name.endsWith("jsonld")
}

fun isStringLiteral(element: PsiElement) = element.elementType == JsonElementTypes.STRING_LITERAL

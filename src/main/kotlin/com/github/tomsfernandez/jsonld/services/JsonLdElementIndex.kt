package com.github.tomsfernandez.jsonld.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.externalSystem.service.project.manage.ProjectDataService
import com.intellij.psi.PsiElement

@Service
class JsonLdElementIndexService {

    private val cache = mutableMapOf<String, Map<String, PsiElement>>()

    fun get(filePath: String): Map<String, PsiElement>? = cache[filePath]
    fun add(filePath: String, links: Map<String, PsiElement>) = cache.put(filePath, links)
}

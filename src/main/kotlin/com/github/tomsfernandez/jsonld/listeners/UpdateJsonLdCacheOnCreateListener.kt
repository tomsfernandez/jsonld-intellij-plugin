package com.github.tomsfernandez.jsonld.listeners

import com.github.tomsfernandez.jsonld.domain.JsonLd
import com.github.tomsfernandez.jsonld.services.JsonLdElementIndexService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class UpdateJsonLdCacheOnCreateListener : FileEditorManagerListener {

    private val log: Logger = Logger.getInstance(this::class.java)

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val cacheService = service<JsonLdElementIndexService>()
        val psiFile = PsiManager.getInstance(source.project).findFile(file)

        if (psiFile == null) {
            log.error("Didn't find PSI File for ${file.path}")
            return
        }

        val entries = JsonLd.buildIdToElementMap(psiFile)
        log.debug("Cached ${file.path} with ${entries.size} elements")
        cacheService.add(file.path, entries)
    }
}

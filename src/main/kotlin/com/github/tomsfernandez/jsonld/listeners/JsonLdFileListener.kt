package com.github.tomsfernandez.jsonld.listeners

import com.github.tomsfernandez.jsonld.domain.JsonLd
import com.github.tomsfernandez.jsonld.services.JsonLdElementIndexService
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class JsonLdFileListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val cacheService = source.project.getService(JsonLdElementIndexService()::class.java)
        val psiFile = PsiManager.getInstance(source.project).findFile(file)
        println("Caching....")
        if (psiFile == null) return

        val entries = JsonLd.buildIdToElementMap(psiFile)
        println("Cached ${file.path} with ${entries.size} elements")
        cacheService.add(file.path, entries)
    }
}

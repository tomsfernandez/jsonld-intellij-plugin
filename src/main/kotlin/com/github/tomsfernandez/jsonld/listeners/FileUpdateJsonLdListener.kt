package com.github.tomsfernandez.jsonld.listeners

import com.github.tomsfernandez.jsonld.domain.JsonLd
import com.github.tomsfernandez.jsonld.services.JsonLdElementIndexService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

class FileUpdateJsonLdListener(private val project: Project): BulkFileListener {

    override fun after(events: MutableList<out VFileEvent>) {
        val jsonLdEvents = events.filter { it.path.endsWith("jsonld") }
        val cache = service<JsonLdElementIndexService>()
        jsonLdEvents.forEach { e ->
            withPsiFile(e.file) { psiFile ->
                when(e) {
                    is VFileDeleteEvent -> println("Detected delete!")
                    is VFileContentChangeEvent -> cache.add(e.path, JsonLd.buildIdToElementMap(psiFile))
                }
            }
        }
    }

    private fun withPsiFile(file: VirtualFile?, block: (PsiFile) -> Unit) {
        if (file == null) return
        val psiFile = PsiManager.getInstance(project).findFile(file)
        if (psiFile != null) block(psiFile)
    }

}

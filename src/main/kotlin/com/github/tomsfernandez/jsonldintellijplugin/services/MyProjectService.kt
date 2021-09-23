package com.github.tomsfernandez.jsonldintellijplugin.services

import com.intellij.openapi.project.Project
import com.github.tomsfernandez.jsonldintellijplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

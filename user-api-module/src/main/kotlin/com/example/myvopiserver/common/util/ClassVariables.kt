package com.example.myvopiserver.common.util

import jakarta.annotation.PostConstruct
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.springframework.stereotype.Component
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

// unused
@Component
class ClassVariables {

    private val moduleNameDir = "\\user-api-module"
    private val sourceDirName = "\\src\\main\\kotlin"
    private val projectName = "\\com\\example\\myvopiserver"
    private val rootPackageName = "com.example.myvopiserver"
    private val entityDirName = "\\domain"

    @PostConstruct
    fun initBuild() {
        val classPath = File("").absolutePath.plus("${moduleNameDir}${sourceDirName}${projectName}${entityDirName}")
        val className = File(classPath).listFiles { file -> file.isFile && file.name.endsWith(".kt") && !file.name.contains("BaseTime") }
            ?.map { file -> file.nameWithoutExtension }
            ?: emptyList()
        className.forEach { name ->
            val clazz = Class.forName("$rootPackageName.domain.$name").kotlin
            entityBuild(clazz)
        }
    }

    companion object {
        val ColumnNamesByClass = mutableMapOf<KClass<*>, List<String>>()
        val TablesNamesByClass = mutableMapOf<KClass<*>, String?>()

        fun <T : Any> entityBuild(clazz: KClass<T>) {
            val columnProperties = clazz.memberProperties
            val columns = columnProperties.mapNotNull { it.javaField?.annotations }
                .flatMap { it.toList() }
                .filterIsInstance<Column>()
                .map { it.name }
            val joinColumns = columnProperties.mapNotNull { it.javaField?.annotations }
                .flatMap { it.toList() }
                .filterIsInstance<JoinColumn>()
                .map { it.name }
            val allColumnNames = columns + joinColumns
            ColumnNamesByClass[clazz] = allColumnNames

            val tableAnnotation = clazz.annotations
                .filterIsInstance<Table>()
                .map { it.name }
                .firstOrNull()
            TablesNamesByClass[clazz] = tableAnnotation
        }
    }
}
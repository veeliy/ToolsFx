package me.leon.toolsfx.plugin

import java.io.File
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.RadioButton
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import me.leon.*
import me.leon.ext.DEFAULT_SPACING
import me.leon.ext.cast
import me.leon.ext.fx.clipboardText
import me.leon.ext.fx.copy
import me.leon.ext.fx.fileDraggedHandler
import me.leon.ext.properText
import me.leon.ext.toFile
import me.leon.toolsfx.plugin.ext.ImageServiceType
import me.leon.toolsfx.plugin.ext.locationServiceType
import me.leon.toolsfx.plugin.ext.serviceTypeMap
import tornadofx.*

class ImageProcessView : PluginFragment("ImageProcessView") {
    override val version = "v1.1.0"
    override val date: String = "2022-12-17"
    override val author = "Leon406"
    override val description = "图片模块"
    private var taInput: TextArea by singleAssign()
    private var taOutput: TextArea by singleAssign()
    private var img: ImageView by singleAssign()

    private val fileMode = SimpleBooleanProperty(true)
    private val showImage = SimpleBooleanProperty(false)

    private val eventHandler = fileDraggedHandler {
        taInput.text =
            if (fileMode.get()) {
                it.first().absolutePath
            } else {
                it.first().properText()
            }
    }
    private var imageServiceType: ImageServiceType = ImageServiceType.FIX_PNG
    private val controller: ImageController by inject()

    private val inputText: String
        get() = taInput.text.trim()
    private val outputText: String
        get() = taOutput.text

    override val root = vbox {
        paddingAll = DEFAULT_SPACING
        spacing = DEFAULT_SPACING
        title = "ImageProcessView"
        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = DEFAULT_SPACING
            label(messages["input"])
            checkbox("文件模式", fileMode)
            button(graphic = imageview(IMG_IMPORT)) {
                tooltip(messages["pasteFromClipboard"])
                action { taInput.text = clipboardText() }
            }
        }

        taInput = textarea {
            isWrapText = true
            onDragEntered = eventHandler
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            paddingTop = DEFAULT_SPACING
            paddingBottom = DEFAULT_SPACING
            spacing = DEFAULT_SPACING
            label("function:")
            tilepane {
                hgap = 8.0
                vgap = 8.0
                alignment = Pos.TOP_LEFT
                prefColumns = 4
                togglegroup {
                    serviceTypeMap.forEach {
                        radiobutton(it.key) {
                            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
                            if (it.value == ImageServiceType.FIX_PNG) isSelected = true
                        }
                    }
                    selectedToggleProperty().addListener { _, _, new ->
                        imageServiceType = new.cast<RadioButton>().text.locationServiceType()
                        println(imageServiceType)
                    }
                }
            }
        }

        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = DEFAULT_SPACING
            paddingLeft = DEFAULT_SPACING

            button(messages["run"], imageview(IMG_RUN)) { action { doProcess() } }
        }
        hbox {
            spacing = DEFAULT_SPACING
            alignment = Pos.CENTER_LEFT
            label(messages["output"])
            button(graphic = imageview(IMG_COPY)) {
                tooltip(messages["copy"])
                action {
                    if (showImage.get()) {
                        img.image.copy()
                    } else {
                        outputText.copy()
                    }
                }
            }
            button(graphic = imageview(IMG_UP)) {
                tooltip(messages["up"])
                action {
                    taInput.text = outputText
                    taOutput.text = ""
                }
            }
            button("stegSolve") { action { startStegSolve() } }
        }

        stackpane {
            taOutput = textarea {
                promptText = messages["outputHint"]
                isWrapText = true
                visibleWhen(!showImage)
            }
            img = imageview()
            scrollpane(true) {
                visibleWhen(showImage)
                content = img
            }
        }
    }

    private fun doProcess() {
        if (inputText.isEmpty()) return
        runAsync { controller.process(imageServiceType, inputText, fileMode.get()) } ui
            {
                showImage.value = it !is String
                when (it) {
                    is String -> taOutput.text = it
                    is ByteArray -> img.image = Image(it.inputStream())
                    is Image -> img.image = it
                }
            }
    }

    companion object {
        private val javaHome = System.getProperty("java.home")
        private val userDir = System.getProperty("user.dir")
        private var command: Array<String>? = null

        init {
            userDir
                .toFile()
                .walk()
                .find { it.extension == "jar" && it.name.startsWith("StegSolve") }
                ?.run {
                    command =
                        arrayOf(
                            "$javaHome${File.separator}bin${File.separator}java",
                            "-jar",
                            "-Dsun.java2d.uiScale=${ToolsApp.scale}",
                            absolutePath
                        )
                }
        }

        private fun startStegSolve() = Runtime.getRuntime().exec(command)
    }
}

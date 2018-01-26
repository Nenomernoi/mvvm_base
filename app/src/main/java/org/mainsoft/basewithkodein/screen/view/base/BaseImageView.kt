package org.mainsoft.basewithkodein.screen.view.base

import java.io.File

interface BaseImageView : BaseView {
    fun initFile(path: File?)
    fun initImage(path: String?)
    fun openAlbums()
    fun openCamera()
}

package com.tonywis.movieexplorer.utils.requests

/**
 * Created by Tony on 02/02/2018.
 */

abstract class TaskComplete<TypeData> {

    var result: TypeData? = null

    abstract fun run()
}

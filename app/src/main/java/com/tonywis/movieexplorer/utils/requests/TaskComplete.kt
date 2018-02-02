package com.tonywis.movieexplorer.utils.requests

/**
 * Created by Tony on 02/02/2018.
 */

import com.tonywis.movieexplorer.models.Answer

abstract class TaskComplete<TypeData> {

    var result: Answer<TypeData>? = null

    abstract fun run()
}

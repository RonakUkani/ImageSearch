package com.app.imagesearchdemo.api


data class AppResponse<T>(
    val success: Boolean = false,
    val status: Int = 0,
    val data: T? = null
)


object ApiEndpoints {
    /*---------------------------------------------------------------
     * APIs ApiEndpoints of TailorApp
     *---------------------------------------------------------------
     */
    const val IMAGE_SEARCH ="3/gallery/search/1"


}

object ApiParameters {
    /*-----------------------------------------------------------------
     * Parameters
     *-----------------------------------------------------------------
     */
    const val QUERY_PARAM ="q"


}
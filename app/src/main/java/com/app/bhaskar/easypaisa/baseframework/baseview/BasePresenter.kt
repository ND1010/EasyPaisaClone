package com.pa.baseframework.baseview

interface BasePresenter {
    /**
     * Method that should signal the appropriate view to show the appropriate error with the
     * provided message.
     */
    fun onError(message: String)

    fun registerBus()

    fun unRegisterBus()
}
package me.wkbin.mvihelper.core


internal interface ViewModelContract<EVENT> {
    fun process(viewEvent:EVENT)
}
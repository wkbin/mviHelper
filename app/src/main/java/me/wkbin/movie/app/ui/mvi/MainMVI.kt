package me.wkbin.movie.app.ui.mvi

import android.graphics.Bitmap


/**
 * 在继续之前，让我们重新强调一下MVI架构中的一些基本术语。
 *
 * ViewState：顾名思义，这是Model层的一部分，我们的视图要观察这个Model的状态变化。
 * ViewState应该代表视图在任何给定时间的当前状态。所以这个类应该有我们的视图所依赖的所有变量内容。
 * 每次有任何用户的输入/动作，我们都会暴露这个类的修改过的副本（以保持之前没有被修改的状态）。
 * 我们可以使用Kotlin的Data Class来创建这个Model。
 */

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}

data class MainViewState(
    val fetchStatus: FetchStatus,
    val bitmap: Bitmap?
)

/**
 * ViewEffect：在Android中，我们有一些动作更像是fire-and-forget，
 * 例如Toast，在这些情况下，我们不能使用ViewState，
 * 因为它保持状态。这意味着，如果我们使用ViewState来显示Toast，
 * 它将在配置改变或每次有新的状态时再次显示，除非我们通过 "toast is shown "事件来重置其状态。
 * 如果你不希望这样做，你可以使用ViewEffect，因为它是基于SingleLiveEvent的，
 * 不需要维护状态。ViewEffect也是我们Model的一部分，我们可以使用Kotlin的密封类来创建它。
 */

sealed class MainViewEffect

/**
 * ViewEvent：它表示用户可以在视图上执行的所有动作/事件。它用于将用户的输入/动作传递给ViewModel。
 * 我们可以使用Kotlin的Sealed Class来创建这个事件集。
 */
sealed class MainViewEvent {
    /**
     * 下拉刷新
     */
    object OnSwipeRefresh: MainViewEvent()

    /**
     * 上拉加载
     */
    object OnLoadMore :MainViewEvent()
}
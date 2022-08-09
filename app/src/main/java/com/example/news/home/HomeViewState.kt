/** Activity or Fragment controller class*/
sealed class HomeViewState {

    data class ShowToast(val msg: String) : HomeViewState()

    data class ShowDialog(val msg: String, val title: String? = null) : HomeViewState()

    data class GetDataFail(val msg: String) : HomeViewState()

    object Loading : HomeViewState()

    object ScrollToUp : HomeViewState()

    data class GetDataSuccess(val isShow: Boolean) : HomeViewState()

    object GetLoadMoreDataFail : HomeViewState()
}
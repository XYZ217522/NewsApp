/** Activity or Fragment controller class*/
sealed class HomeViewState {

    class ShowToast(val msg: String) : HomeViewState()

    class ShowDialog(val msg: String, val title: String? = null) : HomeViewState()

    class GetDataFail(val msg: String) : HomeViewState()

    object Loading : HomeViewState()

    object ScrollToUp : HomeViewState()

    class GetDataSuccess(val isShow: Boolean) : HomeViewState()

    object GetLoadMoreDataFail : HomeViewState()
}
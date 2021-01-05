package org.mainsoft.base.screen.model.grid

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import org.mainsoft.base.lib.Action
import org.mainsoft.base.screen.model.base.BaseApiUseCase

class GridUseCase : BaseApiUseCase() {

    fun getList(state: GridViewState?, page: Int): ReceiveChannel<Action<GridViewState>> = produceActions {
        send { copy(loading = page == 0, error = null, page = page) }
        try {

            val results = state?.data ?: mutableListOf()
            results.addAll(urls)
            Thread.sleep(1000L)

            send { copy(data = results, loading = false, page = page) }
        } catch (e: Exception) {
            send { copy(error = e, loading = false, page = page) }
            Log.e("GridUseCase", e.message, e)
        }
    }

    private val urls = mutableListOf(
            "https://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
            "https://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
            "https://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
            "https://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
            "https://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
            "https://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
            "https://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
            "https://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
            "https://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
            "https://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
            "https://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
            "https://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
            "https://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg",
            "https://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
            "https://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
            "https://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
            "https://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
            "https://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
            "https://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
            "https://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
            "https://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
            "https://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
            "https://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
            "https://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
            "https://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
            "https://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg"
    )


}


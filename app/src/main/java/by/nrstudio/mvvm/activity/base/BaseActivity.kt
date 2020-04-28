package by.nrstudio.mvvm.activity.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		supportFragmentManager.fragments.forEach { nav ->
			nav.childFragmentManager.fragments.forEach {
				it?.onActivityResult(requestCode, resultCode, data)
			}
		}
	}
/*
    private fun reloadData() {
        supportFragmentManager.fragments.forEach { nav ->
            nav.childFragmentManager.fragments.forEach {
                if (it is BaseFragment?) {
                    it?.onReloadData()
                }
            }
        }
    }
 */

	abstract fun getLayout(): Int
}

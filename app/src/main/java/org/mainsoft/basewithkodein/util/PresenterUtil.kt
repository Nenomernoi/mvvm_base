package org.mainsoft.basewithkodein.util

import org.mainsoft.basewithkodein.screen.fragment.ExampleFragment
import org.mainsoft.basewithkodein.screen.fragment.ExampleListFragment
import org.mainsoft.basewithkodein.screen.fragment.ExamplePageFragment
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.ExampleListPresenter
import org.mainsoft.basewithkodein.screen.presenter.ExamplePagePresenter
import org.mainsoft.basewithkodein.screen.presenter.ExamplePresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleListView
import org.mainsoft.basewithkodein.screen.view.ExamplePageView
import org.mainsoft.basewithkodein.screen.view.ExampleView
import org.mainsoft.basewithkodein.screen.view.base.BaseView


class PresenterUtil {

    private val mapPresenter: HashMap<String, BasePresenter> = hashMapOf()

    fun <P : BasePresenter> getPresenter(v: BaseView): P {

        val name = v::class.java.simpleName

        val p = mapPresenter[name] ?: when (name) {
            ExampleFragment::class.java.simpleName -> {
                saveAndGetPresenter(name, ExamplePresenter(v as ExampleView))
            }
            ExampleListFragment::class.java.simpleName -> {
                saveAndGetPresenter(name, ExampleListPresenter(v as ExampleListView))
            }
            ExamplePageFragment::class.java.simpleName -> {
                saveAndGetPresenter(name, ExamplePagePresenter(v as ExamplePageView))
            }
            else -> {
                null as P
            }
        }
        p as P
        p.setView(v)
        return p
    }

    private fun saveAndGetPresenter(name: String, p: BasePresenter): BasePresenter {
        mapPresenter[name] = p
        return p
    }

    fun removePresenter(fragmentClass: Class<out BaseFragment>) {
        mapPresenter.remove(fragmentClass.simpleName)
    }

    fun clearAll() {
        mapPresenter.clear()
    }

}
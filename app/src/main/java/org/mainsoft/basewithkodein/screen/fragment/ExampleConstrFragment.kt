package org.mainsoft.basewithkodein.screen.fragment

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_constr.button
import kotlinx.android.synthetic.main.fragment_constr.button2
import kotlinx.android.synthetic.main.fragment_constr.cnstMain
import org.kodein.direct
import org.kodein.generic.instance
import org.mainsoft.basewithkodein.App
import org.mainsoft.basewithkodein.R
import org.mainsoft.basewithkodein.screen.fragment.base.BaseFragment
import org.mainsoft.basewithkodein.screen.presenter.ExampleConstrPresenter
import org.mainsoft.basewithkodein.screen.presenter.base.BasePresenter
import org.mainsoft.basewithkodein.screen.view.ExampleConstView

class ExampleConstrFragment : BaseFragment(), ExampleConstView {

    override fun getLayout() = R.layout.fragment_constr

    init {
        presenter = App.kodein.direct.instance<ExampleConstView, ExampleConstrPresenter>(arg = this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    //FOR PAGER
    companion object {
        fun newInstance(position: Int): ExampleConstrFragment {
            val pageFragment = ExampleConstrFragment()
            val arguments = Bundle()
            arguments.putLong(BasePresenter.ARGUMENT_PAGE_NUMBER, position.toLong())
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////

    override fun initListeners() {
        button?.setOnClickListener {
            val set = ConstraintSet()
            set.clone(cnstMain)
            changeConstraints(set)
            TransitionManager.beginDelayedTransition(cnstMain)
            set.applyTo(cnstMain)
        }
        button2?.setOnClickListener {
            val set = ConstraintSet()
            set.clone(cnstMain)
            changeDefConstraints(set)
            TransitionManager.beginDelayedTransition(cnstMain)
            set.applyTo(cnstMain)
        }
    }

    private fun changeConstraints(set: ConstraintSet) {

        set.connect(R.id.textView, ConstraintSet.LEFT, R.id.textView2, ConstraintSet.LEFT)
        set.connect(R.id.textView, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 48)

        set.constrainCircle(R.id.button2, R.id.button, 500, 280f)

    }

    private fun changeDefConstraints(set: ConstraintSet) {

        set.connect(R.id.textView, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(R.id.textView, ConstraintSet.TOP, R.id.imageView, ConstraintSet.TOP, 48)

        set.constrainCircle(R.id.button2, R.id.button, 300, 311f)

    }

}

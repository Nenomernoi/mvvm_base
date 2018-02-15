package org.mainsoft.basewithkodein.util

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.DialogInterface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.widget.TextView
import org.mainsoft.basewithkodein.R
import java.util.Calendar

class DialogUtil {
    companion object {

        fun showErrorPermissionDialog(context: Context, listener: DialogInterface.OnClickListener,
                                      listenerCancel: DialogInterface.OnClickListener) {
            baseDialog(context, R.string.warning,
                    R.string.error_full_permission, R.string.cancel,
                    R.string.ok, listener, listenerCancel)
        }

        /////////////////////////////////////////////////////////////////////////////////////////

        fun showImageDialog(context: Context, list: List<String>, listener: ListenerDialogList) {
            baseListDialog(context, list, R.string.popup_title, listener)
        }

        fun showWarningErrorDialog(context: Context, message: String?,
                                   listener: DialogInterface.OnClickListener) {
            baseMessageDialog(context, R.string.warning, message, R.string.ok, listener)
        }

        /////////////////////////////////////////////////////////////////////////////////////////

        fun showBaseCalendarDialog(context: Context, date: Long?, dateMin: Long?, dateMax: Long?,
                                   listener: OnDateSetListener) {
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.timeInMillis = date
            }

            val dialog = DatePickerDialog(context, listener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH))

            val calMax = Calendar.getInstance()
            if (dateMax != null) {
                calMax.timeInMillis = dateMax
            }
            dialog.datePicker.maxDate = calMax.timeInMillis

            if (dateMin != null) {
                val calMin = Calendar.getInstance()
                calMin.timeInMillis = dateMin
                dialog.datePicker.minDate = calMin.timeInMillis
            }

            dialog.show()
        }

        /////////////////////////////////////////////////////////////////////////////////////////

        private fun baseDialog(context: Context, title: Int, message: Int, cancel: Int, ok: Int,
                               listener: DialogInterface.OnClickListener,
                               listenerCancel: DialogInterface.OnClickListener) {
            baseDialog(context, title, context.resources.getString(message),
                    cancel, ok, listener, listenerCancel)
        }

        private fun baseDialog(context: Context, title: Int,
                               message: String?, cancel: Int,
                               ok: Int, listener: DialogInterface.OnClickListener,
                               listenerCancel: DialogInterface.OnClickListener) {
            val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
            builder.setMessage(message)
            builder.setTitle(title)
            builder.setPositiveButton(ok, listener)
            builder.setNegativeButton(cancel, listenerCancel)

            val dialog: AlertDialog = builder.show()

            val face = ResourcesCompat.getFont(context, R.font.good_dog)
            dialog.findViewById<TextView>(android.R.id.message)?.typeface = face
            dialog.findViewById<TextView>(android.R.id.title)?.typeface = face
        }

        private fun baseMessageDialog(context: Context, title: Int, message: String?, ok: Int,
                                      listener: DialogInterface.OnClickListener) {
            val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
            builder.setMessage(message)
            builder.setTitle(title)
            builder.setPositiveButton(ok, listener)

            val dialog: AlertDialog = builder.show()

            val face = ResourcesCompat.getFont(context, R.font.good_dog)
            dialog.findViewById<TextView>(android.R.id.message)?.typeface = face
            dialog.findViewById<TextView>(android.R.id.title)?.typeface = face
        }

        private fun baseListDialog(context: Context, list: List<String>,
                                   title: Int, listener: ListenerDialogList) {
            val items = list.toTypedArray()
            val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
            builder.setTitle(title)
            builder.setItems(items) { dialog, position -> listener.onSelectItem(position, dialog) }
            builder.show()
        }

        interface ListenerDialogList {
            fun onSelectItem(position: Int, dialog: DialogInterface)
        }
    }
}
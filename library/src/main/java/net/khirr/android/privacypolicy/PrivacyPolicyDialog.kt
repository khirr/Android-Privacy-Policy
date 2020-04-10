package net.khirr.android.privacypolicy

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.net_khirr_dialog_privacy_policies.view.*
import net.khirr.android.privacypolicy.R
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.Drawable
import java.util.*
import android.content.res.ColorStateList

class PrivacyPolicyDialog(private val context: AppCompatActivity,
                          private val termsOfServiceUrl: String,
                          private val privacyPolicyUrl: String) {

    var backgroundColor = Color.parseColor("#ffffff")
    var titleTextColor = Color.parseColor("#222222")
    var subtitleTextColor = Color.parseColor("#757575")
    var linkTextColor = Color.parseColor("#000000")
    var termsOfServiceTextColor = Color.parseColor("#757575")


    //  Accept Button
    var acceptButtonColor = Color.parseColor("#222222")
    var acceptTextColor = Color.parseColor("#ffffff")
    var acceptText = context.getString(R.string.net_khirr_accept)

    //  Cancel
    var cancelText = context.getString(R.string.net_khirr_cancel)
    var cancelTextColor = Color.parseColor("#757575")

    //  Strings
    var title = context.getString(R.string.net_khirr_terms_of_service)
    var termsOfServiceSubtitle = context.getString(R.string.net_khirr_terms_of_service_subtitle)

    var europeOnly = false

    private var lines = ArrayList<String>()

    companion object {
        private const val sharedPreferencesName = "netKhirrPolicies"
        private const val fieldPoliciesAccepted = "netKhirrPoliciesAccepted"
    }

    interface OnClickListener {
        fun onAccept(isFirstTime: Boolean)
        fun onCancel()
    }

    var onClickListener: OnClickListener? = null
    var dialog: AlertDialog? = null

    private var sharedPref = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    private var adapter: PrivacyPoliciesAdapter? = null

    private var policiesAccepted: Boolean
        get() = sharedPref.getBoolean(fieldPoliciesAccepted, false)
        set(value) = sharedPref.edit()
                .putBoolean(fieldPoliciesAccepted, value)
                .apply()

    fun addPoliceLine(line: String) {
        lines.add(line)
    }

    private fun loadLayout(): View {
        val inflater = context.layoutInflater
        val layout = inflater.inflate(R.layout.net_khirr_dialog_privacy_policies, null)

        layout.container.setBackgroundColor(backgroundColor)

        layout.termsOfServiceTitle.text = title
        layout.termsOfServiceTitle.setTextColor(titleTextColor)

        layout.termsOfServiceSubtitleTextView.text = toHtml(termsOfServiceSubtitle)
        layout.termsOfServiceSubtitleTextView.movementMethod = LinkMovementMethod.getInstance()
        layout.termsOfServiceSubtitleTextView.setLinkTextColor(linkTextColor)
        layout.termsOfServiceSubtitleTextView.setTextColor(subtitleTextColor)

        layout.acceptTextView.setTextColor(acceptTextColor)
        setBackgroundColor(layout.acceptButton, acceptButtonColor)
        layout.acceptTextView.text = acceptText

        layout.cancelTextView.text = cancelText
        layout.cancelTextView.setTextColor(cancelTextColor)

        layout.acceptButton.setOnClickListener {
            dismiss()
            onClickListener?.onAccept(true)
            policiesAccepted = true
        }

        layout.cancelButton.setOnClickListener {
            dismiss()
            onClickListener?.onCancel()
            policiesAccepted = false
        }

        layout.policiesRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = PrivacyPoliciesAdapter(termsOfServiceTextColor)
        layout.policiesRecyclerView.adapter = adapter

        adapter?.updateDataSet(lines)


        return layout
    }

    private fun dismiss() {
        if (!context.isFinishing && dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }

    fun show() {
        if (policiesAccepted) {
            onClickListener?.onAccept(false)
            return
        }

        if (europeOnly && !EUHelper.isEu(context)) {
            onClickListener?.onAccept(false)
            return
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(loadLayout())
        builder.setCancelable(false)
        dialog = builder.show()
    }

    private fun toHtml(res: Int): Spanned {
        return toHtml(context.getString(res))
    }

    private fun toHtml(body: String): Spanned {
        val body = body
                .replace("{accept}", context.getString(R.string.net_khirr_accept))
                .replace("{privacy}", "<a href=\"$privacyPolicyUrl\">")
                .replace("{/privacy}", "</a>")
                .replace("{terms}", "<a href=\"$termsOfServiceUrl\">")
                .replace("{/terms}", "</a>")
                .replace("{", "<")
                .replace("}", ">")
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(body)
        }
    }

    private fun setBackgroundColor(view: View?, resColor: Int) {
        if (view == null || view.background == null)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view.background is RippleDrawable) {
            val rippleDrawable = view.background as RippleDrawable
            rippleDrawable.setColorFilter(resColor, PorterDuff.Mode.MULTIPLY)
        } else {
            if (view.background is ColorDrawable) {
                //  Non ripple
                val drawable = view.background as ColorDrawable
                //  Solid
                drawable.color = resColor
            } else if (view.background is GradientDrawable) {
                //  Non ripple
                val drawable = view.background as GradientDrawable
                //  Solid
                drawable.setColor(resColor)
            }
        }
    }
}
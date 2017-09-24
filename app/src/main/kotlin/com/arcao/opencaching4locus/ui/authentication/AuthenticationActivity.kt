package com.arcao.opencaching4locus.ui.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.arcao.opencaching4locus.R
import com.arcao.opencaching4locus.data.account.AccountType
import com.arcao.opencaching4locus.databinding.ActivityAuthenticationBinding
import com.arcao.opencaching4locus.ui.base.BaseActivity
import com.arcao.opencaching4locus.ui.base.constants.AppConstants
import com.arcao.opencaching4locus.ui.base.util.showError
import javax.inject.Inject

@Suppress("NOTHING_TO_INLINE")
class AuthenticationActivity : BaseActivity() {
    @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var accountType: AccountType

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountType = (intent.getSerializableExtra(EXTRA_ACCOUNT_TYPE) ?: AccountType.OPENCACHING_PL) as AccountType

        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                @Suppress("OverridingDeprecatedMember")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean = checkUrl(url)

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showProgress()
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    // don't hide progress bar for OAUTH_CALLBACK_URL
                    if (!url.startsWith(AppConstants.OAUTH_CALLBACK_URL))
                        hideProgress()
                }
            }
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthenticationViewModel::class.java)
        viewModel.authorizationState.observe(this, Observer {
            when (it) {
                is AuthenticationStarted, AccessTokenRequestSent -> showProgress()
                is RequestTokenReceived -> openAuthorizeUrl(it.url)
                is AuthenticationError -> showError(it.throwable)
                is AuthenticationSuccess -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })

        if (savedInstanceState == null)
            viewModel.retrieveRequestToken(accountType)
    }

    private inline fun showProgress() {
        binding.isLoading = true
    }

    private inline fun hideProgress() {
        binding.isLoading = false
    }

    private inline fun openAuthorizeUrl(url: String) {
        binding.webView.loadUrl(url)
    }

    private fun checkUrl(url: String): Boolean = when {
        url.startsWith(AppConstants.OAUTH_CALLBACK_URL) -> {
            viewModel.retrieveAccessToken(accountType, Uri.parse(url))
            true
        }
        else -> false
    }


    companion object {
        const val EXTRA_ACCOUNT_TYPE = "ACCOUNT_TYPE"
        fun createIntent(context: Context, accountType: AccountType): Intent = Intent(context, AuthenticationActivity::class.java).apply {
            putExtra(EXTRA_ACCOUNT_TYPE, accountType)
        }
    }
}

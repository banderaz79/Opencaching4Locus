package com.arcao.opencaching4locus.data.oauth.provider

import com.arcao.opencaching4locus.data.okapi.OkApiServiceType
import com.github.scribejava.core.builder.api.DefaultApi10a
import com.github.scribejava.core.builder.api.OAuth1SignatureType
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.utils.OAuthEncoder

open class OpencachingOAuthProvider(private val serviceType: OkApiServiceType) : DefaultApi10a() {
    private val requestTokenEndpoint = "${serviceType.endpoint}services/oauth/request_token"
    private val accessTokenEndpoint = "${serviceType.endpoint}services/oauth/access_token"
    private val authorizationUrl = "${serviceType.endpoint}services/oauth/authorize"


    override fun getRequestTokenEndpoint(): String {
        return requestTokenEndpoint
    }

    override fun getAccessTokenEndpoint(): String {
        return accessTokenEndpoint
    }

    override fun getAuthorizationUrl(requestToken: OAuth1RequestToken): String {
        return authorizationUrl + "?oauth_token=" + OAuthEncoder.encode(requestToken.token)
    }

    override fun getSignatureType(): OAuth1SignatureType = OAuth1SignatureType.QueryString
}
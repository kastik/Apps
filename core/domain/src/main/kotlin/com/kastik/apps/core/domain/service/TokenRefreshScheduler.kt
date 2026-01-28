package com.kastik.apps.core.domain.service


interface TokenRefreshScheduler {
    fun scheduleRefresh()
    fun cancelRefresh()
}
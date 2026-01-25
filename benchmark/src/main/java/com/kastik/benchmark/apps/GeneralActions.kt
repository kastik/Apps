package com.kastik.benchmark.apps

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.textAsString

fun MacrobenchmarkScope.launchAppAndDismissSigningDialog() {
    pressHome()
    startActivityAndWait()
    onElementOrNull(timeoutMs = 2000) { textAsString() == "Dismiss" }?.click()
}

fun MacrobenchmarkScope.scrollFeed() {
    val announcementList = onElement(timeoutMs = 2000) { contentDescription == "announcement_feed" }
    val timesToScrollDown = 3
    repeat(timesToScrollDown) {
        announcementList.scroll(Direction.DOWN, 1f)
    }
    repeat(timesToScrollDown) {
        announcementList.scroll(Direction.UP, 1f)
    }
}
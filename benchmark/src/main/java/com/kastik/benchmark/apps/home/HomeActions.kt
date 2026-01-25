package com.kastik.benchmark.apps.home

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.type


fun MacrobenchmarkScope.refreshFeed() {
    val announcementList = onElement(timeoutMs = 2000) { contentDescription == "announcement_feed" }
    announcementList.scroll(Direction.UP, 1f)
}

fun MacrobenchmarkScope.scrollSearchBarResults() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    val searchText = "Test"
    device.type(searchText)
    device.pressBack()
    val quickResultList =
        onElement(timeoutMs = 2000) { contentDescription == "search_bar:announcement_quick_results" }
    quickResultList.scroll(Direction.DOWN, 1f)
    device.pressBack()
    waitForStableInActiveWindow()
}
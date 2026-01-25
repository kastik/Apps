package com.kastik.benchmark.apps.search

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.pressDelete
import androidx.test.uiautomator.type

fun MacrobenchmarkScope.navigateToSearchViaQuery() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    val searchText = "Test"
    device.type(searchText)
    device.pressEnter()
    waitForStableInActiveWindow()
}

fun MacrobenchmarkScope.openSearchScreenViaTag() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    device.pressBack()
    val tagQuickResultList =
        onElement(timeoutMs = 2000) { contentDescription == "search_bar:tag_quick_results" }
    tagQuickResultList.children.random().click()
    waitForStableInActiveWindow()
}

fun MacrobenchmarkScope.navigateToSearchViaAuthor() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    device.pressBack()
    val tagQuickResultList =
        onElement(timeoutMs = 2000) { contentDescription == "search_bar:author_quick_results" }
    tagQuickResultList.children.random().click()
    waitForStableInActiveWindow()
}

fun MacrobenchmarkScope.navigateToSearchViaFAB() {
    onElement(timeoutMs = 2000) { contentDescription == "Go to search" }.click()
    waitForStableInActiveWindow()
}

fun MacrobenchmarkScope.refreshSearchScreenViaQuery() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    val searchText = "Test"
    device.type(searchText)
    device.pressEnter()
    waitForStableInActiveWindow()
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    device.pressDelete(searchText.length)
    device.pressEnter()
}

fun MacrobenchmarkScope.refreshSearchScreenViaTagQuickResult() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    device.pressBack()
    val tagQuickResultList =
        onElement(timeoutMs = 2000) { contentDescription == "search_bar:tag_quick_results" }
    tagQuickResultList.children.random().click()
}

fun MacrobenchmarkScope.refreshSearchScreenViaAuthorQuickResult() {
    onElement(timeoutMs = 2000) { contentDescription == "search_bar:input_field" }.click()
    waitForStableInActiveWindow()
    device.pressBack()
    val tagQuickResultList =
        onElement(timeoutMs = 2000) { contentDescription == "search_bar:author_quick_results" }
    tagQuickResultList.children.random().click()
}
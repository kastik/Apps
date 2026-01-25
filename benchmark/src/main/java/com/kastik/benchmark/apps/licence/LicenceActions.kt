package com.kastik.benchmark.apps.licence

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.textAsString

fun MacrobenchmarkScope.navigateToLicences() {
    onElement(timeoutMs = 2000) { contentDescription == "Settings" }.click()
    onElement(timeoutMs = 2000) { isScrollable }.scroll(Direction.DOWN, 1f)
    onElement(timeoutMs = 2000) { textAsString() == "Open source licenses" }.click()
}

fun MacrobenchmarkScope.scrollLicencesList() {
    val licenceList = onElement(timeoutMs = 2000) { contentDescription == "licences:licence_list" }
    licenceList.scroll(Direction.DOWN, 1f)
    licenceList.scroll(Direction.UP, 1f)

}

fun MacrobenchmarkScope.openRandomLicence() {
    val licenceList = onElement(timeoutMs = 2000) { contentDescription == "licences:licence_list" }
    licenceList.children.random().click()
    waitForStableInActiveWindow()
    device.pressBack()
}
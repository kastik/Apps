package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.datastore.proto.QueryScope
import com.kastik.apps.core.datastore.proto.Sort
import com.kastik.apps.core.datastore.proto.Theme
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import org.junit.Test

class PreferencesMappersTest {

    @Test
    fun themeMapsToUserTheme() {
        assertThat(Theme.System.toUserTheme()).isEqualTo(UserTheme.FOLLOW_SYSTEM)
        assertThat(Theme.Light.toUserTheme()).isEqualTo(UserTheme.LIGHT)
        assertThat(Theme.Dark.toUserTheme()).isEqualTo(UserTheme.DARK)
        assertThat(Theme.UNRECOGNIZED.toUserTheme()).isEqualTo(UserTheme.FOLLOW_SYSTEM)
    }

    @Test
    fun userThemeMapsToTheme() {
        assertThat(UserTheme.FOLLOW_SYSTEM.toTheme()).isEqualTo(Theme.System)
        assertThat(UserTheme.LIGHT.toTheme()).isEqualTo(Theme.Light)
        assertThat(UserTheme.DARK.toTheme()).isEqualTo(Theme.Dark)
    }

    @Test
    fun sortTypeMapsToSort() {
        assertThat(SortType.ASC.toSort()).isEqualTo(Sort.ASC)
        assertThat(SortType.DESC.toSort()).isEqualTo(Sort.DESC)
        assertThat(SortType.Priority.toSort()).isEqualTo(Sort.Priority)
    }

    @Test
    fun sortMapsToSortType() {
        assertThat(Sort.ASC.toSortType()).isEqualTo(SortType.ASC)
        assertThat(Sort.DESC.toSortType()).isEqualTo(SortType.DESC)
        assertThat(Sort.Priority.toSortType()).isEqualTo(SortType.Priority)
        assertThat(Sort.UNRECOGNIZED.toSortType()).isEqualTo(SortType.ASC)
    }

    @Test
    fun searchScopeMapsToQueryScope() {
        assertThat(SearchScope.Title.toQueryScope()).isEqualTo(QueryScope.Title)
        assertThat(SearchScope.Body.toQueryScope()).isEqualTo(QueryScope.Body)
        assertThat(SearchScope.TitleAndBody.toQueryScope()).isEqualTo(QueryScope.TITLE_AND_BODY)
    }

    @Test
    fun queryScopeMapsToSearchScope() {
        assertThat(QueryScope.Title.toSearchScope()).isEqualTo(SearchScope.Title)
        assertThat(QueryScope.Body.toSearchScope()).isEqualTo(SearchScope.Body)
        assertThat(QueryScope.TITLE_AND_BODY.toSearchScope()).isEqualTo(SearchScope.TitleAndBody)
        assertThat(QueryScope.UNRECOGNIZED.toSearchScope()).isEqualTo(SearchScope.Title)
    }
}
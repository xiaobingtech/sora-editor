/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2024  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 ******************************************************************************/

package com.xiaobingkj.giteer.ui.search

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.xiaobingkj.giteer.data.model.RepositoryBean
import com.xiaobingkj.giteer.data.model.UserBean
import com.xiaobingkj.giteer.data.network.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.requestNoCheck

class SearchUserViewModel: BaseViewModel() {

    val userEvent = MutableLiveData<List<UserBean>>()

    fun getSearchUser(page: Int, q: String, order: String = "desc", perpage: Int = 100) {
        requestNoCheck({
            HttpRequestCoroutine.getSearchUser(page, q)
        }, {
            userEvent.postValue(it)
        }, {
            ToastUtils.showLong(it.errorLog)
        })
    }
}
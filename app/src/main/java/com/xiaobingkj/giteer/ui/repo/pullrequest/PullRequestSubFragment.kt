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

package com.xiaobingkj.giteer.ui.repo.pullrequest

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.xiaobingkj.giteer.data.model.IssueBean
import com.xiaobingkj.giteer.data.model.PullRequestBean
import io.github.rosemoe.sora.app.R
import io.github.rosemoe.sora.app.databinding.FragmentTrendSubBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment

class PullRequestSubFragment(name: String, state: String) : BaseVmDbFragment<PullRequestSubViewModel, FragmentTrendSubBinding>() {
    private val name = name
    private val state = state
    private val adapter = PullRequestSubAdapter()
    private var page: Int = 1
    override fun layoutId(): Int = R.layout.fragment_pullrequest_sub
    override fun createObserver() {
        mViewModel.errorEvent.observe(viewLifecycleOwner) {
            mDatabind.refreshLayout.finishRefresh()
            mDatabind.refreshLayout.finishLoadMore()
        }
        mViewModel.issueEvent.observe(this, Observer {
            if (page == 1) {
                adapter.removeAtRange(IntRange(0, adapter.itemCount - 1))
            }
            adapter.addAll(it)
            adapter.notifyDataSetChanged()
            if (it.size < 20) {
                mDatabind.refreshLayout.finishLoadMoreWithNoMoreData()
            }else{
                mDatabind.refreshLayout.finishLoadMore()
            }
            mDatabind.refreshLayout.finishRefresh()
        })
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        val listView = mDatabind.listView
        listView.layoutManager = LinearLayoutManager(context)
        listView.adapter = adapter

        val loadMoreListener = object: OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                headerRefresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                footerRefresh()
            }

        }
        mDatabind.refreshLayout.setOnRefreshLoadMoreListener(loadMoreListener)

        val onItemClickListener = object: BaseQuickAdapter.OnItemClickListener<PullRequestBean> {
            override fun onClick(
                adapter: BaseQuickAdapter<PullRequestBean, *>,
                view: View,
                position: Int
            ) {
//                val bundle = Bundle()
//                val repoV3 = adapter.getItem(position)
//                bundle.putParcelable("repoV3", repoV3)
//                nav().navigate(R.id.repoFragment, bundle)
            }

        }
        adapter.setOnItemClickListener(onItemClickListener)
        headerRefresh()
    }

    fun headerRefresh() {
        page = 1
        mViewModel.getPulls(name, state, page)
    }

    fun footerRefresh() {
        page = page + 1
        mViewModel.getPulls(state, name, page)
    }

    override fun lazyLoadData() {

    }

    override fun showLoading(message: String) {

    }

}
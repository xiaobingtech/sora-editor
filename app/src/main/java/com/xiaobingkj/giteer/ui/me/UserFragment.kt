package com.xiaobingkj.giteer.ui.me

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.kingja.loadsir.core.LoadService
import com.xiaobingkj.giteer.data.model.MessageBean
import com.xiaobingkj.giteer.data.model.UserBean
import com.xiaobingkj.giteer.data.storage.Storage
import com.xiaobingkj.giteer.ext.loadServiceInit
import com.xiaobingkj.giteer.ext.showEmpty
import com.xiaobingkj.giteer.ext.showLoading
import io.github.rosemoe.sora.app.R
import io.github.rosemoe.sora.app.databinding.FragmentUserBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment
import me.hgj.jetpackmvvm.ext.nav

class UserFragment : BaseVmDbFragment<UserViewModel, FragmentUserBinding>() {
    override fun layoutId(): Int = R.layout.fragment_user
    private var user: UserBean? = null
    private var name: String = ""
    private var orgAdapter = OrgAdapter()
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    override fun createObserver() {
        mViewModel.errorEvent.observe(viewLifecycleOwner) {
            ToastUtils.showLong(it.errorLog)
        }
        mViewModel.userEvent.observe(viewLifecycleOwner) {
            loadsir.showSuccess()
            user = it
            mActivity.supportActionBar?.title = it.name
            val avatar = mDatabind.avatar
            if (it.avatar_url.equals("https://gitee.com/assets/no_portrait.png")) {
                avatar.setTextAndColor(it.name.substring(0, 1), R.color.gray)
            }else{
                Glide.with(mActivity).load(it.avatar_url).into(avatar)
            }
            mDatabind.name.text = it.name
            mDatabind.desc.text = it.bio
            mDatabind.time.text = TimeUtils.date2String(TimeUtils.string2Date(it.created_at, "yyyy-MM-dd'T'HH:mm:ssXXX"), "yyyy-MM-dd HH:mm:ss") + "加入"
            if (it.email != null) {
                mDatabind.emailAddress.text = it.email
            }
            mDatabind.repoNum.text = it.public_repos.toString()
            mDatabind.followerNum.text = it.followers.toString()
            mDatabind.followNum.text = it.following.toString()
        }
        mViewModel.historyEvent.observe(viewLifecycleOwner) {
            mDatabind.contribution.contributions = it.data.contribution_calendar.year_streak
        }
        mViewModel.orgEvent.observe(viewLifecycleOwner) {
            orgAdapter.removeAtRange(IntRange(0, orgAdapter.itemCount - 1))
            orgAdapter.addAll(it)
            orgAdapter.notifyDataSetChanged()
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        name = arguments?.getString("name")!!
        mDatabind.click = ProxyClick()

        loadsir = loadServiceInit(mDatabind.layout) {
            //点击重试时触发的操作
            requestData()
        }

        val orgListView = mDatabind.org
        orgListView.layoutManager = LinearLayoutManager(context)
        orgListView.adapter = orgAdapter
    }

    fun requestData() {
        if (user == null) {
            loadsir.showLoading()
        }
        mViewModel.getUser(name)
        mViewModel.getOrgs(name)
        mViewModel.getUserBrowser_history(name)
    }

    override fun lazyLoadData() {
        requestData()
    }

    override fun showLoading(message: String) {

    }

    inner class ProxyClick() {
        fun toRepoList() {
            val bundle = Bundle()
            bundle.putString("type", "user")
            bundle.putString("name", user?.login)
            nav().navigate(R.id.repoListFragment, bundle)
        }

        fun toFollower() {
            val bundle = Bundle()
            bundle.putString("type", "user")
            bundle.putString("action", "follower")
            bundle.putString("name", user?.login)
            nav().navigate(R.id.userListFragment, bundle)
        }

        fun toFollowing() {
            val bundle = Bundle()
            bundle.putString("type", "user")
            bundle.putString("action", "following")
            bundle.putString("name", user?.login)
            nav().navigate(R.id.userListFragment, bundle)
        }
        fun toChat() {
//            val bundle = Bundle()
//            val mutableList = mutableListOf<MessageBean.ListDTO>()
//            val message = MessageBean.ListDTO()
//            message.content = ""
//            message.sender =
//            mutableList.add(0, message)
//            bundle.putParcelableArray("msgs", mutableList.toTypedArray())
//            nav().navigate(R.id.chatFragment, bundle)
        }
    }

}
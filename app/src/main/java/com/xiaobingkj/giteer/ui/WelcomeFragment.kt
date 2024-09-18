package com.xiaobingkj.giteer.ui

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.xiaobingkj.giteer.data.GithubVersion
import com.xiaobingkj.giteer.data.storage.Storage
import com.xiaobingkj.giteer.ext.loadServiceInit
import com.xiaobingkj.giteer.ui.login.LoginViewModel
import io.github.rosemoe.sora.app.R
import io.github.rosemoe.sora.app.databinding.FragmentWelcomeBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.nav


class WelcomeFragment : BaseVmDbFragment<LoginViewModel, FragmentWelcomeBinding>() {
    override fun layoutId(): Int = R.layout.fragment_welcome
    override fun createObserver() {
        mViewModel.tokenEvent.observe(viewLifecycleOwner) {
            Storage.token = it
            nav().navigate(R.id.tabFragment)
        }
        mViewModel.errorEvent.observe(viewLifecycleOwner) {
            ToastUtils.showLong(it.errorLog)
            nav().navigate(R.id.loginFragment)
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        if (Storage.isLogin) {
            if (Storage.token.refresh_token != null) {
                mViewModel.refreshOauthToken(Storage.token.refresh_token)
            }
        }else{
            nav().navigate(R.id.loginFragment)
        }
    }

    override fun lazyLoadData() {

    }

    override fun showLoading(message: String) {

    }

}
/*
 * Copyright (c) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.android.sports.databinding.FragmentSportsListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class SportsListFragment : Fragment() {

    private val sportsViewModel: SportsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSportsListBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSportsListBinding.bind(view)
        val slidingPaneLayout = binding.slidingPaneLayout
        /**
         * 在手机等小屏幕设备上，如果列表窗格和详细信息窗格重叠，
         * 那么在默认情况下，用户可以向两个方向滑动，这样一来，即使在没有使用手势导航的情况下，
         * 也可以随意在两个窗格之间切换。
         *
         * 通过设置SlidingPaneLayout的锁定模式，锁定或解锁详细信息窗格。
         *
         * 锁定模式仅用于控制哪些用户手势是可行的。无论设置何种锁定模式，
         * 都可以随时以编程方式打开或关闭SlidingPaneLayout。
         */
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
        // Connect the SlidingPaneLayout to the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, //该回调仅在fragment的生命周期内有效
            SportsListOnBackPressedCallback(slidingPaneLayout)
        )

        // Initialize the adapter and set it to the RecyclerView.
        val adapter = SportsAdapter {
            // Update the user selected sport as the current sport in the shared viewmodel
            // This will automatically update the dual pane content
            sportsViewModel.updateCurrentSport(it)
            // Slide the detail pane into view. If both panes are visible,
            // this has no visible effect.
            // val action = SportsListFragmentDirections.actionSportsListFragmentToNewsFragment()
            // this.findNavController().navigate(action)
            // 使第二个窗格显示在第一个窗格前方。
            // 如果两个窗格都可见（例如在平板电脑上），该操作将不会产生任何可见的影响。
            binding.slidingPaneLayout.openPane()
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(sportsViewModel.sportsData)
    }
}

/**
 * Callback providing custom back navigation.
 */

class SportsListOnBackPressedCallback(
    private val slidingPaneLayout: SlidingPaneLayout
) : OnBackPressedCallback( //自定义返回导航，返回键的回调
    // Set the default 'enabled' state to true only if it is slidable (i.e., the panes
    // are overlapping) and open (i.e., the detail pane is visible).
    // 仅当第二个窗格可滑动时，isSlideable为true，这种情况会出现在较小的屏幕上，且只会显示一个窗格。
    // 如果第二个窗格（内容窗格）完全打开，isOpen为true。
    // 即确保仅在小屏幕设备上以及内容窗格处于打开状态时，才会启用回调。
    slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen
), SlidingPaneLayout.PanelSlideListener { //监听和监控与滑动窗格相关的事件

    // 将SportsListOnBackPressedCallback监听器类添加到监听器列表中
    // 改列表将收到详细信息窗格滑动事件通知
    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }

    override fun handleOnBackPressed() {
        // Return to the list pane when the system back button is pressed.
        // 关闭内容窗格并返回到列表窗格
        slidingPaneLayout.closePane()
    }

    // 详细信息窗格滑动
    override fun onPanelSlide(panel: View, slideOffset: Float) {}

    // 详细信息窗格打开（可见）时，启用OnBackPressedCallback回调
    override fun onPanelOpened(panel: View) {
        // Intercept the system back button when the detail pane becomes visible.
        isEnabled = true //相当于setEnable(true)
    }

    // 详细信息窗格关闭时
    override fun onPanelClosed(panel: View) {
        // Disable intercepting the system back button when the user returns to the
        // list pane.
        isEnabled = false
    }
}

package com.dicoding.refactorgithubsubmissionapi.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.databinding.FragmentUserFollowerFollowingBinding
import com.dicoding.refactorgithubsubmissionapi.adapter.SectionsPagerAdapter
import com.dicoding.refactorgithubsubmissionapi.adapter.UsersListAdapter
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.MainViewModel
import com.dicoding.refactorgithubsubmissionapi.ui.view_model.UserFollowerFollowingViewModel

class UserFollowerFollowingFragment : Fragment() {

    private lateinit var binding: FragmentUserFollowerFollowingBinding
    private lateinit var userFollowerFollowingViewModel: UserFollowerFollowingViewModel
    private lateinit var adapter: UsersListAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentUserFollowerFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set layout manager untuk RecyclerView yg ada di fragment_user_follower_following.xml
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowerFollowingAccount.layoutManager = linearLayoutManager

        // buat garis pemisah antar item di RecyclerView
        val itemDecoration = DividerItemDecoration(requireActivity(), linearLayoutManager.orientation)
        binding.rvFollowerFollowingAccount.addItemDecoration(itemDecoration)

        // dapetin ViewModel
        userFollowerFollowingViewModel = ViewModelProvider(requireActivity())[UserFollowerFollowingViewModel::class.java]

        userFollowerFollowingViewModel.isLoading.observe(requireActivity()) { value ->
            if (value == true) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }
        // dapetin posisi dan username dari SectionsPager
        val position = arguments?.getInt(SectionsPagerAdapter.POSITION)
        val username = arguments?.getString(SectionsPagerAdapter.USERNAME)

        // error msg toast
        userFollowerFollowingViewModel.errorMsg.observe(requireActivity()) { message ->
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        }

        adapter = UsersListAdapter()

        // observe data follower maupun following
        when (position) {
            1 -> {
                if (userFollowerFollowingViewModel.userFollowersList.value == null){
                    userFollowerFollowingViewModel.getFollowersFromTheUser(usernameQuery = username.toString())
                }
                userFollowerFollowingViewModel.userFollowersList.observe(viewLifecycleOwner) { usersList ->
                    if (usersList != null) {
                        setUsersListDataToRecyclerView(usersList)
                    }
                }
            }
            2 -> {
                if (userFollowerFollowingViewModel.userFollowingList.value == null){
                    userFollowerFollowingViewModel.getFollowingFromTheUser(usernameQuery = username.toString())
                }
                userFollowerFollowingViewModel.userFollowingList.observe(viewLifecycleOwner) { usersList ->
                    if (usersList != null) {
                        setUsersListDataToRecyclerView(usersList)
                    }
                }
            }
        }

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.userDetail.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { user ->
                val moveDetailUserActivityIntent = Intent(requireActivity(), DetailUserActivity::class.java)
                moveDetailUserActivityIntent.putExtra("DATA", user)
                startActivity(moveDetailUserActivityIntent)
            }
        }

    }

    private fun setUsersListDataToRecyclerView(accountsList: List<ItemsItem?>) {
        adapter.submitList(accountsList)
        binding.rvFollowerFollowingAccount.adapter = adapter

        adapter.setOnItemClickListener(object : UsersListAdapter.OnItemClickListener {
            override fun onItemClick(userAccount: ItemsItem) {
                mainViewModel.getGithubUserDetailAccount(usernameQuery = userAccount.login.toString())
            }
        })
    }
}
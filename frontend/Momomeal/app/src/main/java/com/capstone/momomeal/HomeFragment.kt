package com.capstone.momomeal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import com.capstone.momomeal.databinding.FragmentHomeBinding
import com.capstone.momomeal.feature.BaseFragment
import com.capstone.momomeal.feature.Category
import com.capstone.momomeal.feature.Chatroom
import com.capstone.momomeal.feature.adapter.ChatroomAdapter


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val TAG = "HomeFragment"
    private lateinit var mainActivity: MainActivity
    private val createChatFragment = CreateChatFragment()
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { Address ->
            if (Address.resultCode == Activity.RESULT_OK) {
                Address.data?.let {
                    binding.fragmentHomeEditAddress.text = it.getStringExtra("data")
                }
            }
        }
    val chatroomList = arrayListOf<Chatroom>(
        //test
        Chatroom("Bhc 뿌링클 뿌개실분 ~ ", 123, Category.Chicken, 3, "국민대학교 정문", 3.3, listOf(7, 49, 89)),
        Chatroom(
            "밤 12시에 족발 먹을 사람 있니?",
            128,
            Category.BoiledPork,
            3,
            "서울대입구 4번출구",
            3.9,
            listOf(3, 29, 69)
        ),
        Chatroom("분식 먹을 돼지만", 128, Category.Snackbar, 3, "먹자골목", 3.9, listOf(3, 29, 69)),
        Chatroom(
            "스타벅스 새로나온 케이크 먹자",
            128,
            Category.CafeAndDesert,
            3,
            "강남역 4번출구",
            3.9,
            listOf(3, 29, 69)
        ),
        Chatroom("배고픈데 잠이 오니?", 128, Category.Pizza, 3, "마포대표 근처", 3.9, listOf(3, 29, 69)),
        Chatroom("먹고 죽자", 128, Category.Korean, 3, "성북구 길음1동 삼부아파트", 3.9, listOf(3, 29, 69)),
        Chatroom("중국집 시켜먹을 사람 컴", 128, Category.Chinese, 3, "인천 차이나타운", 3.9, listOf(3, 29, 69))
    )

    // onCreate 이후 화면을 구성하는 코드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val retView = super.onCreateView(inflater, container, savedInstanceState)

        val chatroomadapter = ChatroomAdapter(requireContext(), chatroomList)
        binding.fragmentHomeEditAddress.setOnClickListener{
            val intent = Intent(requireActivity().application, MyAddressActivity::class.java)
            startForResult.launch(intent)
        }
        binding.fragmentHomeRecycler.adapter = chatroomadapter

        mainActivity = (activity as MainActivity)
//        val transaction = mainActivity
//            .supportFragmentManager.beginTransaction()
//            .replace(R.id.fl_main_full_container, createChatFragment)
        binding.fabHome.setOnClickListener {
//            transaction.commit() // 이쪽이 제대로 작동함
            val createChatFragment = CreateChatFragment()
            createChatFragment.show(mainActivity.supportFragmentManager, createChatFragment.tag)
        }
        initSearch(binding.fragmentHomeSearchbar)
        return retView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initSearch(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainActivity.findNavController(R.id.fr_main_navi_host).navigate(R.id.action_itemHome_to_searchHome)
                //mainActivity.changeFragment(result)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                //
                return false
            }
        })
    }
}

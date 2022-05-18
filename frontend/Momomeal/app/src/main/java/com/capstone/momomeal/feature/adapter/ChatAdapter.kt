package com.capstone.momomeal.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.capstone.momomeal.R
import com.capstone.momomeal.databinding.ItemMyMsgBinding
import com.capstone.momomeal.databinding.ItemOtherMsgBinding
import com.capstone.momomeal.databinding.ItemOtherMsgFullBinding
import com.capstone.momomeal.data.Chat
import com.capstone.momomeal.data.User_light
import com.capstone.momomeal.data.membInfo
import java.lang.IllegalArgumentException

class ChatAdapter(
//    val context: Context,
    val myInfo: User_light,
    var membMap: HashMap<Int, membInfo>,
    var chatList: ArrayList<Chat>
) : RecyclerView.Adapter<ChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return when (viewType) {
            OTHER_MSG_FULL -> ChatViewHolder.OtherMsgFullViewHolder(
                ItemOtherMsgFullBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            OTHER_MSG -> ChatViewHolder.OtherMsgViewHolder(
                ItemOtherMsgBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            MY_MSG -> ChatViewHolder.MyMsgViewHolder(
                ItemMyMsgBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun getItemCount(): Int = chatList.size

    // 채팅의 경우 RecyclerView에 들어가는 item의 타입이 3개이므로, 타입의 구별이 필요합니다.
    // 그 구별을 위한 함수입니다.
    override fun getItemViewType(position: Int): Int {
        if (chatList[position].uid == myInfo.idUser) {
            return MY_MSG
//        } else if (position == 0) {
//            return OTHER_MSG_FULL
        } else if (position >= 0 && chatList[position - 1].uid != myInfo.idUser){
            return OTHER_MSG_FULL
        } else {
            return OTHER_MSG
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder.OtherMsgFullViewHolder
            -> holder.bind(chatList[position], membMap.get(chatList[position].uid))
            is ChatViewHolder.OtherMsgViewHolder -> holder.bind(chatList[position])
            is ChatViewHolder.MyMsgViewHolder -> holder.bind(chatList[position])
        }
    }
    // 여기서부터는 전용 함수들
    // membMap 갱신용 함수
    fun updateMembers(map: HashMap<Int, membInfo>) {
        membMap = map
        notifyDataSetChanged()
    }
    fun addMember(id: Int, info: membInfo) {
        membMap.set(id, info)
        notifyDataSetChanged()
    }
    fun removeMembers(id: Int) = membMap.remove(id)

    // chatList 갱신용 함수
    fun addChat(chat: Chat) {
        chatList.add(chat)
        notifyItemInserted(chatList.size - 1)
    }

    //  메시지 타입 구별용 객체
    companion object {
        private const val OTHER_MSG_FULL = 0
        private const val OTHER_MSG = 1
        private const val MY_MSG = 2
    }
}

sealed class ChatViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class OtherMsgFullViewHolder(
        private val binding: ItemOtherMsgFullBinding
    ) : ChatViewHolder(binding) {
        fun bind(item: Chat, info: membInfo?) {
            if (info != null) {
                binding.ivOtherProfile.setImageBitmap(info.bitmap)
                binding.tvOtherName.text = info.name
            } else {
                binding.ivOtherProfile.setImageResource(R.drawable.ic_basic_profile)
                binding.tvOtherName.text = "익명"
            }
            binding.tvOtherMsgFull.text = item.chatContent

        }
    }

    class OtherMsgViewHolder(
        val binding: ItemOtherMsgBinding
    ) : ChatViewHolder(binding) {
        fun bind(item: Chat) {
            binding.tvOtherMsg.text = item.chatContent
        }
    }

    class MyMsgViewHolder(
        val binding: ItemMyMsgBinding
    ): ChatViewHolder(binding) {
        fun bind(item: Chat) {
            binding.tvMyMsg.text = item.chatContent
        }
    }
}

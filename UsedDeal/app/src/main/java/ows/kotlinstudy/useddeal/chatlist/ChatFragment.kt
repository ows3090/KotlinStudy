package ows.kotlinstudy.useddeal.chatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ows.kotlinstudy.useddeal.DBKey.Companion.CHILD_CHAT
import ows.kotlinstudy.useddeal.DBKey.Companion.DB_USERS
import ows.kotlinstudy.useddeal.R
import ows.kotlinstudy.useddeal.chatdetail.ChatRoomActivity
import ows.kotlinstudy.useddeal.databinding.FragmentChatBinding
import ows.kotlinstudy.useddeal.home.ArticleAdapter

class ChatFragment: Fragment(R.layout.fragment_chat) {

    private var binding: FragmentChatBinding?= null
    private lateinit var chatListAdater: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()
    private lateinit var chatDB: DatabaseReference

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatBinding = FragmentChatBinding.bind(view)
        binding = fragmentChatBinding

        chatListAdater = ChatListAdapter(onItemClicked = { chatRoom ->
                context?.let {
                    val intent = Intent(it, ChatRoomActivity::class.java)
                    intent.putExtra("chatKey",chatRoom.key)
                    startActivity(intent)
                }
            }
        )
        chatRoomList.clear()

        fragmentChatBinding.chatListRecyclerView.adapter = chatListAdater
        fragmentChatBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if (auth.currentUser == null){
            return
        }

        auth.currentUser?.uid?.let {
            chatDB = Firebase.database.reference.child(DB_USERS).child(it).child(CHILD_CHAT)
        }

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("msg","onDataChanage")
                snapshot.children.forEach{
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }
                chatListAdater.submitList(chatRoomList)
                chatListAdater.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onResume() {
        super.onResume()

        chatListAdater.notifyDataSetChanged()
    }

}
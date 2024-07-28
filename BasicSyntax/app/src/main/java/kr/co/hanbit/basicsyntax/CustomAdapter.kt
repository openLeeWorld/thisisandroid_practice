package kr.co.hanbit.basicsyntax

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.hanbit.basicsyntax.databinding.ItemRecyclerBinding

class CustomAdapter: RecyclerView.Adapter<Holder>() {

    var userList: Repository? = null // 데이터셋

    override fun getItemCount(): Int {
        return userList?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 홀더 생성
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // XML 레이아웃을 실제 코드 상에서 사용가능한 객체로 변환하는 inflate
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 실제 목록에 뿌려지는 아이템을 그려주는
        val user = userList?.get(position)
        holder.setUser(user)
    }
}

class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    fun setUser(user: RepositoryItem?) {
        user?.let {
            binding.textName.text = user.name
            binding.textId.text = user.node_id
            Glide.with(binding.imageAvatar).load(user.owner.avatar_url).into(binding.imageAvatar)
            //GlideApp.with(binding.imageAvatar).load(user.owner.avatar_url).into(binding.imageAvatar)
            // with(컨텍스트).load(이미지 주소).into(이미지 뷰의 id)
        }
    }
}